package com.example.hp.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

public class AgentHomeActivity extends BaseActivity implements SinchService.StartFailedListener  {

    Button logoutBtn, demandsBtn, ConfirmedBtn, settings;
    Session session;
    private ProgressDialog mSpinner;

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
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {
        Log.i("onStarted", "haaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_home);


        session = new Session(this);


        logoutBtn = (Button) findViewById(R.id.logoutBtnA);
        demandsBtn = (Button) findViewById(R.id.demands_btn);
        ConfirmedBtn = (Button) findViewById(R.id.confirmed_btn);
        settings = (Button) findViewById(R.id.sttings);



        settings.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            public void onClick(View v)
            {
                session.setLoggedin(true);
                Intent mainIntent = new Intent(AgentHomeActivity.this,UpdateActivity.class);
                startActivity(mainIntent);



            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                session.setLoggedin(false);
                Intent mainIntent = new Intent(AgentHomeActivity.this,LogAsActivity.class);
                startActivity(mainIntent);



            }
        });

        demandsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent mainIntent = new Intent(AgentHomeActivity.this,DemandsActivity.class);
                startActivity(mainIntent);



            }
        });

        ConfirmedBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent mainIntent = new Intent(AgentHomeActivity.this,ConfirmedActivity.class);
                startActivity(mainIntent);



            }
        });

    }
}
