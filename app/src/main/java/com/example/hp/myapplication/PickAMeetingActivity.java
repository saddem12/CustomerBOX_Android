package com.example.hp.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.hp.myapplication.Request.LoginRequest;
import com.example.hp.myapplication.Request.PickAMeetingRequest;
import com.sinch.android.rtc.SinchError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PickAMeetingActivity extends BaseActivity implements SinchService.StartFailedListener {

    Session session;
    Button logoutBtn;
    RequestQueue requestQueue;
    int success;
    ListView a_lv;
    ArrayList<HashMap<String, String>> agentList;
    JSONArray agents = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ameeting);

        session = new Session(this);


        logoutBtn = (Button) findViewById(R.id.logoutBtnC);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                session.setLoggedin(false);
                Intent mainIntent = new Intent(PickAMeetingActivity.this,LogAsActivity.class);
                startActivity(mainIntent);
            }
        });
        requestQueue = Volley.newRequestQueue(PickAMeetingActivity.this);

        a_lv = (ListView) findViewById(R.id.agent_lv) ;
        agentList = new ArrayList<HashMap<String, String>>();

        a_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String a_id = ((TextView) view.findViewById(R.id.a_id)).getText().toString();
                String a_fn = ((TextView) view.findViewById(R.id.aFN)).getText().toString();
                String a_ln = ((TextView) view.findViewById(R.id.aLN)).getText().toString();

                Intent in = new Intent(getApplicationContext(), SendRequestActivity.class);
                in.putExtra("a_id", a_id);
                in.putExtra("a_fn", a_fn);
                in.putExtra("a_ln", a_ln);
                startActivity(in);


            }
        });


        PickAMeetingRequest pickAMeetingRequest = new PickAMeetingRequest(session.getAgenceId(),  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Login Response", response);
                // Response from the server is in the form if a JSON, so we need a JSON Object
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt("success");
                    if (success==1) {
                        agents = jsonObject.getJSONArray("agent");

                        for (int i = 0; i < agents.length(); i++) {
                            JSONObject a = agents.getJSONObject(i);

                            // Storing each json item in variable
                            String id = a.getString("id");
                            String firstName = a.getString("firstName");
                            String lastName = a.getString("lastName");

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put("id", id);
                            map.put("firstName", firstName);
                            map.put("lastName", lastName);

                            // adding HashList to ArrayList
                            agentList.add(map);
                        }

                        ListAdapter adapter = new SimpleAdapter(
                                PickAMeetingActivity.this,
                                agentList,
                                R.layout.list_item,
                                new String[] { "id", "firstName", "lastName"},
                                new int[] { R.id.a_id, R.id.aFN, R.id.aLN });
                        a_lv.setAdapter(adapter);
                    } else {
                        if(jsonObject.getString("message").equals("No user found"))
                            Toast.makeText(PickAMeetingActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PickAMeetingActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError)
                    Toast.makeText(PickAMeetingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(PickAMeetingActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(PickAMeetingActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(pickAMeetingRequest);
    }



    @Override
    public void onStarted() {

    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        Log.i("Sinch", "Hellllllllllllllllllllooooooooooooooooooooo 0");

        if (!getSinchServiceInterface().isStarted()) {
            Log.i("Sinch", "Hellllllllllllllllllllooooooooooooooooooooo 1");
            getSinchServiceInterface().startClient(session.getUserId());
            Log.i("Sinch", "Hellllllllllllllllllllooooooooooooooooooooo 2");
        }
    }

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }






}
