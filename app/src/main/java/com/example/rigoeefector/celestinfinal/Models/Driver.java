package com.example.rigoeefector.celestinfinal.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rigoeefector on 7/11/20.
 */

public class Driver {

    private String username;
    private String em_phone;
    private String driver_id;
   private  String first_name;
    private  String last_name;
    private  String address;


    private String age;
    private  String carModel;
    private String car_plate_number;
    public String getEm_phone() {
        return em_phone;
    }

    public String getUsername() {
        return username;
    }

    public String getDriver_id() {
        return driver_id;
    }


    public String getFname() {
        return first_name;
    }

    public String getLname() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarplatenumber() {
        return car_plate_number;
    }
}
