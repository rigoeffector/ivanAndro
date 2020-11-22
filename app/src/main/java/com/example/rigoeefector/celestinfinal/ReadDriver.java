package com.example.rigoeefector.celestinfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//import android.os.Handler;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigoeefector.celestinfinal.Models.Emergency;
import com.example.rigoeefector.celestinfinal.ServerRequests.jsonPlaceholder;
import com.example.rigoeefector.celestinfinal.Session.SaveSharedPreference;

import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReadDriver extends AppCompatActivity  implements SensorEventListener, LocationListener{

    private SharedPreferences sharedPreferences;
    private  String creatorId, getEmergency_phoneParents;

    LocationManager locationManager;

    Button  location;
    TextView curreny_location;



    ProgressBar progressBar;

    private TextView mTextMessage;
    private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager SM;


    private String xT, yT, zT, d_Id,
            location_detection,location_coordinate,
            speed_movement,emergency_name,emergency_location,emergency_phone, parent_phone;

    private  String BASE_URL = Constants.BASEURL+"api/accident/";
    public Retrofit retrofit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_driver);


        curreny_location =(TextView) findViewById(R.id.currenctLocation);
        location =(Button)  findViewById(R.id.button_location);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

//        runtime permission to get access for locations



        if(ContextCompat.checkSelfPermission(ReadDriver.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ReadDriver.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getLocation();
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });


//        getLocation();




        mTextMessage = (TextView) findViewById(R.id.message);


        // Create our Sensor Manger
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register Sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Assign Text
        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbar);
//        titleBar = (TextView) findViewById(R.id.titleToolbar);

        toolbar.inflateMenu(R.menu.drop_down_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        Logout();
                        break;
                    case R.id.sms:
                        startActivity(new Intent(getApplicationContext(), FeedBack.class));
                        break;
                }

                return true;
            }
        });

//        for session only to identify an owner
        SharedPreferences sharedPreferences;

        sharedPreferences = getSharedPreferences("login_user",MODE_PRIVATE);
        String userIdshared = sharedPreferences.getString("driverId","DEFAULT");
        String parentPhoneNumber = sharedPreferences.getString("emPhone", "DEFAUL");
        creatorId = String.valueOf(userIdshared);
        getEmergency_phoneParents = String.valueOf(parentPhoneNumber);


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

    @SuppressLint("MissingPermission")
//    private void getLocation() {
////        this one to get lat and long
//
//         progressBar.setVisibility(View.VISIBLE);
//        try {
//            locationManager =(LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER ,5000,5, ReadDriver.this);
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xText.setText("X: " + sensorEvent.values[0]);
        yText.setText("Y: " + sensorEvent.values[1]);
        zText.setText("Z: " + sensorEvent.values[2]);
        String maxX = String.valueOf(9);
        Toast.makeText(getApplicationContext(),"xvalue="+xText, Toast.LENGTH_SHORT);

        double xMax = sensorEvent.values[0];
        double yMax = sensorEvent.values[1];
        double zMax = sensorEvent.values[2];
        double speedMax = xMax + yMax;

        MediaPlayer mPlayer2 = new MediaPlayer();
        if(Math.abs(speedMax) >= 25){

            mPlayer2 = MediaPlayer.create(this, R.raw.alarm);
            mPlayer2.start();
            System.out.println(xMax);
        }
        if(Math.abs(speedMax) <= 9) {
            mPlayer2.pause();
        }
        final boolean[] thatThingHappened = {false};
        if( !thatThingHappened[0] && Math.abs(speedMax)>=40 && Math.abs(speedMax)<=41){
         OnceRequest onceRequest = new OnceRequest();
         onceRequest.run(new Runnable() {
             @Override
             public void run() {
                 thatThingHappened[0] = true;
                 SendAccidentInfo(d_Id,location_detection,location_coordinate,
                         xT,yT,zT, speed_movement, emergency_name,
                         emergency_location,emergency_phone, parent_phone);
                 startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                 SM.unregisterListener(ReadDriver.this);

             }
         });

        }
    }
    private void SendAccidentInfo(String d_id, String location_detection,
                                  String location_coordinate, String xT,
                                  String yT, String zT, String speed_movement,
                                  String emergency_name, String emergency_location,
                                  String emergency_phone, String parent_phone) {

        jsonPlaceholder jsonPlaceholderApi = retrofit.create(jsonPlaceholder.class);
        final Emergency emergency = new Emergency(
                creatorId,
                "KAGARAMA KG 45ST",
                "152",
                "40.458",
                "Over 60",
                "480",
                "450",
                "POLICE STATION",
                "KICACYIRU POLICE STATION",
                "+250784638201",
                 getEmergency_phoneParents
        );
        Call<Emergency> call = jsonPlaceholderApi.accidentDetected(emergency);
        call.enqueue(new Callback<Emergency>() {
            @Override
            public void onResponse(Call<Emergency> call, Response<Emergency> response) {
                if(response.isSuccessful()){
                    Log.d("UserId=", getEmergency_phoneParents);
                    Toast.makeText(getApplicationContext(),"Successfull ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"not Successfull ", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<Emergency> call, Throwable t) {
                Log.d("FAILED", String.valueOf(call.isExecuted()));
            t.printStackTrace();
            }
        });
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void Logout() {
//        TODO MISSING IMPLEMENTATION FOR USER TO LOGOUT WITH SHARED PREFERENCES
        // Set LoggedIn status to false
        SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
      Toast.makeText(this,""+location.getLatitude()+", "+location.getLongitude(), Toast.LENGTH_SHORT);

      try {

//          Gecoder to transform street address or any address

          Geocoder geocoder = new Geocoder( ReadDriver.this, Locale.getDefault());
          List<Address> addresses  = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
          String address = addresses.get(0).getAddressLine(0);

          if(!address.equals("")){
              progressBar.setVisibility(View.GONE);

          }


          curreny_location.setText(address );

      }catch (Exception e){
          e.printStackTrace();
      }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
