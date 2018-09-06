package com.example.hp.myapplication.Request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ConfirmedCalenderRequest extends StringRequest {
    private static final String LOGIN_URL = "MeetingCalendar.php";
    private Map<String, String> parameters;
    private static final Ip ip = new Ip();


    public ConfirmedCalenderRequest(String agent_id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ip.getIp()+""+LOGIN_URL, listener, errorListener);

        Log.i("Login Response", ip.getIp()+""+LOGIN_URL+"?agent_id="+agent_id);
        parameters = new HashMap<>();
        parameters.put("agent_id", agent_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }}
