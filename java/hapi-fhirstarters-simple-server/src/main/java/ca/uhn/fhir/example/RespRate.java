package ca.uhn.fhir.example;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StringType;

import java.util.ArrayList;
import java.util.List;

/**
 * Definition class for adding extensions to the built-in
 * Observation resource type.
 * 
 * Note the "profile" attribute below, which indicates the URL/ID of the
 * profile implemented by this resource. You are not required to supply this,
 * but if you do it will be automatically populated in the resource meta
 * tag if the resource is returned by a server.
 */
@ResourceDef(name="Observation", profile="http://hl7.org/fhir/StructureDefinition/resprate")
public class RespRate extends Observation {

   private static final long serialVersionUID = 1L;

   /**
    * Each extension is defined in a field. Any valid HAPI Data Type
    * can be used for the field type. Note that the [name=""] attribute
    * in the @Child annotation needs to match the name for the bean accessor
    * and mutator methods.
    */
   @Child(name="petName")   
   @Extension(url="http://example.com/dontuse#petname", definedLocally=false, isModifier=false)
   @Description(shortDefinition="The name of the Observation's favourite pet")
   private StringType myPetName;
   //private valueQuantity myRespRate;

   /**
    * The second example extension uses a List type to provide
    * repeatable values. Note that a [max=] value has been placed in
    * the @Child annotation.
    * 
    * Note also that this extension is a modifier extension
    */
   @Child(name="RespMeasurements", max=Child.MAX_UNLIMITED)   
   @Extension(url="http://example.com/dontuse#RespMeasurements", definedLocally=false, isModifier=true)
   @Description(shortDefinition="Some dates of note for this Observation")
   private List<DateTimeType> myRespMeasurements;

   /**
    * It is important to override the isEmpty() method, adding a check for any
    * newly added fields. 
    */
   @Override
   public boolean isEmpty() {
      return super.isEmpty() && ElementUtil.isEmpty(myPetName, myRespMeasurements);
   }
   
   /********
    * Accessors and mutators follow
    * 
    * IMPORTANT:
    * Each extension is required to have an getter/accessor and a setter/mutator.
    * You are highly recommended to create getters which create instances if they
    * do not already exist, since this is how the rest of the HAPI FHIR API works. 
    ********/
   
   /** Getter for important dates */
   public List<DateTimeType> getRespMeasurements() {
      if (myRespMeasurements==null) {
         myRespMeasurements = new ArrayList<DateTimeType>();
      }
      return myRespMeasurements;
   }

   /** Getter for pet name */
   public StringType getPetName() {
      if (myPetName == null) {
         myPetName = new StringType();
      }
      return myPetName;
   }

   /** Setter for important dates */
   public void setRespMeasurements(List<DateTimeType> theRespMeasurements) {
      myRespMeasurements = theRespMeasurements;
   }

   /** Setter for pet name */
   public void setPetName(StringType thePetName) {
      myPetName = thePetName;
   }

}
