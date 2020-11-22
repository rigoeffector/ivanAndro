package com.example.rigoeefector.celestinfinal.Models;

/**
 * Created by rigoeefector on 7/12/20.
 */

public class Emergency {
    private String d_Id;
    private String location_detection;
    private String location_coordinate;
    private String speed_movement;
    private String accelerometorXasis;
    private String accelerometerYasix;
    private String accelerometerZasix;
    private String emergency_name;
    private String emergency_location;
    private String emergency_phone;
    private String parent_phone;

    public Emergency(String d_Id, String location_detection, String location_coordinate,
                     String speed_movement, String accelerometorXasis,
                     String accelerometerYasix, String accelerometerZasix,
                     String emergency_name, String emergency_location,
                     String emergency_phone, String parent_phone) {
        this.d_Id = d_Id;
        this.location_detection = location_detection;
        this.location_coordinate = location_coordinate;
        this.speed_movement = speed_movement;
        this.accelerometorXasis = accelerometorXasis;
        this.accelerometerYasix = accelerometerYasix;
        this.accelerometerZasix = accelerometerZasix;
        this.emergency_name = emergency_name;
        this.emergency_location = emergency_location;
        this.emergency_phone = emergency_phone;
        this.emergency_phone = parent_phone;
    }
}
