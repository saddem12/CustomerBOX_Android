package com.example.hp.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MeetingsCalActivity extends AppCompatActivity {

    ListView meetings_cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_cal);

        meetings_cal = (ListView) findViewById(R.id.meetingsCal_lv);

        meetings_cal.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String meetings_id = ((TextView) view.findViewById(R.id.a_id)).getText().toString();

                Intent in = new Intent(getApplicationContext(), ConfirmedDtlsActivity.class);
                in.putExtra("meetings_id", meetings_id);
                startActivity(in);


            }
        });
    }
}
