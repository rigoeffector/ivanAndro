package com.example.rigoeefector.celestinfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigoeefector.celestinfinal.Models.Driver;
import com.example.rigoeefector.celestinfinal.ServerRequests.jsonPlaceholder;
import com.example.rigoeefector.celestinfinal.Session.SaveSharedPreference;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Logout{
    public TextView username;
    public TextView passwords;
    public Button login;
    public  TextView signupLink;
    Constants constants;
    public Retrofit retrofit;
    private  String BASE_URL = constants.BASEURL+"api/drivers/";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (TextView) findViewById(R.id.username);
        passwords = (TextView) findViewById(R.id.passsword);
        signupLink = (TextView) findViewById(R.id.signupLink);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login(username.getText().toString(), passwords.getText().toString());
            }
        });
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });


        //        FOR DEBUGGING ONLY REMEMEBER TO REMOVE ON PROD

        HttpLoggingInterceptor httpLoggingInterceptor =  new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .build();

        //        FOR DEBUGGING ONLY REMEMEBER TO REMOVE ON PROD

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


//        user's session

        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())){
            Intent intent = new Intent(getApplicationContext(),homePage.class);
            startActivity(intent);
        }else {
//            TODO MISSING IMPLEMENTATION ;
//            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
        }


    }

    private void Login(String s, String s1) {
        if(!s.isEmpty() && !s1.isEmpty()){
            jsonPlaceholder jsonPlaceholderApi = retrofit.create(jsonPlaceholder.class);
            Call<Driver> call = jsonPlaceholderApi.loginDriver(s,s1);
            call.enqueue(new Callback<Driver>() {
                @Override
                public void onResponse(Call<Driver> call, Response<Driver> response) {
 if(response.isSuccessful()){
     Driver driver = response.body();

    Log.d("Respose", String.valueOf(driver.getEm_phone()));
     SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
     String driverId = driver.getDriver_id().toString();
     String username = driver.getUsername().toString();
     String emPhone = driver.getEm_phone().toString();
     String fname = driver.getFname().toString();
     String lname = driver.getLname().toString();
     String address = driver.getAddress().toString();
     String carModel = driver.getCarModel().toString();
     String carplate= driver.getCarplatenumber().toString();

     Intent intent = new Intent(getApplicationContext(),homePage.class);

     SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login_user",MODE_PRIVATE);

     SharedPreferences.Editor editor = sharedPreferences.edit();
     editor.putString("driverId", driverId);
     editor.putString("username", username);
     editor.putString("fname", fname);
     editor.putString("lname",lname);
     editor.putString("address", address);
     editor.putString("carmodel", carModel);
     editor.putString("carplate", carplate);
     editor.putString("emPhone", emPhone);
     editor.commit();
     startActivity(intent);
     return;

 }
     else{
     Toast.makeText(getApplicationContext(),"Error"  + call.request(),Toast.LENGTH_SHORT).show();
 }
                }

                @Override
                public void onFailure(Call<Driver> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Fill the Form",Toast.LENGTH_SHORT).show();
        }

    }
}
