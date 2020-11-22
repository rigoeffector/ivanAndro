package com.example.rigoeefector.celestinfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigoeefector.celestinfinal.Models.Driver;
import com.example.rigoeefector.celestinfinal.Models.SignupDriver;
import com.example.rigoeefector.celestinfinal.ServerRequests.jsonPlaceholder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    public EditText username, password,em_phoneNumber, fname, lname, address, age, car_model, car_plate;
    public Button signup;
    public TextView loginPage;
    private  String BASE_URL = Constants.BASEURL+"/api/drivers/";
    public Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username =(EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.passsword);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        address = (EditText) findViewById(R.id.address);
        age=(EditText) findViewById(R.id.age);
        car_model = (EditText) findViewById(R.id.model);
        car_plate =(EditText) findViewById(R.id.carplatenumber);
        em_phoneNumber=(EditText) findViewById(R.id.em_phoneNumber);
        signup =(Button) findViewById(R.id.signup);
        loginPage = (TextView) findViewById(R.id.signinLink);
//       retrofit request initialize


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).
                        addConverterFactory(GsonConverterFactory.create())
//        .client(okHttpClient)/
                .build();
//        end of retrofit request
//        end of initilalizing retrofit


        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpUser(username.getText().toString(), em_phoneNumber.getText().toString(),  password.getText().toString(), fname.getText().toString(), lname.getText().toString()
                ,address.getText().toString(), age.getText().toString(), car_model.getText().toString(), car_plate.getText().toString());
            }
        });
    }

    private void SignUpUser(String s,String s0,  String s1, String s2, String s3, String s4, String s5, String s6, String s7) {
        if(!s.isEmpty() && !s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty()
                && !s5.isEmpty() && !s6.isEmpty() && !s7.isEmpty()){
            Log.d("Sawaaa", s);
            jsonPlaceholder jsonPlaceholderApi = retrofit.create(jsonPlaceholder.class);
            SignupDriver signupDriver = new SignupDriver(
                    s,s0,s1,s2,s3,s4,s5,s6,s7
            );

            Call<SignupDriver> call  = jsonPlaceholderApi.signUpDrivers( signupDriver);
            call.enqueue(new Callback<SignupDriver>() {
                @Override
                public void onResponse(Call<SignupDriver> call, Response<SignupDriver> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Driver is Registered Successfully:",Toast.LENGTH_SHORT).show();
                        username.setText("");
                        password.setText("");
                        fname.setText("");
                        lname.setText("");
                        address.setText("");
                        age.setText("");
                        car_model.setText("");
                        car_plate.setText("");
                        em_phoneNumber.setText("");
                    }
                }

                @Override
                public void onFailure(Call<SignupDriver> call, Throwable t) {
t.printStackTrace();
                }
            });


        }
        else {
            Toast.makeText(getApplicationContext(),"Fill the Form",Toast.LENGTH_SHORT).show();
        }
    }
}
