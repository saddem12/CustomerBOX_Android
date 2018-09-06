package com.example.hp.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hp.myapplication.Request.AgencesRequest;
import com.example.hp.myapplication.Request.UpdateRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {

    String firstname, lastname, email, password, confirmpassword, region, idAG;

    EditText fname, lname, mail, pass, confpass;
    Button ok, cancel;
    Spinner regSpinner, agenceSpinner;
    List<String> regionList;
    Session session;
    ArrayList<String> agencesAdList,agencesIdList;
    JSONArray agences = null;


    RequestQueue requestQueue;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        session = new Session(this);

        fname = (EditText) findViewById(R.id.ufname);
        lname = (EditText) findViewById(R.id.ulname);
        mail = (EditText) findViewById(R.id.umail);
        pass = (EditText) findViewById(R.id.upass);
        confpass = (EditText) findViewById(R.id.uconfpass);

        regSpinner = (Spinner) findViewById(R.id.ureg);
        agenceSpinner = (Spinner) findViewById(R.id.uagence);

        cancel = (Button) findViewById(R.id.ucancel);
        ok = (Button) findViewById(R.id.update);

        regionList = new ArrayList<String>();

        regionList.add("Select your region");
        regionList.add("Ariana");
        regionList.add("Baja");
        regionList.add("Ben Arous");
        regionList.add("Bizerte");
        regionList.add("Gabès");
        regionList.add("Gafsa");
        regionList.add("Jendouba");
        regionList.add("Kairouan");
        regionList.add("Kasserine");
        regionList.add("Kebili");
        regionList.add("Kef");
        regionList.add("Mahdia");
        regionList.add("Manouba");
        regionList.add("Medenine");
        regionList.add("Monastir");
        regionList.add("Nabeul");
        regionList.add("Sfax");
        regionList.add("Sidi Bouzid");
        regionList.add("Siliana");
        regionList.add("Sousse");
        regionList.add("Tataouine");
        regionList.add("Tozeur");
        regionList.add("Tunis");
        regionList.add("Zaghouan");


        requestQueue = Volley.newRequestQueue(UpdateActivity.this);//Creating the RequestQueue

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regionList);
        regSpinner.setAdapter(dataAdapter);


        agencesIdList = new ArrayList<String>();
        agencesAdList = new ArrayList<String>();
        agencesAdList.add("Choose Agence");
        agencesIdList.add("0");


        regSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                region = regSpinner.getSelectedItem().toString();

                AgencesRequest agencesRequest = new AgencesRequest(region, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Login Response", response);
                        // Response from the server is in the form if a JSON, so we need a JSON Object
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            success = jsonObject.getInt("success");
                            if (success==1) {

                                agences = jsonObject.getJSONArray("agences");
                                for (int i = 0; i < agences.length(); i++) {
                                    JSONObject c = agences.getJSONObject(i);

                                    String ida = c.getString("id");
                                    String name = c.getString("name");

                                    agencesIdList.add(ida);
                                    agencesAdList.add(name);

                                }

                                ArrayAdapter adapterr = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, agencesAdList);
                                agenceSpinner.setAdapter(adapterr);

                            } else {
                                if(jsonObject.getString("message").equals("No user found"))
                                    Toast.makeText(UpdateActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UpdateActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof ServerError)
                            Toast.makeText(UpdateActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        else if (error instanceof TimeoutError)
                            Toast.makeText(UpdateActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(UpdateActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(agencesRequest);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        agenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                idAG = agencesIdList.get(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                fname.setText("");
                lname.setText("");
                mail.setText("");
                pass.setText("");
                confpass.setText("");
                regSpinner.setSelection(0);
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                firstname = fname.getText().toString();
                lastname = lname.getText().toString();
                email = mail.getText().toString();
                password = pass.getText().toString();
                confirmpassword = confpass.getText().toString();
                region = regSpinner.getSelectedItem().toString();

                if(firstname.equals("") || lastname.equals("") || email.equals("") || password.equals("") || confirmpassword.equals("")
                        || region.equals("Select your region") || idAG.equals("0") ){
                    Toast.makeText(UpdateActivity.this, "Please make sure to fill all your credentials!", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirmpassword)){
                    Toast.makeText(UpdateActivity.this, "Your password doesn't match!", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                    confpass.setText("");
                }else if(password.length()<8) {
                    Toast.makeText(UpdateActivity.this, "Your password lenght must be 8 characters at least!", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                    confpass.setText("");
                }else if(!isValidEmail(email)) {
                    Toast.makeText(UpdateActivity.this, "Please enter a valid E-mail!", Toast.LENGTH_SHORT).show();
                    mail.setText("");
                }else if(!password.matches(".*([a-zA-Z].*[0-9]|[0-9].*[a-zA-Z]).*") ) {
                    Toast.makeText(UpdateActivity.this, "Your password must contains characters and numbers!", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                    confpass.setText("");
                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(UpdateActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Inscription ");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
/*
                    UpdateRequest updateRequest = new UpdateRequest(firstname, password, lastname, email, region, idAG,  new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("********************", response);
                            progressDialog.dismiss();
                            // Response from the server is in the form if a JSON, so we need a JSON Object
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                success = jsonObject.getInt("success");
                                if (success==1) {
                                    Intent loginSuccess = new Intent(UpdateActivity.this, LogAsActivity.class);
                                    startActivity(loginSuccess);

                                } else {
                                    if(jsonObject.getString("message").equals("No user found"))
                                        Toast.makeText(UpdateActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(UpdateActivity.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(UpdateActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(UpdateActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(UpdateActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(updateRequest);*/
                }
            }
        });
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
