package com.example.rigoeefector.celestinfinal.ServerRequests;


import com.example.rigoeefector.celestinfinal.Models.Driver;
import com.example.rigoeefector.celestinfinal.Models.Emergency;
import com.example.rigoeefector.celestinfinal.Models.FeedBackForm;
import com.example.rigoeefector.celestinfinal.Models.SignupDriver;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rigoeefector on 7/11/20.
 */

public interface jsonPlaceholder {
    @POST("login.php")
    Call<Driver> loginDriver (@Query("username") String username, @Query("user_password") String user_password);

    @POST("create.php")
    Call<SignupDriver> signUpDrivers(@Body SignupDriver signupDriver);

    @POST("create.php")
    Call<FeedBackForm> submitFeedBack(@Body FeedBackForm feedBackForm);

    @POST("create.php")
    Call<Emergency>accidentDetected (@Body Emergency emergency);
}
