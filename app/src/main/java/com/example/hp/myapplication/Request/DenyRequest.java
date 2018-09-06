package com.example.hp.myapplication.Request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DenyRequest extends StringRequest {
    private static final String LOGIN_URL = "denyRequest.php";
    private Map<String, String> parameters;
    private static final Ip ip = new Ip();


    public DenyRequest(String meeting_id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, ip.getIp()+""+LOGIN_URL, listener, errorListener);

        Log.i("Login Response", ip.getIp()+""+LOGIN_URL+"?meetings_id="+meeting_id);
        parameters = new HashMap<>();
        parameters.put("meetings_id", meeting_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }}
