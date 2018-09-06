package com.example.hp.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.SupportActivity;

public class Session {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }



    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public boolean getLoggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }


    public void setRole(String role){
        editor.putString("role",role);
        editor.commit();
    }

    public String getRole(){
        return prefs.getString("role","");
    }


    public void setUserId(String id){
        editor.putString("id",id);
        editor.commit();
    }

    public String getUserId(){
        return prefs.getString("id","");
    }

    public void setAgenceId(String id){
        editor.putString("agence_id",id);
        editor.commit();
    }

    public String getAgenceId(){
        return prefs.getString("agence_id","");
    }

}
