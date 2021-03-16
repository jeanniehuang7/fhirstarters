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
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.instance.model.api.IBaseResource;

//added to try to do resp rate extension 
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.context.FhirContext;

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

// access java common operations
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.io.*;
import java.time.LocalDate;

public class ObservationResourceProvider implements IResourceProvider {

   private Map<String, Observation> myObservations = new HashMap<String, Observation>();
   private Firestore db;

   public ObservationResourceProvider(Firestore _db) {
      db = _db;
      Helper.retrieveUsers(db);

      RespRate patient = new RespRate();
      patient.setPetName(new StringType("Fido"));
      patient.getImportantDates().add(new DateTimeType("2010-01-02"));
      patient.getImportantDates().add(new DateTimeType("2014-01-26T11:11:11"));

      myObservations.put("1", patient);

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
      searchForObservation(db, theId);

      //Observation retVal = myObservations.get(theId.getIdPart());
      Observation retVal = myObservations.get("1");
      
      if (retVal == null) {
         throw new ResourceNotFoundException(theId);
      }
      return retVal;
   }

   // to do: 
   // format resp rate.java according to the structure definition
   // try with hapi.fhi.org to see what ID to call to return a respiratory rate json
   // attempt bundling

   private void searchForObservation(Firestore db, @IdParam IdType theId) {
      System.out.printf("Searching for observation with id %s \n", theId.getIdPart());
      try {
         ApiFuture<QuerySnapshot> future = db.collection("g_respiration").whereEqualTo("summaryId", theId.getIdPart()).get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();
         System.out.println("Document size: ");
         System.out.println(documents.size());
         
         if (documents.size() < 1) {
            return;
         }
         setObservationResource(theId);
		}

      catch (Exception e) {
         e.printStackTrace();
      }      
      return;
   }

   private void setObservationResource(@IdParam IdType theId) {
      
   }
}
