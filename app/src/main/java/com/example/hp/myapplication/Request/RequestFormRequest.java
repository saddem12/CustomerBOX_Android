package com.example.hp.myapplication.Request;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestFormRequest extends StringRequest {

    private static final String URL = "meetings.php";
    private Map<String, String> parameters;
    private static final Ip ip = new Ip();


    public RequestFormRequest(String title, String details, String id_customer, String id_agent,  Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ip.getIp()+""+URL, listener, errorListener);

        Log.i("Login Response", ip.getIp()+""+URL+"?title="+title+"&details="+details+"&id_customer="+id_customer+"&id_agent="+id_agent);


        parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("details", details);
        parameters.put("id_customer", id_customer);
        parameters.put("id_agent", id_agent);



    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
