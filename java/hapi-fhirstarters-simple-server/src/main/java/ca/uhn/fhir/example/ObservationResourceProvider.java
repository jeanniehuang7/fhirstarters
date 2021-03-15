package ca.uhn.fhir.example;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.instance.model.api.IBaseResource;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.io.*;
import java.time.LocalDate;

public class ObservationResourceProvider implements IResourceProvider {

   private Map<String, Observation> myObservations = new HashMap<String, Observation>();
   private Firestore db;

   /**
    * Constructor
    I don't think I want to read all of the Firebase data in at the time of
    class construction. So I will go and fetch data and format as FHIR resource
    at the time we want to read it. 
    */

   public ObservationResourceProvider(Firestore _db) {
      db = _db;
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

      Observation retVal = myObservations.get(theId.getIdPart());
      
      if (retVal == null) {
         throw new ResourceNotFoundException(theId);
      }
      return retVal;
   }

   private void searchForObservation(Firestore db, @IdParam IdType theId) {
      try {
         ApiFuture<QuerySnapshot> future = db.collection("g_userMetric").whereEqualTo("user_id", theId.getIdPart()).get();
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
