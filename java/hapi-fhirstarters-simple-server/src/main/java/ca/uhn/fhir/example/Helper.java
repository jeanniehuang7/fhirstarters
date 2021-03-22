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
                //    System.out.println("User: " + document.getId());
                //    System.out.println("Summary ID: " + document.getString("summaryId"));
                //    System.out.println("User ID: " + document.getString("user_id"));
               }	
       }
       catch (Exception e){
           e.printStackTrace();

       }
       
   }

    // convert unix time to 
    // An instant in time in the format YYYY-MM-DDThh:mm:ss.sss+zz:zz 
    // (e.g. 2015-02-07T13:28:17.239+02:00 or 2017-01-01T00:00:00Z).

   public static String formatDate(Long unixTimeStamp) {
    // convert seconds to milliseconds
    Date date = new java.util.Date(unixTimeStamp*1000L); 
    // the format of your date
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); // iso 8601
    // give a timezone reference for formatting 
    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-7")); 
    String formattedDate = sdf.format(date);
    System.out.println("Effective dateTime: " + formattedDate);
    return formattedDate;

   }
}