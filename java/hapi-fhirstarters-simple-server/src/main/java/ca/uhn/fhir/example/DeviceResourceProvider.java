package ca.uhn.fhir.example;
import ca.uhn.fhir.example.Helper;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.instance.model.api.IBaseResource;

import org.hl7.fhir.r4.model.Device.DeviceDeviceNameComponent;
import org.hl7.fhir.r4.model.Device.DeviceNameType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.instance.model.api.IBaseEnumFactory;
import org.hl7.fhir.r4.model.Device.DeviceNameTypeEnumFactory;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.io.*;
import java.time.LocalDate;

public class DeviceResourceProvider implements IResourceProvider {

    private Map<String, Device> myDevices = new HashMap<String, Device>();
    private Firestore db;
 
    public DeviceResourceProvider(Firestore _db) {
       db = _db;
    }
 
    @Override
    public Class<? extends IBaseResource> getResourceType() {
       return Device.class;
    }
 
    /**
     * Simple implementation of the "read" method 
    */
    @Read()
    public Device read(@IdParam IdType theId) {
       setDevices();
       Device retVal = myDevices.get(theId.getIdPart());
       
       if (retVal == null) {
          throw new ResourceNotFoundException(theId);
       }
       return retVal;
    }

    private void setDevices() {
        Device theDevice = new Device();
        theDevice.setManufacturer("Garmin");
        Enumeration<Device.DeviceNameType> myEnum = new Enumeration<Device.DeviceNameType>(new Device.DeviceNameTypeEnumFactory());
        myEnum.setValue(Device.DeviceNameType.MODELNAME); 

        String name = "vivoactive4WifiSmall";
        theDevice.setDeviceName(new ArrayList<Device.DeviceDeviceNameComponent>(){{
            add(new Device.DeviceDeviceNameComponent(new StringType(name), myEnum));
        }}); 
        myDevices.put("1", theDevice);
    }
}