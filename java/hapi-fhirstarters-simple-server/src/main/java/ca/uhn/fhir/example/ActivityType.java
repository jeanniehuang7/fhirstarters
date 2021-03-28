package ca.uhn.fhir.example;
import ca.uhn.fhir.example.Mdc;
import ca.uhn.fhir.example.ActivityEnum;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;



public class ActivityType {
    ActivityEnum activity;

    Coding activityCoding = null; 
    
    public ActivityType(String attrName) {
        switch(attrName.toLowerCase()) {
            case "walking":
                this.activity=ActivityEnum.WALKING;
                activityCoding = new Coding("urn:iso:std:iso:11073:10101", String.valueOf(Mdc.get32BitCodeFromReferenceId("MDC_HF_ACT_WALK")),"Walking activity");
                break;
            case "running":
                this.activity=ActivityEnum.RUNNING;
                activityCoding = new Coding("https://connect.garmin.com/", "running-activity-code","Running activity");
                break;
            case "cycling":
                this.activity=ActivityEnum.CYCLING;
                activityCoding = new Coding("https://connect.garmin.com/", "cycling-activity-code","Cycling activity");
                break;
            case "sedentary":
                this.activity=ActivityEnum.SEDENTARY;
                activityCoding = new Coding("https://connect.garmin.com/", "sedentary-activity-code","Sedentary activity");
                break;
            case "fitness_equipment":
                this.activity=ActivityEnum.FITNESS_EQUIPMENT;
                activityCoding = new Coding("https://connect.garmin.com/", "fitness-equipment-activity-code","Fitness Equipment activity");
                break;
            case "swimming":
                this.activity=ActivityEnum.SWIMMING;
                activityCoding = new Coding("https://connect.garmin.com/", "swimming-activity-code","Swimming activity");
                break;
        }  
    }

    public CodeableConcept getActivityCoding() {
        if (activityCoding != null) {
            return new CodeableConcept(activityCoding);
        }
        return null;
    }
}