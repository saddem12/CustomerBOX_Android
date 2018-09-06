package com.example.hp.myapplication.Request;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PickAMeetingRequest extends StringRequest {

    private static final String LOGIN_URL = "agents_lv.php";
    private Map<String, String> parameters;
    private static final Ip ip = new Ip();


    public PickAMeetingRequest(String agence_id,  Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ip.getIp()+""+LOGIN_URL, listener, errorListener);

        Log.i("Login Response", ip.getIp()+""+LOGIN_URL+"?agence_id="+agence_id);
        parameters = new HashMap<>();
        parameters.put("agence_id", agence_id);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
