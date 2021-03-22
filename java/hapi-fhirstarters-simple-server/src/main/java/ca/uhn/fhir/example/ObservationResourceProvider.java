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
import java.time.LocalDate;

//added to try to do resp rate extension 
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
import org.hl7.fhir.r4.model.Observation.ObservationComponentComponent;

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
      Helper.retrieveUsers(db);
      setDummyRespWithExtension();  
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
      searchForDocument(db, theId, "g_respiration");
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
      myResp.setEffective(new DateTimeType("1996-02-02"));
      myResp.setValue(new Quantity(null,26,"http://unitsofmeasure.org","/min","breaths/min"));
      myResp.setCode(new CodeableConcept(new Coding("http://loinc.org","9279-1","Respiratory rate")));
    
      myObservations.put("1", myResp);
   }


   private void setRespRateResource(@IdParam IdType theId, List<QueryDocumentSnapshot> documents) {
      QueryDocumentSnapshot document = documents.get(0);
      System.out.println("Document data: " + document.getData());
      String userId = document.getString("user_id");
      String startTime = document.get("startTimeInSeconds").toString();
      String localOffset = document.get("startTimeOffsetInSeconds").toString();
      
      HashMap <String, Double> measurements = (HashMap)document.get("timeOffsetEpochToBreaths");
      
      // existing issue: measurements isn't guaranteed to be in order, so this fetches a random entry
      Map.Entry<String,Double> entry = measurements.entrySet().iterator().next();
      String firstKey = entry.getKey();
      Double firstValue = entry.getValue();
      System.out.println("Time offset: " + firstKey);
      System.out.println("Resp value: " + firstValue);

      Long unixTimeStamp = Long.parseLong(startTime) + Long.parseLong(localOffset) + Long.parseLong(firstKey);
     
      RespRate myResp = new RespRate();
      Reference patientReference = new Reference("Patient/" + userId);
      myResp.setSubject(patientReference);
      myResp.setId(theId.getIdPart());
      Coding myCoding = new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs");
      CodeableConcept myCodeConcept = new CodeableConcept(myCoding);
      myResp.addCategory(myCodeConcept);
      myResp.setEffective(new DateTimeType(Helper.formatDate(unixTimeStamp)));

      ObservationComponentComponent obs1 = new ObservationComponentComponent​(new CodeableConcept(new Coding("http://loinc.org","9279-1","Respiratory rate")));
      obs1.setValue(new Quantity(null,firstValue,"http://unitsofmeasure.org","/min","breaths/min"));

      // although we can have a list of component observations, unforunately they all correspond to the same time.
      // so we need an alternate method for recording time series data.
      List <ObservationComponentComponent> myList = new ArrayList<ObservationComponentComponent>();
      myList.add(obs1);
      myResp.setComponent(myList);

      myObservations.put(theId.getIdPart(), myResp);
   }

   //tbd: search firebase for all documents in all collections 
   private void searchForDocument(Firestore db, @IdParam IdType theId, String collName) {
      System.out.printf("Searching for Observation with id %s \n", theId.getIdPart());
      try {
         ApiFuture<QuerySnapshot> future = db.collection(collName).whereEqualTo("summaryId", theId.getIdPart()).get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();
         System.out.println("Number of matching documents: " + documents.size());
         //System.out.println(documents.size());
         
         if (documents.size() < 1) {
            return;
         }
         setRespRateResource(theId, documents);
		}

      catch (Exception e) {
         e.printStackTrace();
      }      
      return;
   }


}
