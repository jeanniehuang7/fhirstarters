package ca.uhn.fhir.example;
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
                activityCoding = new Coding("https://connect.garmin.com/", "walking-code","Walking activity");
                break;
            case "running":
                this.activity=ActivityEnum.RUNNING;
                activityCoding = new Coding("https://connect.garmin.com/", "running-code","Running activity");
                break;
            case "cycling":
                this.activity=ActivityEnum.CYCLING;
                activityCoding = new Coding("https://connect.garmin.com/", "cycling-code","Cycling activity");
                break;
            case "sedentary":
                this.activity=ActivityEnum.SEDENTARY;
                activityCoding = new Coding("https://connect.garmin.com/", "sedentary-code","Sedentary activity");
                break;
            case "fitness_equipment":
                this.activity=ActivityEnum.FITNESS_EQUIPMENT;
                activityCoding = new Coding("https://connect.garmin.com/", "fitness-equipment-code","Fitness Equipment activity");
                break;
            case "swimming":
                this.activity=ActivityEnum.SWIMMING;
                activityCoding = new Coding("https://connect.garmin.com/", "swimming-code","Swimming activity");
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