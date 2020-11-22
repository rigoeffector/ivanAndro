package com.example.rigoeefector.celestinfinal.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rigoeefector on 7/11/20.
 */

public class SignupDriver {
    private String username;
    private String em_phone;
    private String user_password;
     @SerializedName("firstname")
     private String first_name;
     @SerializedName("lastname")
     private String last_name;
    private String address;
    private String age;
     @SerializedName("carmodel")
     private String carModel;
     @SerializedName("carplatenumber")
     private  String car_plate_number;

    public SignupDriver(String username, String em_phone,  String user_password, String first_name, String last_name, String address, String age, String carModel, String car_plate_number) {
        this.username = username;
        this.em_phone = em_phone;
        this.user_password = user_password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.age = age;
        this.carModel = carModel;
        this.car_plate_number = car_plate_number;
    }
}
