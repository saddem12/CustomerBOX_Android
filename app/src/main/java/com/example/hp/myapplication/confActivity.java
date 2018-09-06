package com.example.hp.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class confActivity extends AppCompatActivity {

    Intent i;
    String meetings_id, email;
    DatePicker dpicker;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);

        i= getIntent();
        meetings_id = i.getStringExtra("meetings_id");
        email = i.getStringExtra("email");
        dpicker=(DatePicker)findViewById(R.id.date_picker);

        next = (Button)findViewById(R.id.next) ;

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                int m = dpicker.getMonth() + 1;
                String month;
                if (m<10){
                    month= "0"+String.valueOf(m);
                }else{
                    month= String.valueOf(m);
                }

                int d = dpicker.getDayOfMonth();
                String day;
                if (d<10){
                    day= "0"+String.valueOf(d);
                }else{
                    day= String.valueOf(d);
                }

                String dateDMY = day+"-"+ month+"-"+dpicker.getYear();

                Intent mainIntent = new Intent(confActivity.this,conf2Activity.class);
                mainIntent.putExtra("meetings_id", meetings_id);
                mainIntent.putExtra("email", email);
                mainIntent.putExtra("dateDMY", dateDMY);
                startActivity(mainIntent);
            }
        });



    }
}
