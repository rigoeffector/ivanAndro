package com.example.rigoeefector.celestinfinal;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rigoeefector on 7/31/20.
 */

public class DataParser {
    private HashMap<String, String> getSingleNearByPlace(JSONObject googlePlaceJSON){
        HashMap<String, String> googlePlaceMap = new HashMap<>();

        String NameOfPlace = "-NA-";
        String vicinity = "-NA-";
        String latitude = "-";
        String longitude = "";
        String reference = "";
        String place_ids = "-NA-";
        String phoneNumber = "-NA-";


        try
        {

            if(!googlePlaceJSON.isNull("name")){
                NameOfPlace = googlePlaceJSON.getString("name");
            }
             if(!googlePlaceJSON.isNull("vicinity")){
                 vicinity = googlePlaceJSON.getString("vicinity");
             }

             latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
             longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
             if(!googlePlaceJSON.isNull("place_id")){
                 place_ids = googlePlaceJSON.getString("place_id");
                 getPhoneNumbers(place_ids);
             }


            reference = googlePlaceJSON.getString("reference");



            googlePlaceMap.put("place_name", NameOfPlace);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("place_id_text", place_ids);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return  googlePlaceMap;
    }

    private String getPhoneNumbers(String place_id) {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json\n" + "?");
        googleURL.append("place_id=" + place_id);
        googleURL.append("&fields=formatted_phone_number");
        googleURL.append("&key="+"AIzaSyAOA62mD3Kv1OyFV0C33YfUSKm-qLFoSRw");

        Log.d("GoogleMapPhone", "url = "+ googleURL.toString());

        return  googleURL.toString();

    }


    private List<HashMap<String, String>> getAllNearByPlaces(JSONArray jsonArray){
         int counter = jsonArray.length();

         List<HashMap<String, String>>  NearbyPlacesList = new ArrayList<>();

         HashMap<String, String> NearbyPlaceMap = null;


         for(int i=0; i< counter; i++){
             try
             {
                 NearbyPlaceMap = getSingleNearByPlace((JSONObject) jsonArray.get(i));
                 NearbyPlacesList.add(NearbyPlaceMap);
             }
             catch (JSONException e) {
                 e.printStackTrace();
             }
         }

         return NearbyPlacesList;
    }

    public  List<HashMap<String, String>> parse(String jSONData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(jSONData);
            jsonArray = jsonObject.getJSONArray("results");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return  getAllNearByPlaces(jsonArray);
    }
}
