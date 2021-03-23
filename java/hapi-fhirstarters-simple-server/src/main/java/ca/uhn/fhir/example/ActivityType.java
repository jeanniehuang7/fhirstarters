package ca.uhn.fhir.example;
import ca.uhn.fhir.example.ActivityEnum;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;


public class ActivityType {
    ActivityEnum activity;
    public ActivityType(String attrName) {
        switch(attrName) {
            case "walking":
                this.activity=ActivityEnum.WALKING;
                break;
            case "running":
                this.activity=ActivityEnum.RUNNING;
                break;
            case "cycling":
                this.activity=ActivityEnum.CYCLING;
                break;
            case "sedentary":
                this.activity=ActivityEnum.SEDENTARY;
                break;
            case "fitness_equipment":
                this.activity=ActivityEnum.FITNESS_EQUIPMENT;
                break;
            case "swimming":
                this.activity=ActivityEnum.SWIMMING;
                break;
        }  
    }

    public CodeableConcept getActivityCoding() {
        switch(activity) {
            case WALKING:
                return new CodeableConcept(new Coding("https://connect.garmin.com/", "walking-code","Walking activity"));
            case RUNNING:
                return new CodeableConcept(new Coding("https://connect.garmin.com/", "running-code","Running activity"));
            case CYCLING:
                return new CodeableConcept(new Coding("https://connect.garmin.com/", "cycling-code","Cycling activity"));
            case SEDENTARY:
                return new CodeableConcept(new Coding("https://connect.garmin.com/", "sedentary-code","Sedentary activity"));
            case FITNESS_EQUIPMENT: 
                return new CodeableConcept(new Coding("https://connect.garmin.com/", "fitness-equipment-code","Fitness Equipment activity"));
            case SWIMMING:
                return new CodeableConcept(new Coding("https://connect.garmin.com/", "swimming-code","Swimming activity"));
        }
        return null;
    }
}