package com.example.hp.myapplication.Request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest extends StringRequest {

    private static final String LOGIN_URL = "update_profile.php";
    private Map<String, String> parameters;
    private static final Ip ip = new Ip();


    public UpdateRequest(String user_id, String password, String firstName, String lastName, String userName, String region, String agence_id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ip.getIp()+""+LOGIN_URL, listener, errorListener);
        Log.i("Login Response", ip.getIp()+""+LOGIN_URL+"?user_id="+user_id+"&password="+password+"&firstName="+firstName+"&lastName="+lastName+"&userName="+userName+"&region="+region+"&agence_id="+agence_id);

        parameters = new HashMap<>();
        parameters.put("user_id", user_id);
        parameters.put("password", password);
        parameters.put("firstName", firstName);
        parameters.put("lastName", lastName);
        parameters.put("userName", userName);
        parameters.put("region", region);
        parameters.put("agence_id", agence_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
