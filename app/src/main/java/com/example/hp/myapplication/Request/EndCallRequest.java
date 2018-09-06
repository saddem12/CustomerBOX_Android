package com.example.hp.myapplication.Request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EndCallRequest extends StringRequest {

    private static final String LOGIN_URL = "EndCall.php";
    private Map<String, String> parameters;
    private static final Ip ip = new Ip();

    public EndCallRequest(String meetings_id, String duration, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, ip.getIp()+""+LOGIN_URL, listener, errorListener);

        Log.i("Login Response", ip.getIp()+""+LOGIN_URL+"?meetings_id="+meetings_id+"&duration="+duration);
        parameters = new HashMap<>();
        parameters.put("meetings_id", meetings_id);
        parameters.put("duration", duration);



}
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
