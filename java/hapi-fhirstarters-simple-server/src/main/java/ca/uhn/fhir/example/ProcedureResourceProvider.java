package ca.uhn.fhir.example;
import ca.uhn.fhir.example.Helper;
import ca.uhn.fhir.example.ActivityType;
import ca.uhn.fhir.example.ActivityEnum;

// fhir
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Procedure.ProcedureStatus;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.MarkdownType;

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
import java.util.ArrayList;


public class ProcedureResourceProvider implements IResourceProvider {

    private Map<String, Procedure> myProcedures = new HashMap<String, Procedure>();
    private Firestore db;

    /**
     * Constructor
     I will go and fetch data and format as FHIR resource
     at the time we want to read it. 
     */
 
    public ProcedureResourceProvider(Firestore _db) {
       db = _db;
    }
 
    @Override
    public Class<? extends IBaseResource> getResourceType() {
       return Procedure.class;
    }
 
    /**
     * Simple implementation of the "read" method 
     * Get data from Firebase by calling http://localhost:8080/Procedure/g* where * is a number
    */
    @Read()
    public Procedure read(@IdParam IdType theId) {
        QueryDocumentSnapshot document = Helper.searchForDocument(db, theId);
        
        setResourceWrapper(document, theId);
 
        Procedure retVal = myProcedures.get(theId.getIdPart());
        
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
        
        switch (collName) {
            case "g_moveIq": 
                setMoveIqResource(document, theId);
                break;
        }
    }


    // I looked on loinc.org to try to find corresponding walking, fitness equipment, etc 
    // activity codes. LOINC codes are generated typically from health surveys and don't 
    // match a one-time fitness activity well (example: LOINC 77591-6 describes a person's 
    // total walking physical activity as a duration and frequency of days over the week). 
    // My best effort solution is to make a new code with Garmin. 
    // there are also a lot of other code systems at http://hl7.org/fhir/R4/terminologies-systems.html. 
    private void setMoveIqResource(QueryDocumentSnapshot document, @IdParam IdType theId) {
        Procedure myProcedure = new Procedure();
        myProcedure.setSubject(new Reference("Patient/" + document.getString("user_id")));
        myProcedure.setId(theId.getIdPart());
        myProcedure.setStatus(Procedure.ProcedureStatus.COMPLETED);
        
        ActivityType a = new ActivityType(document.getString("activityType"));
        myProcedure.setCode(a.getActivityCoding());
        Period periodOfActivity = new Period();
        if (document.get("activitySubType") != null) {
            List<Annotation> myNote=new ArrayList<Annotation>(){{
                add(new Annotation(new MarkdownType​("Activity subtype: " + document.getString("activitySubType"))));
            }};
            myProcedure.setNote(myNote);
        }
        
        
        Object startTime = document.get("startTimeInSeconds");
        Object localOffset = document.get("offsetInSeconds");
        Object duration = document.get("durationInSeconds");

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

        periodOfActivity.setStartElement(new DateTimeType(Helper.formatDate(start)));
        periodOfActivity.setEndElement(new DateTimeType(Helper.formatDate(end)));

        myProcedure.setPerformed(periodOfActivity);
        myProcedures.put(theId.getIdPart(), myProcedure);

    }
}
 
 