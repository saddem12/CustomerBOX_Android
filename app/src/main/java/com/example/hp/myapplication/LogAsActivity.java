package com.example.hp.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogAsActivity extends AppCompatActivity {

    Button BtnA, BtnC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_as);

        BtnA = (Button) findViewById(R.id.btnA);
        BtnA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent mainIntent = new Intent(LogAsActivity.this,LoginActivity.class);
                mainIntent.putExtra("role", "Agent");
                startActivity(mainIntent);
            }
        });
        BtnC = (Button) findViewById(R.id.btnC);
        BtnC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent mainIntent = new Intent(LogAsActivity.this,LoginActivity.class);
                mainIntent.putExtra("role", "Customer");
                startActivity(mainIntent);
            }
        });

    }
}
