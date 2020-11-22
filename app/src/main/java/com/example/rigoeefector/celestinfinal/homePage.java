package com.example.rigoeefector.celestinfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.rigoeefector.celestinfinal.Session.SaveSharedPreference;

import javax.microedition.khronos.egl.EGLDisplay;

public class homePage extends AppCompatActivity {
public TextView username, titleBar;
public  TextView fname;
public TextView lname;
public  TextView address, em_phone;
public  TextView carmodel;
public  TextView carplate;
public Button saveChanges;;
public String allowedSattus;
final  static  String DEFAULT = "N/A";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        username =(TextView) findViewById(R.id.username);
        fname = (TextView) findViewById(R.id.fname);
        lname = (TextView) findViewById(R.id.lname);
        address =(TextView) findViewById(R.id.address);
        carmodel =(TextView) findViewById(R.id.carmodel);
        carplate =(TextView) findViewById(R.id.carplatenumber);
        em_phone = (TextView)findViewById(R.id.em_phoneNumber);
        saveChanges =(Button) findViewById(R.id.savechanges);


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDrive();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        titleBar = (TextView) findViewById(R.id.titleToolbar);


//        for session only to identify an owner
        SharedPreferences sharedPreferences;

        sharedPreferences = getSharedPreferences("login_user",MODE_PRIVATE);




        String usernameShared = sharedPreferences.getString("username", DEFAULT);
        String em_phoneShared = sharedPreferences.getString("emPhone", DEFAULT);
        String fnameShared = sharedPreferences.getString("fname", DEFAULT);
        String lnameShared = sharedPreferences.getString("lname", DEFAULT);
        String addressShared = sharedPreferences.getString("address", DEFAULT);
        String carmodelShared = sharedPreferences.getString("carmodel", DEFAULT);
        String carplateShared = sharedPreferences.getString("carplate", DEFAULT);



        username.setText(usernameShared.toString());
        fname.setText(fnameShared.toString());
        lname.setText(lnameShared.toString());
        address.setText(addressShared.toString());
        carmodel.setText(carmodelShared.toString());
        carplate.setText(carplateShared.toString());
        em_phone.setText(em_phoneShared.toString());

        Switch isAllowed =(Switch) findViewById(R.id.switch1);
        isAllowed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(getApplicationContext(),"Checked", Toast.LENGTH_LONG);
                    Log.d("ON", String.valueOf(b));
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login_user",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    allowedSattus = String.valueOf(b);
                    editor.putString("allowedDetection", String.valueOf(b));
                    editor.commit();
                }
                else {
                    Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_SHORT);
                    Log.d("OFF", String.valueOf(b));
                }
            }
        });
        toolbar.inflateMenu(R.menu.drop_down_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.logout:
                        Logout();
                        break;
                    case R.id.sms:
                        startActivity(new Intent(getApplicationContext(),FeedBack.class));
                        break;
                }

                return true;
            }
        });

    }

    private void goToDrive() {
        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
        Toast.makeText(getApplicationContext(),"Value of Switch" + allowedSattus, Toast.LENGTH_SHORT);
    }

    private void Logout() {
//        TODO MISSING IMPLEMENTATION FOR USER TO LOGOUT WITH SHARED PREFERENCES
        // Set LoggedIn status to false
        SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}




