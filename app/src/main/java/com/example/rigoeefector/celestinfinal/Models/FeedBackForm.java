package com.example.rigoeefector.celestinfinal.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rigoeefector on 7/12/20.
 */

public class FeedBackForm {
    private String driversId;
    private String subject_title;
    private String msg_description;
    private  String message;

    public FeedBackForm(String driversId,String subject_title, String msg_description) {
        this.driversId = driversId;
        this.subject_title = subject_title;
        this.msg_description = msg_description;
    }
}