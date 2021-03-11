package ca.uhn.fhir.example;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
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

public class Example01_PatientResourceProvider implements IResourceProvider {

   private Map<String, Patient> myPatients = new HashMap<String, Patient>();
   private Firestore db;

   /**
    * Constructor
    I don't think I want to read all of the Firebase data in at the time of
    class construction. So I will go and fetch data and format as FHIR resource
    at the time we want to read it. 
    */

   public Example01_PatientResourceProvider(Firestore _db) {
      db = _db;
      retrieveUsers(db);
   }

   @Override
   public Class<? extends IBaseResource> getResourceType() {
      return Patient.class;
   }

   /**
    * Simple implementation of the "read" method
    */

   @Read()
   public Patient read(@IdParam IdType theId) {
      // Replace below with getting data from Firebase 
      // and then adding all the appropriate information

      // for testing/development purposes
      // retrieve dummy user by asking for http://localhost:8080/Patient/1 
      setDummyUser(db, theId);
      searchForPatient(db, theId); 

      Patient retVal = myPatients.get(theId.getIdPart());
      
      if (retVal == null) {
         throw new ResourceNotFoundException(theId);
      }
      return retVal;
   }



   // the passed in theId must be server ID of 1 
   private void setDummyUser(Firestore db, @IdParam IdType theId) {
      Patient pat1 = new Patient();
      pat1.setId("1"); // i think this is a logical identifier for the server?
      pat1.addIdentifier().setSystem("https://warriorwellness.me").setValue("some dummy business ID like g_999");
      pat1.addName().setFamily("Bruin").addGiven("Jane").addGiven("C");
      myPatients.put("1", pat1);
   }

   // To do: implement a "search for document" given id
   // for now, assume that the server id matches the business id? 

   private void searchForPatient(Firestore db, @IdParam IdType theId) {
      System.out.printf("Searching for patient with id %s \n", theId.getIdPart());
      
      try {
         ApiFuture<QuerySnapshot> future = db.collection("g_userMetric").whereEqualTo("user_id", theId.getIdPart()).get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();
         System.out.println("Document size: ");
         System.out.println(documents.size());
         

         if (documents.size() < 1) {
            return;
         } 
         
         // Select the most recent user metric for this user
         LocalDate mostRecentDate = LocalDate.of(1900, 01, 01);
         QueryDocumentSnapshot mostRecentDoc = null;

         for (int i=0; i < documents.size(); i++) {
            QueryDocumentSnapshot document = documents.get(i);

            if (i==0) {
               mostRecentDoc = document;
            }

            System.out.println("User: " + document.getId());
            System.out.println("Summary ID: " + document.getString("summaryId"));
            System.out.println("User ID: " + document.getString("user_id"));
            System.out.println("Calendar date: " + document.getString("calendarDate"));
            
            String [] dateParts = document.getString("calendarDate").split("-");
            
            if (dateParts.length != 3) {
               System.out.println("Date format in Firebase is not the expected format of YYYY-MM-DD");
               ArrayIndexOutOfBoundsException incorrectDate = new ArrayIndexOutOfBoundsException();
               throw incorrectDate;
            }
            
            LocalDate curDate = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));

            if (curDate.isAfter(mostRecentDate)) {
               mostRecentDate = curDate;
               mostRecentDoc = document;
            }
         }

         System.out.println("Most recent doc is " + mostRecentDoc.getString("calendarDate"));
         setPatientResource(mostRecentDoc, theId);
		}
      catch (Exception e) {
         e.printStackTrace();
      }      
      return;
   }

   private void setPatientResource(QueryDocumentSnapshot doc, @IdParam IdType theId) {
      Patient pat1 = new Patient();
      pat1.setId(theId.getIdPart()); // i think this is a logical identifier for the server?
      pat1.addIdentifier().setSystem("https://warriorwellness.me").setValue(theId.getIdPart());
      //pat1.addName().setFamily("Bruin").addGiven("Jane").addGiven("C");
      myPatients.put(theId.getIdPart(), pat1);
   }

   // test that you've connected to Firebase by retrieving all user metric documents in Garmin. 
   private void retrieveUsers(Firestore db)
	{
		// asynchronously retrieve all users
		ApiFuture<QuerySnapshot> query = db.collection("g_userMetric").get();
		// ...
		// query.get() blocks on response
		try {
			QuerySnapshot querySnapshot = query.get();
				List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
				for (QueryDocumentSnapshot document : documents) {
					System.out.println("User: " + document.getId());
					System.out.println("Summary ID: " + document.getString("summaryId"));
					System.out.println("User ID: " + document.getString("user_id"));
				}	
		}
		catch (Exception e){
			e.printStackTrace();

		}
	    
	}


}
