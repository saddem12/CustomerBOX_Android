package com.example.hp.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Request.DemandsRequest;
import com.example.hp.myapplication.Request.PickAMeetingRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DemandsActivity extends AppCompatActivity {
    ListView demands_lv;
    ArrayList<HashMap<String, String>> demandsList;
    RequestQueue requestQueue;
    int success;
    JSONArray demandes = null;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demands);
        requestQueue = Volley.newRequestQueue(DemandsActivity.this);

        session= new Session(this);
        demands_lv = (ListView) findViewById(R.id.demands_lv) ;
        demandsList = new ArrayList<HashMap<String, String>>();

        demands_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String meetings_id = ((TextView) view.findViewById(R.id.a_id)).getText().toString();

                Intent in = new Intent(getApplicationContext(), DemandDtlsActivity.class);
                in.putExtra("meetings_id", meetings_id);
                startActivity(in);

            }
        });


        DemandsRequest demandsRequest = new DemandsRequest(session.getUserId(),  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Login Response", response);
                // Response from the server is in the form if a JSON, so we need a JSON Object
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt("success");
                    if (success==1) {
                        demandes = jsonObject.getJSONArray("meeting");

                        for (int i = 0; i < demandes.length(); i++) {
                            JSONObject a = demandes.getJSONObject(i);

                            // Storing each json item in variable
                            String id = a.getString("id");
                            String firstName = a.getString("firstName");
                            String lastName = a.getString("lastName");
                            String subject = a.getString("subject");

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put("id", id);
                            map.put("name", firstName+" "+lastName);
                            map.put("subject", subject);

                            // adding HashList to ArrayList
                            demandsList.add(map);
                        }
                        Log.i("------------", "+++++++++++++++++++++++");

                        ListAdapter adapter = new SimpleAdapter(
                                DemandsActivity.this,
                                demandsList,
                                R.layout.list_item,
                                new String[] { "id", "name", "subject"},
                                new int[] { R.id.a_id, R.id.aFN, R.id.aLN });
                        demands_lv.setAdapter(adapter);
                    } else {
                        if(jsonObject.getString("message").equals("No user found"))
                            Toast.makeText(DemandsActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DemandsActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError)
                    Toast.makeText(DemandsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(DemandsActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(DemandsActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(demandsRequest);
    }






}

