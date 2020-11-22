package com.example.rigoeefector.celestinfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigoeefector.celestinfinal.Models.Emergency;
import com.example.rigoeefector.celestinfinal.ServerRequests.jsonPlaceholder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements SensorEventListener,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = MapsActivity.class.getName();
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_code = 99;
    private Double latitude, longitude;
    private int PromiximityRadius = 10000;

    private SharedPreferences sharedPreferences;
    private String creatorId, getEmergency_phoneParents;

    //    sensorText
    private TextView mTextMessage;
    private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager SM;
    private TextView locationName, locationCoords;


//    retrofit and model data


    private String xT, yT, zT, d_Id,
            location_detection, location_coordinate,
            speed_movement, emergency_name, emergency_location, emergency_phone, parent_phone;

    private String BASE_URL = Constants.BASEURL + "api/accident/";
    public Retrofit retrofit;
    Boolean isClicked = false;

    GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserPermissionLocation();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        getNearbyPlaces.delegate = this;
//        getNearbyPlaces.execute();


//        GRAB ASYNCH RESULT NOW


        // Create our Sensor Manger
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register Sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Assign Text
        xText = (TextView) findViewById(R.id.Xasis);
        yText = (TextView) findViewById(R.id.Yaxis);
        zText = (TextView) findViewById(R.id.Zaxis);


        //        for session only to identify an owner
        SharedPreferences sharedPreferences;

        sharedPreferences = getSharedPreferences("login_user", MODE_PRIVATE);
        String userIdshared = sharedPreferences.getString("driverId", "DEFAULT");
        String parentPhoneNumber = sharedPreferences.getString("emPhone", "DEFAUL");
        creatorId = String.valueOf(userIdshared);
        getEmergency_phoneParents = String.valueOf(parentPhoneNumber);


        //        FOR DEBUGGING ONLY REMEMEBER TO REMOVE ON PROD

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        getting my current location

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);

        }


//
    }


    public boolean checkUserPermissionLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, Request_User_Location_code);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, Request_User_Location_code);
            }
            return false;
        } else {
            return true;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case Request_User_Location_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


    public void onClick(View view) {
        isClicked = true;
        String hospital = "hospital", police = "police";

        Object transferData[] = new Object[2];

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();


        switch (view.getId()) {
            case R.id.search_icon:
                EditText addressField = (EditText) findViewById(R.id.location_search);
                String address = addressField.getText().toString();

                List<Address> addressList = null;

                MarkerOptions userMarkerOptions = new MarkerOptions();

                if (!TextUtils.isEmpty(address)) {

                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(address, 1);
                        if (addressList != null) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                                userMarkerOptions.position(latLng);

                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


                                mMap.addMarker(userMarkerOptions);
                                Log.d("Named=", address);

//         move camera to that location now


                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                            }


                        } else {
                            Toast.makeText(this, "No Address found! try again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "enter any place", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.policed_nearby:
                mMap.clear();
                String url = getUrl(latitude, longitude, police);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Police Station.......", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing  Nearby Police Station.......", Toast.LENGTH_SHORT).show();
                break;
            case R.id.hospital_nearby:
                mMap.clear();
                String url1 = getUrl(latitude, longitude, hospital);
                transferData[0] = mMap;
                transferData[1] = url1;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Hospitals.......", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing  Nearby Hospitals.......", Toast.LENGTH_SHORT).show();
                Log.d("Url=", url1);
                break;
            default:
                return;
        }

    }

    private String getUrl(Double latitude, Double longitude, String nearbyPlace) {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json\n" + "?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + PromiximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyAOA62mD3Kv1OyFV0C33YfUSKm-qLFoSRw");

        Log.d("GoogleMap", "url = " + googleURL.toString());
        return googleURL.toString();

    }


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


//        connect our google api cleint


        googleApiClient.connect();

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


//        to update movement we use fusion location api


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {            // TODO: Consider calling
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                    locationRequest,
                    this);


        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


//        to diplay name of location on the marker


        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);


        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null){


        String address = addresses.get(0).getAddressLine(0).toString();
        markerOptions.title(address);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

//        locationName.setText(address);
//        locationName.setText("lat="+location.getLatitude()+ "long="+location.getLongitude());


        currentUserLocationMarker = mMap.addMarker(markerOptions);

//         move camera to that location now


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));


//        let get updated location


        if (googleApiClient != null && address != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

//        locationName.setText(address);
//        locationName.setText("lat="+location.getLatitude()+ "long="+location.getLongitude());

            if (String.valueOf(address) != null) {
                Log.d("Names=", address);
//                locationName.setText(address);
                location_detection = address;
                location_coordinate = "lat=" + location.getLatitude() + "long=" + location.getLongitude();

                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
//                getNearbyPlaces.execute();
//                String placeName = getNearbyPlaces.pname;
//                Log.d("Pnames=", placeName);

            }

        }
        }
//        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();GetN
//        if(getNearbyPlaces.getStatus() == AsyncTask.Status.FINISHED){
//
//            Log.d("placeIds=", "FINISHED");
//
//
//
//        }
//        if(getNearbyPlaces.getStatus() == AsyncTask.Status.PENDING){
//            Log.d("placeIds=", " not FINISHED");
//
//        }
//        if(getNearbyPlaces.getStatus() == AsyncTask.Status.RUNNING){
//            Log.d("placeIds=", " RUNNING");
//
//
//        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xText.setText("X: " + sensorEvent.values[0]);
        yText.setText("Y: " + sensorEvent.values[1]);
        zText.setText("Z: " + sensorEvent.values[2]);
        String maxX = String.valueOf(9);
        Toast.makeText(getApplicationContext(), "xvalue=" + xText, Toast.LENGTH_SHORT);

        double xMax = sensorEvent.values[0];
        double yMax = sensorEvent.values[1];
        double zMax = sensorEvent.values[2];
        double speedMax = xMax + yMax;
        double speedAll = xMax + yMax + zMax;

        MediaPlayer mPlayer2 = new MediaPlayer();
        if (Math.abs(speedMax) >= 25) {

            mPlayer2 = MediaPlayer.create(this, R.raw.alarm);
            mPlayer2.start();
            System.out.println(xMax);
        }
        if (Math.abs(speedMax) <= 9) {
            mPlayer2.pause();
        }
        final boolean[] thatThingHappened = {false};
        if (!thatThingHappened[0] && Math.abs(speedMax) >= 40 && Math.abs(speedMax) <= 41) {
            speed_movement = String.valueOf(speedAll);
            xT = String.valueOf(xMax);
            yT = String.valueOf(yMax);
            zT = String.valueOf(zMax);
            Log.d("sana=", "byarenze");
            Log.d("Location=", "byarenze=" + String.valueOf(location_coordinate));
            OnceRequest onceRequest = new OnceRequest();
            onceRequest.run(new Runnable() {
                @Override
                public void run() {
                    thatThingHappened[0] = true;
                    SendAccidentInfo(d_Id, location_detection, location_coordinate,
                            xT, yT, zT, speed_movement, emergency_name,
                            emergency_location, emergency_phone, parent_phone);
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    SM.unregisterListener(MapsActivity.this);

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
                location_detection,
                location_coordinate,
                speed_movement,
                xT,
                yT,
                zT,
                "POLICE STATION",
                "KICACYIRU POLICE STATION",
                emergency_phone,
                getEmergency_phoneParents
        );
        Call<Emergency> call = jsonPlaceholderApi.accidentDetected(emergency);
        call.enqueue(new Callback<Emergency>() {
            @Override
            public void onResponse(Call<Emergency> call, Response<Emergency> response) {
                if (response.isSuccessful()) {
                    Log.d("UserId=", getEmergency_phoneParents);
                    Toast.makeText(getApplicationContext(), "Sent Successfull ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "not Successfull ", Toast.LENGTH_SHORT).show();

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


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Statred", "states");

    }
}




