package ca.uhn.fhir.example;

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

import java.util.List;


// A class of helper methods to be used by resource providers 
public class Helper {
    // test that you've connected to Firebase by retrieving all user metric documents in Garmin. 
   public static void retrieveUsers(Firestore db)
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