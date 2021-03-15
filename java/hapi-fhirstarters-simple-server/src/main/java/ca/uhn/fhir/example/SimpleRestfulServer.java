package ca.uhn.fhir.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.io.*;


@WebServlet("/*")
public class SimpleRestfulServer extends RestfulServer {

	@Override
	protected void initialize() throws ServletException {
		// Create a context for the appropriate version
		setFhirContext(FhirContext.forR4());


		try {	
			// Initialize Firebase
			// Use a service account
			String credentials_path = "/Users/jeanniehuang/Developer/garmin/FHIR-Sandbox/fhir-sandbox-f2a24-6a2139c74d93.json";
			InputStream serviceAccount = new FileInputStream(credentials_path);
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(credentials)
				.build();
			FirebaseApp.initializeApp(options);
			
			Firestore db = FirestoreClient.getFirestore();

			// Register resource providers
			
			// Calls the constructor on the resource provider class? 
			registerProvider(new PatientResourceProvider(db));
			registerProvider(new ObservationResourceProvider(db));
			
			// Format the responses in nice HTML
			registerInterceptor(new ResponseHighlighterInterceptor());
	}

	catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
