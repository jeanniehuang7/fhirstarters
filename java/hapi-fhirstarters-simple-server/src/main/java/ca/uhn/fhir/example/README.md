# Synopsis
 In this project, we are aiming to make the Warrior Wellness app (SMART-on) FHIR compatible to share smartwatch sensor data with EHRs and other authorized parties. Providing a holistic view of a patient by integrating information from multiple SMART FHIR compatible apps would create a huge benefit in improving patient care.

# Running the HAPI Simple Server
Visit <a href="https://firebase.google.com/docs/firestore/quickstart">Firestore Quickstart </a> for instructions on how to generate a private key and save the serviceAccount.json for your Firebase project. Replace ```String credentials_path``` in SimpleRestfulServer.java with your own ```path/to/serviceAccount.json``` to initialize the SDK.

If you have added new dependencies, or if it is your first time running the code, run ```mvn compile``` before Jetty. Then run ```mvn jetty:run```. 

You can also visit the original instructions @ fhirstarters/java/hapi-fhirstarters-simple-server on how to compile and run this code using Maven.

# Description of files

## Server files

### SimpleRestfulServer.java
Registers all the different resource providers and connects to the Firebase database. Every new resource provider must be registered here. Passes the database as a constructor to each resource provider. 

### Example_AuthorizationInterceptor.java
Handles security. Original file provided by the fhirstarters code that I have not changed.


## Resource Providers
### PatientResourceProvider.java
Retrieves g_userMetric. 

### ObservationResourceProvider.java
Retrieves g_respiration, g_daily, g_activity, g_epoch, g_pulseOx, g_stress, g_activityDetails, g_sleep. The majority of the implementation is in this file. 

### DeviceResourceProvider.java
Retrieves one type of device, the Garmin Vivoactive4WifiSmall. It is referenced to in the activity resource.  

### ProcedureResourceProvider.java
Retrieves g_moveIq. Taking a liberal interpretation of procedure here, in that a recorded activity could be considered a procedure. This also could have been considered an Observation instead. 

### RespRate.java
A dummy file that can be used for extensions in the future. I am not using it in this implementation, but one can see the format for making extensions here. 

## Activity files

### ActivityType.java
A convenient method of returning codings for different activity types. Uses a mix of coding systems. Used by the resource providers. 

### ActivityEnum.java
All the possible activity types in Garmin. 

## Other files
### Helper.java
A file for common helper functions used by the resource providers, like searching for a document in Firebase. 

### Mdc.java
Code provided by Brian Reinhold on Zulip. Used by ObservationResourceProvider.java to fetch 32-bit codes for certain activities according to the MDC code system. Follows the <a href="https://www.iso.org/standard/77338.html">ISO/IEEE 11073-10101 standard</a>. 
### Hints.java
Original file provided by the fhirstarters code that I have not changed.




# Information and resources
+ <a href="https://hapifhir.io/hapi-fhir/docs/server_plain/server_types.html">HAPI Plain FHIR Server</a> - I recommend reading through "Working with the FHIR Model" and "Plain Server"
+ <a href="https://hapifhir.io/hapi-fhir/docs/appendix/javadocs.html">HAPI FHIR API Docs</a> - For debugging and coding with HAPI FHIR modules.
+ <a href="http://hl7.org/fhir/modules.html">HL7 Specification</a> - Official documentation of HL7
+ <a href="https://chat.fhir.org/">Zulip</a> - Official FHIR community with FHIR experts, very responsive to questions
+ <a href="https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/Observation.html">HAPI Observation docs</a> - One of the pages I visited most often while coding
+ <a href="http://hl7.org/fhir/R4/observation.html">FHIR Observation docs </a> - One of the pages I visited most often while coding
