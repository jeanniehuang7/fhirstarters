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
import java.util.Arrays;

// added to try to customize the capability statement
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
//import ca.uhn.fhir.rest.server.interceptor.StaticCapabilityStatementInterceptor;
//import ca.uhn.fhir.rest.server.interceptor.*;
import ca.uhn.fhir.validation.ResultSeverityEnum;
//import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Enumerations;
//import org.springframework.web.cors.CorsConfiguration;


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
			
			registerProvider(new PatientResourceProvider(db));
			registerProvider(new ObservationResourceProvider(db));
			registerProvider(new ProcedureResourceProvider(db));
			
			// Format the responses in nice HTML
			// Provides the autmomatically generated metadata/capability statement as well?
			registerInterceptor(new ResponseHighlighterInterceptor());


			// Use a statically generated capability statement 

			// Create the interceptor
			// StaticCapabilityStatementInterceptor interceptor = new StaticCapabilityStatementInterceptor();

			// // There are two ways of supplying a CapabilityStatement to the
			// // interceptor. You can use a static resource found on the classpath
			// interceptor.setCapabilityStatementResource("./capabilitystatement.json");

			// // ..or you can simply create one in code (in which case you do not
			// // need to call setCapabilityStatementResource(..))
			// // CapabilityStatement cs = new CapabilityStatement();
			// // cs.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
			// // cs.getSoftware().setName("My Acme Server");

			// // Now register the interceptor
			// registerInterceptor(interceptor);
	}

	catch (IOException e) {
			e.printStackTrace();
		}
		
	}



}
