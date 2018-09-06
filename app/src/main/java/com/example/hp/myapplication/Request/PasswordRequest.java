package com.example.hp.myapplication.Request;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PasswordRequest extends StringRequest {

    private static final String LOGIN_URL = "password.php";
    private Map<String, String> parameters;
    private static final Ip ip = new Ip();


    public PasswordRequest(String user_id, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ip.getIp()+""+LOGIN_URL, listener, errorListener);
        Log.i("Login Response", ip.getIp()+""+LOGIN_URL+"?user_id="+user_id+"&password="+password);

        parameters = new HashMap<>();
        parameters.put("user_id", user_id);
        parameters.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
