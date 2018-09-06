package com.example.hp.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
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
import com.example.hp.myapplication.Request.ConfirmedCalenderRequest;
import com.example.hp.myapplication.Request.DemandsRequest;
import com.example.hp.myapplication.Request.MeetingListByDateRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ConfirmedActivity extends AppCompatActivity {


    private SimpleDateFormat dateff = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    RequestQueue requestQueue;
    Session session;
    int success;
    ArrayList<String> dateList;
    JSONArray meetings = null;
    String selectedDateRdv;
    JSONArray demandes = null;
    ListView demands_lv;
    ArrayList<HashMap<String, String>> demandsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed);

        session= new Session(this);

        requestQueue = Volley.newRequestQueue(ConfirmedActivity.this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);


        dateList = new ArrayList<String>();
        demands_lv = (ListView) findViewById(R.id.ListByDate) ;


        demands_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String meetings_id = ((TextView) view.findViewById(R.id.a_id)).getText().toString();

                Intent in = new Intent(getApplicationContext(), ConfirmedDtlsActivity.class);
                in.putExtra("meetings_id", meetings_id);
                startActivity(in);

            }
        });

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                try {
                    Date date = formatter.parse(dateClicked.toString());
                    Log.d("****************+++", ""+dateff.format(date));
                    selectedDateRdv = dateff.format(date);

                    MeetingListByDateRequest meetingListByDateRequest = new MeetingListByDateRequest(session.getUserId(),selectedDateRdv,  new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Login Response", response);
                            // Response from the server is in the form if a JSON, so we need a JSON Object
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                success = jsonObject.getInt("success");
                                demandsList = new ArrayList<HashMap<String, String>>();
                                if (success==1) {
                                    demandes = jsonObject.getJSONArray("meetings");

                                    for (int i = 0; i < demandes.length(); i++) {
                                        JSONObject a = demandes.getJSONObject(i);

                                        // Storing each json item in variable
                                        String id = a.getString("id");
                                        String firstName = a.getString("firstName");
                                        String lastName = a.getString("lastName");
                                        String time = a.getString("time");

                                        // creating new HashMap
                                        HashMap<String, String> map = new HashMap<String, String>();

                                        // adding each child node to HashMap key => value
                                        map.put("id", id);
                                        map.put("name", firstName+" "+lastName);
                                        map.put("time", time);

                                        // adding HashList to ArrayList
                                        demandsList.add(map);
                                    }
                                    Log.i("------------", "+++++++++++++++++++++++");
                                } else {
                                    if(jsonObject.getString("message").equals("No meeting found"))
                                        Toast.makeText(ConfirmedActivity.this, "No Meeting Found", Toast.LENGTH_SHORT).show();
                                }

                                ListAdapter adapter = new SimpleAdapter(
                                        ConfirmedActivity.this,
                                        demandsList,
                                        R.layout.list_item2,
                                        new String[] { "id", "name", "time"},
                                        new int[] { R.id.a_id, R.id.aFN, R.id.aLN });
                                demands_lv.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ConfirmedActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof ServerError)
                                Toast.makeText(ConfirmedActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(ConfirmedActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(ConfirmedActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(meetingListByDateRequest);




                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });



        ConfirmedCalenderRequest confirmedCalenderRequest = new ConfirmedCalenderRequest(session.getUserId(),  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Login Response", response);
                // Response from the server is in the form if a JSON, so we need a JSON Object
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt("success");
                    if (success==1) {
                        meetings = jsonObject.getJSONArray("meetings");
                        for (int i = 0; i < meetings.length(); i++) {
                            JSONObject c = meetings.getJSONObject(i);
                            dateList.add(c.getString("date"));
                        }
                        Log.i("------------", "+++++++++++++++++++++++");
                        try {
                            for (int i = 0; i < dateList.size(); i++) {
                                Event ev1 = new Event(Color.WHITE,dateff.parse(dateList.get(i)).getTime());
                                compactCalendar.addEvent(ev1);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if(jsonObject.getString("message").equals("No user found"))
                            Toast.makeText(ConfirmedActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ConfirmedActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError)
                    Toast.makeText(ConfirmedActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(ConfirmedActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(ConfirmedActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(confirmedCalenderRequest);





    }


}
