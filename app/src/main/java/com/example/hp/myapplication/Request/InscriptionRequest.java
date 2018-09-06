package com.example.hp.myapplication.Request;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InscriptionRequest extends StringRequest {

    private static final String URL = "add.php";
    private Map<String, String> parameters;
    private static final Ip ip = new Ip();


    public InscriptionRequest(String email, String password, String firstname, String lastname, String region, String agence_id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ip.getIp()+""+URL, listener, errorListener);

        /*Log.i("Login Response", ip.getIp()+""+URL+"?email="+email+"&password="+password+"&firstname="+firstname+"&lastname="+lastname+"&region="+region+"&agence_id="+agence_id);*/
        Log.i("Login Response", ip.getIp()+""+URL+"?firstname="+firstname+"&lastname="+lastname+"&password="+password+"&email="+email+"&region="+region+"&agence_id="+agence_id);

        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", password);
        parameters.put("firstname", firstname);
        parameters.put("lastname", lastname);
        parameters.put("region", region);
        parameters.put("agence_id", agence_id);



    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
