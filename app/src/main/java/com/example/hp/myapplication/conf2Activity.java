package com.example.hp.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Request.ConfRequest;
import com.example.hp.myapplication.Request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class conf2Activity extends AppCompatActivity {

    TimePicker tpicker;
    Button conf_meet;
    Intent i;
    String meetings_id, email, dateDMY, dateHM;
    RequestQueue requestQueue;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf2);

        i= getIntent();
        meetings_id = i.getStringExtra("meetings_id");
        email = i.getStringExtra("email");
        dateDMY = i.getStringExtra("dateDMY");

        requestQueue = Volley.newRequestQueue(conf2Activity.this);//Creating the RequestQueue

        tpicker=(TimePicker)findViewById(R.id.time_picker);
        conf_meet = (Button)findViewById(R.id.conf_meet);

        conf_meet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                int h = tpicker.getCurrentHour();
                String hour;
                if (h<10){
                    hour= "0"+String.valueOf(h);
                }else{
                    hour= String.valueOf(h);
                }
                int m = tpicker.getCurrentMinute();
                String minute;
                if (h<10){
                    minute= "0"+String.valueOf(m);
                }else{
                    minute= String.valueOf(m);
                }

                dateHM = hour+":"+minute;


                ConfRequest confRequest = new ConfRequest(meetings_id, dateDMY, dateHM,  new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Login Response", response);
                        // Response from the server is in the form if a JSON, so we need a JSON Object
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            success = jsonObject.getInt("success");
                            if (success==1) {

                                try {
                                    new SimpleMail().sendEmail(email, "Confirmation RDV", "Votre rendez-vous sera le: "+dateDMY+" "+dateHM);
                                } catch (Exception e) {
                                    Log.e("SendMail+++++", e.getMessage(), e);
                                }
                                Intent mainIntent = new Intent(conf2Activity.this,AgentHomeActivity.class);
                                startActivity(mainIntent);

                            } else {
                                if(jsonObject.getString("message").equals("No user found"))
                                    Toast.makeText(conf2Activity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(conf2Activity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ServerError)
                            Toast.makeText(conf2Activity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        else if (error instanceof TimeoutError)
                            Toast.makeText(conf2Activity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(conf2Activity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(confRequest);
            }
        });


    }
}
