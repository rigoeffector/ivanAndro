package com.example.rigoeefector.celestinfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigoeefector.celestinfinal.Models.FeedBackForm;
import com.example.rigoeefector.celestinfinal.ServerRequests.jsonPlaceholder;
import com.example.rigoeefector.celestinfinal.Session.SaveSharedPreference;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedBack extends AppCompatActivity {
    public TextView subject,description;
    public Button submit;
    private  String BASE_URL = Constants.BASEURL+"api/messages/";
    public Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private  String creatorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        
        subject =(TextView) findViewById(R.id.subject);
        description =(TextView) findViewById(R.id.description);
        submit = (Button) findViewById(R.id.saveData);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbar);

        toolbar.inflateMenu(R.menu.drop_down_menu);

        toolbar.setNavigationIcon(R.drawable.backhome);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),homePage.class));
                finish();
            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.logout:
                        Logout();
                        break;
//                    case R.id.sms:
//                        startActivity(new Intent(getApplicationContext(),FeedBack.class));
//                        break;
                }

                return true;
            }
        });
//        for session only to identify an owner
        SharedPreferences sharedPreferences;

        sharedPreferences = getSharedPreferences("login_user",MODE_PRIVATE);
        String userIdshared = sharedPreferences.getString("driverId","DEFAULT");
        creatorId = String.valueOf(userIdshared);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFeedBackFormNow( subject.getText().toString(), description.getText().toString());
                System.out.print("DriverId="+creatorId);
            }

        });

//

        //        FOR DEBUGGING ONLY REMEMEBER TO REMOVE ON PROD

        HttpLoggingInterceptor httpLoggingInterceptor =  new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .build();

        //        FOR DEBUGGING ONLY REMEMEBER TO REMOVE ON PROD

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).
                        addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
                .build();
//        end of retrofit request
//        end of initilalizing retrofit
    }



    public void SendFeedBackFormNow(String s, String s1) {
        Log.d("UserId", creatorId);

        if(!s.isEmpty() || !s1.isEmpty()){
            jsonPlaceholder jsonPlaceholder = retrofit.create(jsonPlaceholder.class);

            FeedBackForm feedBackForm = new FeedBackForm(
                    creatorId,
                    s,
                    s1
            );

            Call<FeedBackForm> call = jsonPlaceholder.submitFeedBack(feedBackForm);
            call.enqueue(new Callback<FeedBackForm>() {
                @Override
                public void onResponse(Call<FeedBackForm> call, Response<FeedBackForm> response) {
                    if(response.isSuccessful()){
                        System.out.print("Success");
                        Toast.makeText(getApplicationContext(),"Message Sent Successfully", Toast.LENGTH_SHORT);
                        subject.setText("");
                        description.setText("");
                    }
                }

                @Override
                public void onFailure(Call<FeedBackForm> call, Throwable t) {
                     t.printStackTrace();
                }
            });
        }



    }
    private void Logout() {
//        TODO MISSING IMPLEMENTATION FOR USER TO LOGOUT WITH SHARED PREFERENCES
        // Set LoggedIn status to false
        SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
