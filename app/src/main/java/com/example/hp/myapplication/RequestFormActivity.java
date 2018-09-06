package com.example.hp.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Request.InscriptionRequest;
import com.example.hp.myapplication.Request.RequestFormRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestFormActivity extends AppCompatActivity {

    Intent i;
    String a_id,a_fn,a_ln, sub, req, cus_id;
    TextView name;
    EditText subject, request;
    Button send;
    Session s;

    RequestQueue requestQueue;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);


        i= getIntent();
        a_id = i.getStringExtra("a_id");
        a_fn = i.getStringExtra("a_fn");
        a_ln = i.getStringExtra("a_ln");

        name= (TextView) findViewById(R.id.exp) ;
        name.setText("Send To "+a_ln+" "+a_fn);
        subject= (EditText) findViewById(R.id.sbj) ;
        request= (EditText) findViewById(R.id.dtls) ;
        send=(Button) findViewById(R.id.btn_send);
        s=new Session(this);

        requestQueue = Volley.newRequestQueue(RequestFormActivity.this);



        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                cus_id= s.getUserId();
                sub=subject.getText().toString();
                req=request.getText().toString();
                if(sub.equals("")||req.equals("")){  //*************************here
                    Toast.makeText(RequestFormActivity.this, "Please make sure to fill all your credentials!", Toast.LENGTH_SHORT).show();
                }else{

                    final ProgressDialog progressDialog = new ProgressDialog(RequestFormActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Sending Request ");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    RequestFormRequest requestFormRequest = new RequestFormRequest(sub, req, cus_id,a_id,  new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("********************", response);
                            progressDialog.dismiss();
                            // Response from the server is in the form if a JSON, so we need a JSON Object
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                success = jsonObject.getInt("success");
                                if (success==1) {

                                    Toast.makeText(RequestFormActivity.this, "Sended!", Toast.LENGTH_SHORT).show();
                                    Intent loginSuccess = new Intent(RequestFormActivity.this, PickAMeetingActivity.class);
                                    startActivity(loginSuccess);


                                } else {
                                    if(jsonObject.getString("message").equals("No user found"))
                                        Toast.makeText(RequestFormActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(RequestFormActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(RequestFormActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(RequestFormActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(RequestFormActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(requestFormRequest);
                }

            }
        });



    }
}
