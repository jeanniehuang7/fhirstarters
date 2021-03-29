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

// fhir
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.DateTimeType;

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
import java.util.Collections;  
import java.util.Comparator;    
import java.util.LinkedHashMap;  
import java.util.LinkedList;  
import java.util.List;  
import java.util.Map.Entry;  


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

   public static HashMap<String, Long> sortMapByKeyStringToLong(Map<String, Long> myMap)   {  
    //convert HashMap into List   
    List<Entry<String, Long>> list = new LinkedList<Entry<String, Long>>(myMap.entrySet());  
    //sorting the list elements  
    Collections.sort(list, new Comparator<Entry<String, Long>>()   {  
       public int compare(Entry<String, Long> o1, Entry<String, Long> o2)   {  
          //sort ascending, compare two object and return value
          Long val1 = Long.parseLong(o1.getKey());
          Long val2 = Long.parseLong(o2.getKey());
          if (val2 > val1) {
             return -1;
          } else if (val1 > val2){
             return 1;
          }  else {
             return 0;
          }
       }  
    });
    HashMap<String, Long> sortedMap = new LinkedHashMap<String, Long>();  
    for (Entry<String, Long> entry : list)   {  
       sortedMap.put(entry.getKey(), entry.getValue());  
    }
    return sortedMap;  
    }

    public static HashMap<String, Double> sortMapByKeyStringToDouble(Map<String, Double> myMap)   {  
        //convert HashMap into List   
        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(myMap.entrySet());  
        //sorting the list elements  
        Collections.sort(list, new Comparator<Entry<String, Double>>()   {  
        public int compare(Entry<String, Double> o1, Entry<String, Double> o2)   {  
            //sort ascending, compare two object and return value
            Long val1 = Long.parseLong(o1.getKey());
            Long val2 = Long.parseLong(o2.getKey());
            if (val2 > val1) {
                return -1;
            } else if (val1 > val2){
                return 1;
            }  else {
                return 0;
            }
        }  
        });
        HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();  
        for (Entry<String, Double> entry : list)   {  
        sortedMap.put(entry.getKey(), entry.getValue());  
        }
        return sortedMap;  
    }

    public static QueryDocumentSnapshot searchForDocument(Firestore db, @IdParam IdType theId) {
        String[] theIdParts = theId.getIdPart().split(":");
      
        if (theIdParts.length != 2) {
            System.out.println("To return a non-dummy resource the input ID should be the form collectionName:summaryId");
            return null;
        }
  
        String collName = theIdParts[0];
        String theSummaryId = theIdParts[1];
        
        System.out.print("===========================================\nSearching for Resource with id " + theSummaryId);
        System.out.print(" in collection " + collName + "\n");
        
        try {
            ApiFuture<QuerySnapshot> future = db.collection(collName).whereEqualTo("summaryId", theSummaryId).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            System.out.println("Number of matching documents: " + documents.size());
            
            if (documents.size() >= 1) {
                QueryDocumentSnapshot document = documents.get(0);
                System.out.println("Document data: " + document.getData());
                //System.out.println("Document data: \n" + new JSONObjectdocument.getData() 
                return document;
            }
            
            System.out.println("No matching documents found");
        }

        catch (Exception e) {
            e.printStackTrace();
        }     

        return null;
    }

    public static Period formatPeriod(QueryDocumentSnapshot document, String durationField) {
        Period periodOfActivity = new Period();
        
        Object startTime = document.get("startTimeInSeconds");
        Object localOffset = document.get("offsetInSeconds");
        Object duration = document.get(durationField);

        Long startTimeLong = 0L;
        Long localOffsetLong = 0L;
        Long durationLong = 0L;

        if (startTime != null) {
            startTimeLong = Long.parseLong(String.valueOf(startTime));
        }
        if (localOffset != null) {
            localOffsetLong = Long.parseLong(String.valueOf(localOffset));
        }
        if (duration != null) {
            durationLong = Long.parseLong(String.valueOf(duration));
        }

        Long start = startTimeLong + localOffsetLong;
        Long end = start + durationLong;

        periodOfActivity.setStartElement(new DateTimeType(formatDate(start)));
        periodOfActivity.setEndElement(new DateTimeType(formatDate(end)));

        return periodOfActivity;
    }
}