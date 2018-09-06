package com.example.hp.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                if(session.getLoggedin()){

                    if(session.getRole().equals("Customer")){
                        Intent mainIntent = new Intent(MainActivity.this,PickAMeetingActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }else if(session.getRole().equals("Agent")){
                        Intent mainIntent = new Intent(MainActivity.this,AgentHomeActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }else{
                    Intent mainIntent = new Intent(MainActivity.this,LogAsActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, 5000);
    }
}
