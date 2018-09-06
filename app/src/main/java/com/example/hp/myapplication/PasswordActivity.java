package com.example.hp.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Request.InscriptionRequest;
import com.example.hp.myapplication.Request.PasswordRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordActivity extends AppCompatActivity {


    EditText  pass, confpass;
    Button ok;
    Session session;
    String  password, confirmpassword;
    RequestQueue requestQueue;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        session = new Session(this);
        pass = (EditText) findViewById(R.id.ChPass);
        confpass = (EditText) findViewById(R.id.ConPass);
        ok = (Button) findViewById(R.id.btnPass);

        requestQueue = Volley.newRequestQueue(PasswordActivity.this);//Creating the RequestQueue

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                password = pass.getText().toString();
                confirmpassword = confpass.getText().toString();

                if( password.equals("") || confirmpassword.equals("") ){
                    Toast.makeText(PasswordActivity.this, "Please make sure to fill all your credentials!", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirmpassword)){
                    Toast.makeText(PasswordActivity.this, "Your password doesn't match!", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                    confpass.setText("");
                }else if(password.length()<8) {
                    Toast.makeText(PasswordActivity.this, "Your password lenght must be 8 characters at least!", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                    confpass.setText("");
                }else if(!password.matches(".*([a-zA-Z].*[0-9]|[0-9].*[a-zA-Z]).*") ) {
                    Toast.makeText(PasswordActivity.this, "Your password must contains characters and numbers!", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                    confpass.setText("");
                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(PasswordActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Updating Password ");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    PasswordRequest passwordRequest = new PasswordRequest(session.getUserId(), password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("********************", response);
                            progressDialog.dismiss();
                            // Response from the server is in the form if a JSON, so we need a JSON Object
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                success = jsonObject.getInt("success");
                                if (success==1) {
                                    Intent loginSuccess = new Intent(PasswordActivity.this, LogAsActivity.class);
                                    startActivity(loginSuccess);

                                } else {
                                    if(jsonObject.getString("message").equals("No user found"))
                                        Toast.makeText(PasswordActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PasswordActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(PasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(PasswordActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(PasswordActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(passwordRequest);
                }
            }
        });
    }
}
