// access other classes within this folder
package ca.uhn.fhir.example;
import ca.uhn.fhir.example.Helper;
import ca.uhn.fhir.example.RespRate;
import ca.uhn.fhir.example.Mdc;

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
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent;

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
      QueryDocumentSnapshot document = Helper.searchForDocument(db, theId);
      setResourceWrapper(document, theId);

      Observation retVal = myObservations.get(theId.getIdPart());

      if (retVal == null) {
         throw new ResourceNotFoundException(theId);
      }
      return retVal;
   }

   private void setResourceWrapper(QueryDocumentSnapshot document, @IdParam IdType theId) {
      if (document == null) {
         return;
      }

      String[] theIdParts = theId.getIdPart().split(":");
      
      if (theIdParts.length != 2) {
         return;
      }

      String collName = theIdParts[0];
      String theSummaryId = theIdParts[1];

      switch (collName.toLowerCase()) {
         case "g_respiration": 
            setRespRateResource(theId, document);
            break;
         case "g_pulseOx":
            setPulseOxResource(theId, document);
            break;
         case "g_stress":
            setStressResource(theId, document);
            break;
         case "g_epoch":
            setEpochResource(theId, document);
            break;
         case "g_activity":
            setActivityResource(theId, document);
      }
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

   private void setActivityResource(@IdParam IdType theId, QueryDocumentSnapshot document) {
      Observation myObs = new Observation();
      myObs.setId(theId.getIdPart());
      myObs.setStatus(ObservationStatus.FINAL);
      myObs.setSubject(new Reference("Patient/" + document.getString("user_id")));
      myObs.addCategory(new CodeableConcept(
         new Coding("http://terminology.hl7.org/CodeSystem/observation-category","physical-activity","Physical Activity")
      ));
      myObs.setCode(new CodeableConcept (
         new Coding("https://connect.garmin.com/", "activity-summary-code","High-level fitness activity summaries")
      ));

      myObs.setDevice(new Reference("Device/1"));

      Period periodOfActivity = Helper.formatPeriod(document, "durationInSeconds");
      
      if (periodOfActivity != null) {
         myObs.setEffective(periodOfActivity);
      }
      
      List<Observation.ObservationComponentComponent> theComponentList = 
      new ArrayList<Observation.ObservationComponentComponent>(){};

      theComponentList = setCommonActivityComponents(theComponentList, document);

      if (document.get("startingLatitudeInDegree") != null) {
         Double startingLat = document.getDouble("startingLatitudeInDegree");
         Observation.ObservationComponentComponent latObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("http://loinc.org","91588-4","Latitude Event")));
           
         latObservation.setValue(new Quantity(startingLat));       
         theComponentList.add(latObservation);
      }

      if (document.get("startingLongitudeInDegree") != null) {
         Double startingLat = document.getDouble("startingLongitudeInDegree");
         Observation.ObservationComponentComponent longObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("http://loinc.org","91589-2","Longitude Event")));
           
         longObservation.setValue(new Quantity(startingLat));       
         theComponentList.add(longObservation);
      }
      myObs.setComponent(theComponentList);
      myObservations.put(theId.getIdPart(), myObs);

      if (document.get("averageHeartRateInBeatsPerMinute") != null) {
         Long avgBPM = document.getLong("averageHeartRateInBeatsPerMinute");
         Observation.ObservationComponentComponent avgBPMObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("http://loinc.org","55425-3","Heart rate unspecified time mean by Pedometer")));
         avgBPMObservation.setValue(new Quantity(null, avgBPM, "http://unitsofmeasure.org", "bpm","beats/min"));       
         theComponentList.add(avgBPMObservation);
      }

      if (document.get("maxHeartRateInBeatsPerMinute") != null) {
         Long maxBPM = document.getLong("maxHeartRateInBeatsPerMinute");
         Observation.ObservationComponentComponent maxBPMObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("http://loinc.org","55426-1","Heart rate unspecified time maximum by Pedometer")));
         maxBPMObservation.setValue(new Quantity(null, maxBPM, "http://unitsofmeasure.org", "bpm","beats/min"));       
         theComponentList.add(maxBPMObservation);
      }

      if (document.get("averageRunCadenceInStepsPerMinute") != null) {
         Long code = Mdc.get32BitCodeFromReferenceId("MDC_HF_CAD");
         Double averageCadence = document.getDouble("averageRunCadenceInStepsPerMinute");
         Observation.ObservationComponentComponent avgCadenceObs = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("urn:iso:std:iso:11073:10101",String.valueOf(code),"The cadence over a period of time (average cadence in this case)")));
         avgCadenceObs.setValue(new Quantity(null, averageCadence, "http://unitsofmeasure.org", "steps/min","steps/min"));       
         theComponentList.add(avgCadenceObs);
      }

      if (document.get("maxRunCadenceInStepsPerMinute") != null) {
         Long code = Mdc.get32BitCodeFromReferenceId("MDC_HF_CAD");
         Double maxCadence = document.getDouble("maxRunCadenceInStepsPerMinute");
         Observation.ObservationComponentComponent maxCadenceObs = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("urn:iso:std:iso:11073:10101",String.valueOf(code),"The cadence over a period of time (max cadence in this case)")));
         maxCadenceObs.setValue(new Quantity(null, maxCadence, "http://unitsofmeasure.org", "steps/min","steps/min"));       
         theComponentList.add(maxCadenceObs);
      }


      // unable to find pace codings in either MDC or LOINC systems - for recording average pace in min/km and max pace in min/km
      // but i think this is a just a different way to represent the speed, so pace can be calculated from speed 

      if (document.get("averageSpeedInMetersPerSecond") != null) {
         Long code = Mdc.get32BitCodeFromReferenceId("MDC_HF_SPEED");
         Double measurement = document.getDouble("averageSpeedInMetersPerSecond");
         Observation.ObservationComponentComponent measurementObs = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("urn:iso:std:iso:11073:10101",String.valueOf(code),"The speed (average speed in this case)")));
         measurementObs.setValue(new Quantity(null, measurement, "http://unitsofmeasure.org", "m/s","meters/second"));       
         theComponentList.add(measurementObs);
      }

      if (document.get("maxSpeedInMetersPerSecond") != null) {
         Long code = Mdc.get32BitCodeFromReferenceId("MDC_HF_SPEED");
         Double measurement = document.getDouble("maxSpeedInMetersPerSecond");
         Observation.ObservationComponentComponent measurementObs = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("urn:iso:std:iso:11073:10101",String.valueOf(code),"The speed (max speed in this case)")));
         measurementObs.setValue(new Quantity(null, measurement, "http://unitsofmeasure.org", "m/s","meters/second"));       
         theComponentList.add(measurementObs);
      }

      if (document.get("totalElevationGainInMeters") != null) {
         Long code = Mdc.get32BitCodeFromReferenceId("MDC_HF_ALT_GAIN");
         Double measurement = document.getDouble("totalElevationGainInMeters");
         Observation.ObservationComponentComponent measurementObs = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("urn:iso:std:iso:11073:10101",String.valueOf(code),"Altitude gain")));
         measurementObs.setValue(new Quantity(null, measurement, "http://unitsofmeasure.org", "m","meters"));       
         theComponentList.add(measurementObs);
      }

      if (document.get("totalElevationLossInMeters") != null) {
         Long code = Mdc.get32BitCodeFromReferenceId("MDC_HF_ALT_LOSS");
         Double measurement = document.getDouble("totalElevationLossInMeters");
         Observation.ObservationComponentComponent measurementObs = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("urn:iso:std:iso:11073:10101",String.valueOf(code),"Altitude loss")));
         measurementObs.setValue(new Quantity(null, measurement, "http://unitsofmeasure.org", "m","meters"));       
         theComponentList.add(measurementObs);
      }

      if (document.get("averagePaceInMinutesPerKilometer") != null) {
         Long code = Mdc.get32BitCodeFromReferenceId("MDC_HF_ALT_LOSS");
         Double measurement = document.getDouble("averagePaceInMinutesPerKilometer");
         Observation.ObservationComponentComponent measurementObs = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("urn:iso:std:iso:11073:10101",String.valueOf(code),"Altitude loss")));
         measurementObs.setValue(new Quantity(null, measurement, "http://unitsofmeasure.org", "m","meters"));       
         theComponentList.add(measurementObs);
      }

   }
   private void setEpochResource(@IdParam IdType theId, QueryDocumentSnapshot document) { 
      Observation myObs = new Observation();
      myObs.setId(theId.getIdPart());
      myObs.setStatus(ObservationStatus.FINAL);
      myObs.setSubject(new Reference("Patient/" + document.getString("user_id")));
      myObs.addCategory(new CodeableConcept(
         new Coding("http://terminology.hl7.org/CodeSystem/observation-category","physical-activity","Physical Activity")
      ));
      myObs.setCode(new CodeableConcept (
         new Coding("https://connect.garmin.com/", "epoch-summary-code","15 min Garmin wellness epochs")
      ));

      Period periodOfActivity = Helper.formatPeriod(document, "activeTimeInSeconds");
      
      if (periodOfActivity != null) {
         myObs.setEffective(periodOfActivity);
      }
      
      List<Observation.ObservationComponentComponent> theComponentList = 
         new ArrayList<Observation.ObservationComponentComponent>(){};


      theComponentList = setCommonActivityComponents(theComponentList, document);

      if (document.get("intensity")!=null) {
         String intensity = document.getString("intensity");
         Coding intensityCoding = null;
         switch(intensity.toLowerCase()) {
            case "sedentary":
            // link code text
               intensityCoding = new Coding("https://connect.garmin.com/", "sedentary-intensity-code", "Wellness Monitoring Intensity is Sedentary");
               break;
            case "active":
               intensityCoding = new Coding("https://connect.garmin.com/", "active-intensity-code", "Wellness Monitoring Intensity is Active");;
               break;
            case "highly_active":
               intensityCoding = new Coding("https://connect.garmin.com/", "highly-active-intensity-code", "Wellness Monitoring Intensity is Highly-Active");
               break;
         }

         if (intensityCoding != null) {
            theComponentList.add(new Observation.ObservationComponentComponent(new CodeableConcept(intensityCoding)));
         }
      }

      if (document.get("maxMotionIntensity") != null) {
         Long maxMotionIntensity = document.getDouble("maxMotionIntensity").longValue();
         Observation.ObservationComponentComponent maxMotionIntensityObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding( "https://connect.garmin.com/","max-motion-intensity-code",
            "The largest motion intensity score of any minute in this monitoring period, where Garmin's motion intensity is a numerical abstraction of low-level accelerometer data")));
         maxMotionIntensityObservation.setValue(new Quantity(maxMotionIntensity));
         
         Observation.ObservationReferenceRangeComponent myRange = new Observation.ObservationReferenceRangeComponent();

         myRange.setHigh(new Quantity(null, 7L, "https://connect.garmin.com/", "ceiling-motion-intensity-code", "unitless"));
         myRange.setLow(new Quantity(null, 0L, "https://connect.garmin.com/", "floor-motion-intensity-code", "unitless"));
         maxMotionIntensityObservation.setReferenceRange(new ArrayList<Observation.ObservationReferenceRangeComponent>(){
            {
               add(myRange);
            }
         });    

         theComponentList.add(maxMotionIntensityObservation);
      }

      if (document.get("meanMotionIntensity") != null) {
         Long meanMotionIntensity = document.getDouble("meanMotionIntensity").longValue();
         Observation.ObservationComponentComponent meanMotionIntensityObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding( "https://connect.garmin.com/","mean-motion-intensity-code",
            "The average of motion intensity scores for all minutes in this monitoring period, where Garmin's motion intensity is a numerical abstraction of low-level accelerometer data")));
         meanMotionIntensityObservation.setValue(new Quantity(meanMotionIntensity));

         Observation.ObservationReferenceRangeComponent myRange = new Observation.ObservationReferenceRangeComponent();

         myRange.setHigh(new Quantity(null, 7L, "https://connect.garmin.com/", "ceiling-motion-intensity-code", "unitless"));
         myRange.setLow(new Quantity(null, 0L, "https://connect.garmin.com/", "floor-motion-intensity-code", "unitless"));
         meanMotionIntensityObservation.setReferenceRange(new ArrayList<Observation.ObservationReferenceRangeComponent>(){
            {
               add(myRange);
            }
         });       
         theComponentList.add(meanMotionIntensityObservation);
      }

      List<Annotation> notes = new ArrayList<Annotation>(){{
         add(new Annotation(new MarkdownType​(
            "Total duration of the monitoring period is 900 seconds. Total active time, ie the portion of the monitoring period (in seconds) in which the device wearer was active for this activity type, is set under effectivePeriod. The sum of active times of all epochs of the same start time (and different activity types) should be equal to the duration.")));
      }};

      if (document.get("met") != null) {
         Long met = document.getDouble("met").longValue();
         Observation.ObservationComponentComponent metObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("http://loinc.org","82264-3","MET by measured RMR panel")));
           
         metObservation.setValue(new Quantity(null, met, "http://unitsofmeasure.org", "kcal/min","kcal/min"));       
         theComponentList.add(metObservation);
         notes.add(new Annotation(new MarkdownType​("See https://www.cdc.gov/nccdphp/dnpa/physical/pdf/PA_Intensity_table_2_1.pdf for more information on MET")));
      }

      myObs.setNote(notes);
      myObs.setComponent(theComponentList);
      myObservations.put(theId.getIdPart(), myObs);
      
   }

   private List<Observation.ObservationComponentComponent> setCommonActivityComponents(List<Observation.ObservationComponentComponent> theComponentList, QueryDocumentSnapshot document) {
      if (document.get("activityType") != null) {
         ActivityType a = new ActivityType(document.getString("activityType"));
         CodeableConcept myCoding = a.getActivityCoding();
         if (myCoding != null) {
            theComponentList.add(new Observation.ObservationComponentComponent(myCoding));
         }
      }

      if (document.get("activeKilocalories") != null) {
         Double activeCalories = document.getDouble("activeKilocalories");
         Observation.ObservationComponentComponent distanceObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("http://loinc.org","93819-1","Calories burned in unspecified time --during activity")));
           
         distanceObservation.setValue(new Quantity(null, activeCalories, "http://unitsofmeasure.org", "kcal","Kilocalories"));       
         theComponentList.add(distanceObservation);
      }

      // careful: using .longValue() will truncate decimal places
      if (document.get("distanceInMeters") != null) {
         Double numSteps = document.getDouble("distanceInMeters");
         Observation.ObservationComponentComponent distanceObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("http://loinc.org","55430-3","Walking distance unspecified time Pedometer")));
           
         distanceObservation.setValue(new Quantity(null, numSteps, "http://unitsofmeasure.org", "m","meters"));       
         theComponentList.add(distanceObservation);
      }

      Object stepsObj = document.get("steps");
      if (stepsObj != null) {
         Long numSteps = Long.parseLong(String.valueOf(stepsObj));
         
         Observation.ObservationComponentComponent stepsObservation = new Observation.ObservationComponentComponent(
            new CodeableConcept(new Coding("http://loinc.org","55423-8","Number of steps in unspecified time Pedometer")));
           
         stepsObservation.setValue(new Quantity(numSteps));
         
         theComponentList.add(stepsObservation);
      }


      return theComponentList;

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


}
