package com.example.hp.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText txtEmailC, txtPassC;
    Button btnLoginC;
    RequestQueue requestQueue;
    String username,password, role;
    TextView createAccount;
    int success , p;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent i = getIntent();
        role = i.getStringExtra("role");

        session = new Session(this);

        requestQueue = Volley.newRequestQueue(LoginActivity.this);//Creating the RequestQueue

        createAccount = (TextView) findViewById(R.id.createAccount);

        txtEmailC = (EditText) findViewById(R.id.txtEmailC);
        txtPassC = (EditText) findViewById(R.id.txtPassC);
        btnLoginC = (Button)findViewById(R.id.btnLoginC);


        if (role.equals("Agent")){
            createAccount.setVisibility(View.INVISIBLE);
        }

        createAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent mainIntent = new Intent(LoginActivity.this,InscriptionActivity.class);
                startActivity(mainIntent);
            }
        });

        btnLoginC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                username = txtEmailC.getText().toString();
                password = txtPassC.getText().toString();


                if (!username.equals("") && !password.equals("")) { //Username and Password Validation


                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Logging You In");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    LoginRequest loginRequest = new LoginRequest(username, password, role,  new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Login Response", response);
                            progressDialog.dismiss();
                            // Response from the server is in the form if a JSON, so we need a JSON Object
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                success = jsonObject.getInt("success");
                                p = jsonObject.getInt("password");
                                if (success==1) {
                                    session.setUserId(jsonObject.getString("user_id"));
                                    session.setAgenceId(jsonObject.getString("agence_id"));
                                    if(p==1){
                                        Intent loginSuccess = new Intent(LoginActivity.this, PasswordActivity.class);
                                        startActivity(loginSuccess);
                                    }else if(p==0){
                                        session.setLoggedin(true);
                                        if(role.equals("Customer")){
                                            session.setRole("Customer");
                                            Intent loginSuccess = new Intent(LoginActivity.this, PickAMeetingActivity.class);
                                            startActivity(loginSuccess);
                                        }else if(role.equals("Agent")){
                                            session.setRole("Agent");
                                            Intent loginSuccess = new Intent(LoginActivity.this, AgentHomeActivity.class);
                                            startActivity(loginSuccess);
                                        }
                                    }

                                } else {
                                    if(jsonObject.getString("message").equals("No user found"))
                                        Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(LoginActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(LoginActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(loginRequest);
                }
            }
        });

    }
}
