package com.example.hp.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SendRequestActivity extends AppCompatActivity {

    Intent i;
    ImageButton formBtn, vocalBtn;
    String a_id,a_fn,a_ln;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        i= getIntent();
        a_id = i.getStringExtra("a_id");
        a_fn = i.getStringExtra("a_fn");
        a_ln = i.getStringExtra("a_ln");

        formBtn= (ImageButton) findViewById(R.id.btn_form) ;

        formBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent in = new Intent(SendRequestActivity.this,RequestFormActivity.class);

                in.putExtra("a_id", a_id);
                in.putExtra("a_fn", a_fn);
                in.putExtra("a_ln", a_ln);
                startActivity(in);
            }

        });

        vocalBtn= (ImageButton) findViewById(R.id.btn_vocal) ;

        vocalBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent in = new Intent(SendRequestActivity.this,RequestVocalActivity.class);
                startActivity(in);
            }

        });

    }
}
