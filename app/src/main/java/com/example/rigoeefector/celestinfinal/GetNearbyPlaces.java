package com.example.rigoeefector.celestinfinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rigoeefector on 7/31/20.
 */

public class GetNearbyPlaces extends  AsyncTask<Object, String, String> {


    private  Context context;

    private  String googleplaceData, url;
    private GoogleMap mMap;
    public  String vic;
    public String pId;
    public Double laty;
    public Double langy;
    public String pname;

    public static final String APP_DATA = "AppData";
    SharedPreferences DATA;
    SharedPreferences.Editor Editor;
    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(Object... objects) {


        mMap =(GoogleMap) objects[0];
        url =(String) objects[1];


        DownloadUrl downloadUrl = new DownloadUrl();

        try
        {
            googleplaceData = downloadUrl.ReadTheURL(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return googleplaceData;
    }


    @Override
    protected void onPostExecute(String s) {
        if(!isCancelled()){
            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();

            nearbyPlacesList = dataParser.parse(s);

            DisplayNearByPlaces(nearbyPlacesList);
//            delegate.processFinish(DisplayNearByPlaces(nearbyPlacesList));



        }


//        delegate.processFinish(DisplayNearByPlaces(nearbyPlacesList));



//        super.onPostExecute(s);
    }



    private  void DisplayNearByPlaces(List<HashMap<String, String>> nearbyPlacesList){
        for (int i=0 ; i<nearbyPlacesList.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String>  googleNearbyPlace = nearbyPlacesList.get(i);

            String nameOfPlace = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
            Double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
            Double lng = Double.parseDouble(googleNearbyPlace.get("lng"));
            String  placeId = googleNearbyPlace.get("place_id_text");


            LatLng latLng = new LatLng(lat, lng);

            markerOptions.position(latLng);

            markerOptions.title(nameOfPlace);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
//         move camera to that location now
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


              mMap.addMarker(markerOptions);
              if(mMap.addMarker(markerOptions) != null){
                  pname = nameOfPlace;
                  vic = vicinity;
                  pId = placeId;
                  laty = lat;
                  langy = lng;
              }


        }

    }




}
