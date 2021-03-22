// access other classes within this folder
package ca.uhn.fhir.example;
import ca.uhn.fhir.example.Helper;
import ca.uhn.fhir.example.RespRate;

// general fhir things
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;

// access java common operations
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.io.*;
import java.util.Map.Entry;  

//added to build different vitals profiles
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StringType;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.SampledData;
import org.hl7.fhir.r4.model.Type;

// added to access firebase
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.ImmutableMap;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.CollectionReference;

public class ObservationResourceProvider implements IResourceProvider {

   private Map<String, Observation> myObservations = new HashMap<String, Observation>();
   private Firestore db;

   public ObservationResourceProvider(Firestore _db) {
      db = _db;
      setDummyResp();  
   }

   @Override
   public Class<? extends IBaseResource> getResourceType() {
      return Observation.class;
   }

   /**
    * Simple implementation of the "read" method
    */

   @Read()
   public Observation read(@IdParam IdType theId) {
      searchForDocument(db, theId);
      Observation retVal = myObservations.get(theId.getIdPart());

      if (retVal == null) {
         throw new ResourceNotFoundException(theId);
      }
      return retVal;
   }

   private void setDummyRespWithExtension() {
      RespRate theResp = new RespRate();
      theResp.setPetName(new StringType("Fido"));
      theResp.getRespMeasurements().add(new DateTimeType("2010-01-02"));
      theResp.getRespMeasurements().add(new DateTimeType("2014-01-26T11:11:11"));
      theResp.setId("2");
      myObservations.put("2", theResp);
   }

   private void setDummyResp() {
      RespRate myResp = new RespRate();
      Reference patientReference = new Reference("Patient/g2");
      myResp.setSubject(patientReference);
      myResp.setId("1");
      Coding myCoding = new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs");
      CodeableConcept myCodeConcept = new CodeableConcept(myCoding);
      myResp.addCategory(myCodeConcept);
      Double secondsBetweenMeasurements = 60.0;
      Double milliSecondsBetweenMeasurements = secondsBetweenMeasurements*1000;
      SampledData mySampledData = new SampledData​(new Quantity(0.0), new DecimalType(milliSecondsBetweenMeasurements), new PositiveIntType(1));
      mySampledData.setData("14.2 17.8 19.2");
      myResp.setValue(mySampledData);
      //myResp.setValue(new Quantity(null,26,"http://unitsofmeasure.org","/min","breaths/min"));
      Long unixTimeStamp = Long.parseLong("1616442865");
      DateTimeType myEffectiveDateTime = new DateTimeType(Helper.formatDate(unixTimeStamp));
      myResp.setEffective(myEffectiveDateTime);
      myResp.setCode(new CodeableConcept(new Coding("http://loinc.org","9279-1","Respiratory rate")));
      
      myObservations.put("1", myResp);
   }


   private void setRespRateResource(@IdParam IdType theId, QueryDocumentSnapshot document) {
      Observation myObs = new Observation();

      Meta myMeta = new Meta();
      myMeta.addProfile("http://hl7.org/fhir/StructureDefinition/resprate");
      CodeableConcept myCode = new CodeableConcept(new Coding("http://loinc.org","9279-1","Respiratory rate"));

      String startTime = document.get("startTimeInSeconds").toString();
      String localOffset = document.get("startTimeOffsetInSeconds").toString();
      HashMap <String, Double> measurements = (HashMap)document.get("timeOffsetEpochToBreaths");

      
      //Quantity myQuantity = new Quantity(null,value,"http://unitsofmeasure.org","/min","breaths/min");
      
      Double secondsBetweenMeasurements = 60.0;
      Double milliSecondsBetweenMeasurements = secondsBetweenMeasurements*1000;
      SampledData mySampledData = new SampledData​(new Quantity(0.0), new DecimalType(milliSecondsBetweenMeasurements), new PositiveIntType(1));
      
      // put measurements in order and grab all the values in order.
      HashMap <String, Double> sortedMeasurements = Helper.sortMapByKeyStringToDouble(measurements);
      
      String timeSeriesData = "";
      int counter = 0;
      String firstKey = "";
      for (Entry<String, Double> entry : sortedMeasurements.entrySet())   {
         if (counter == 0) {
            firstKey = entry.getKey();
         }  
         timeSeriesData+=entry.getValue();
         timeSeriesData += " ";
         System.out.println(entry.getKey() +"\t"+entry.getValue());  
         counter++;
      
      }
      timeSeriesData = timeSeriesData.substring(0, timeSeriesData.length() - 1);

      mySampledData.setData(timeSeriesData);

      Long unixTimeStamp = Long.parseLong(startTime) + Long.parseLong(localOffset) + Long.parseLong(firstKey);
      DateTimeType myEffectiveDateTime = new DateTimeType(Helper.formatDate(unixTimeStamp));

      myObs = setCommonVitalsFields(myObs, document, theId, myEffectiveDateTime, mySampledData, myMeta, myCode);
      myObservations.put(theId.getIdPart(), myObs);
   }

   private void setPulseOxResource(@IdParam IdType theId, QueryDocumentSnapshot document) {
      Observation myObs = new Observation();

      Meta myMeta = new Meta();
      myMeta.addProfile("http://hl7.org/fhir/StructureDefinition/oxygensat");
      CodeableConcept myCode = new CodeableConcept(new Coding("http://loinc.org","2708-6","Oxygen saturation"));
      HashMap <String, Long> measurements = (HashMap)document.get("timeOffsetSpo2Values");

      if (measurements == null) {
         myObservations.put(theId.getIdPart(), myObs);
         return;
      }

      String startTime = document.get("startTimeInSeconds").toString();
      String localOffset = document.get("startTimeOffsetInSeconds").toString();

      Double secondsBetweenMeasurements = 60.0;
      Double milliSecondsBetweenMeasurements = secondsBetweenMeasurements*1000;
      SampledData mySampledData = new SampledData​(new Quantity(0.0), new DecimalType(milliSecondsBetweenMeasurements), new PositiveIntType(1));
      
      // put measurements in order and grab all the values in order.
      HashMap <String, Long> sortedMeasurements = Helper.sortMapByKeyStringToLong(measurements);
      
      String timeSeriesData = "";
      int counter = 0;
      String firstKey = "";
      for (Entry<String, Long> entry : sortedMeasurements.entrySet())   {
         if (counter == 0) {
            firstKey = entry.getKey();
         }  
         timeSeriesData+=entry.getValue();
         timeSeriesData += " ";
         System.out.println(entry.getKey() +"\t"+entry.getValue());  
         counter++;
      
      }
      timeSeriesData = timeSeriesData.substring(0, timeSeriesData.length() - 1);

      mySampledData.setData(timeSeriesData);

      // This fetches a random entry
      // Map.Entry<String,Long> entry = measurements.entrySet().iterator().next();

      // String key = entry.getKey();
      // Long value = entry.getValue();
      // System.out.println("Time offset: " + key);
      // System.out.println("SpO2 value: " + value);

      Long unixTimeStamp = Long.parseLong(startTime) + Long.parseLong(localOffset) + Long.parseLong(firstKey);

      DateTimeType myEffectiveDateTime = new DateTimeType(Helper.formatDate(unixTimeStamp));
      //Quantity myQuantity = new Quantity(null,value,"http://unitsofmeasure.org","%","%");

      myObs = setCommonVitalsFields(myObs, document, theId, myEffectiveDateTime, mySampledData, myMeta, myCode);
      myObservations.put(theId.getIdPart(), myObs);
   }

   // Garmin stress numbers are "derived based on a combination of many device sensors and 
   // will automatically adjust to the wearer of the device and gain accuracy over time 
   // as the stress algorithms learn the user’s natural biometric norms"
   // The stress units don't correspond to a commonly agreed upon metric, so I cannot use 
   // a LOINC coding system for this, which is required to be part of a vitals signs profile. 
   // I do not have units either. 
   // Use a "best effort" approach - create a new Garmin coding 
   private void setStressResource(@IdParam IdType theId, QueryDocumentSnapshot document) {
      Observation myObs = new Observation();

      CodeableConcept myCode = new CodeableConcept(new Coding("https://connect.garmin.com/","stress-code","Stress Summaries"));
      HashMap <String, Long> measurements = (HashMap)document.get("timeOffsetStressLevelValues");

      if (measurements == null) {
         myObservations.put(theId.getIdPart(), myObs);
         return;
      }

      String startTime = document.get("startTimeInSeconds").toString();
      String localOffset = document.get("startTimeOffsetInSeconds").toString();

      Double secondsBetweenMeasurements = 180.0;
      Double milliSecondsBetweenMeasurements = secondsBetweenMeasurements*1000;
      SampledData mySampledData = new SampledData​(new Quantity(0.0), new DecimalType(milliSecondsBetweenMeasurements), new PositiveIntType(1));
      
      // put measurements in order and grab all the values in order.
      HashMap <String, Long> sortedMeasurements = Helper.sortMapByKeyStringToLong(measurements);
      
      String timeSeriesData = "";
      int counter = 0;
      String firstKey = "";
      for (Entry<String, Long> entry : sortedMeasurements.entrySet())   {
         if (counter == 0) {
            firstKey = entry.getKey();
         }  
         timeSeriesData+=entry.getValue();
         timeSeriesData += " ";
         System.out.println(entry.getKey() +"\t"+entry.getValue());  
         counter++;
      
      }
      // remove the last space
      timeSeriesData = timeSeriesData.substring(0, timeSeriesData.length() - 1);

      mySampledData.setData(timeSeriesData);

      Long unixTimeStamp = Long.parseLong(startTime) + Long.parseLong(localOffset) + Long.parseLong(firstKey);

      DateTimeType myEffectiveDateTime = new DateTimeType(Helper.formatDate(unixTimeStamp));
      
      Meta myMeta = null;
      myObs = setCommonVitalsFields(myObs, document, theId, myEffectiveDateTime, mySampledData, myMeta, myCode);
      myObservations.put(theId.getIdPart(), myObs);

   }

   private Observation setCommonVitalsFields(Observation myObs, QueryDocumentSnapshot document, 
      @IdParam IdType theId, DateTimeType myEffectiveDateTime, Type myValue,
      Meta myMeta, CodeableConcept myCode) {

      myObs.setSubject(new Reference("Patient/" + document.getString("user_id")));
      myObs.setId(theId.getIdPart());
      myObs.setStatus(ObservationStatus.FINAL);
      Coding myCategoryCoding = new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs");
      myObs.addCategory(new CodeableConcept(myCategoryCoding));
      myObs.setEffective(myEffectiveDateTime);

      if (myMeta!= null) {
         myObs.setMeta(myMeta);
      }
      if (myCode != null){
         myObs.setCode(myCode);
      }
      if (myValue != null) {
         myObs.setValue(myValue); 
      }
       
      return myObs;
   }

   private void searchForDocument(Firestore db, @IdParam IdType theId) {
      System.out.printf("===========================================\nSearching for Observation with id %s \n", theId.getIdPart());
      String[] theIdParts = theId.getIdPart().split(":");

      if (theIdParts.length != 2) {
         System.out.println("To return a non-dummy resource the input ID should be the form collectionName:summaryId");
         return;
      }

      String collName = theIdParts[0];
      String theSummaryId = theIdParts[1];

      try {
         ApiFuture<QuerySnapshot> future = db.collection(collName).whereEqualTo("summaryId", theSummaryId).get();
         List<QueryDocumentSnapshot> documents = future.get().getDocuments();
         System.out.println("Number of matching documents: " + documents.size());
         
         if (documents.size() >= 1) {
            QueryDocumentSnapshot document = documents.get(0);
            System.out.println("Document data: " + document.getData());

            switch (collName) {
               case "g_respiration": 
                  setRespRateResource(theId, document);
                  break;
               case "g_pulseOx":
                  setPulseOxResource(theId, document);
                  break;
               case "g_stress":
                  setStressResource(theId, document);
                  break;
            }
            
            return;
         }
         System.out.println("No matching documents found");
         
      }

      catch (Exception e) {
         e.printStackTrace();
      }     

      return;
   }


}
