package com.co.coller.api;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedPref {

    private final String UID = "uid";
    private final String EMAIL = "email";
    private final String PASS = "password";
    private final String NAMA = "nama_legnkap";
    private final String NOHP = "no_hp";
    private final String FOTO = "profile_img";

    private final String INTRO = "intro";
    private SharedPreferences sPref;
    private Context context;

    public sharedPref(Context context){
        sPref = context.getSharedPreferences("coller", Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLoggin(boolean isloggedin){
        SharedPreferences.Editor edit = sPref.edit();
        edit.putBoolean(INTRO, isloggedin);
        edit.commit();
    }

    public boolean getIsLoggin(){
        return sPref.getBoolean(INTRO, false);
    }

    public void setUid(String isLoggedin){
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(UID, isLoggedin);
        edit.commit();
    }

    public void setEmail(String isLoggedin){
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(EMAIL, isLoggedin);
        edit.commit();
    }

    public void setPass(String isLoggedin){
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(PASS, isLoggedin);
        edit.commit();
    }

    public void setName(String isLoggedin){
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(NAMA, isLoggedin);
        edit.commit();
    }

    public void setNohp(String isLoggedin){
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(NOHP, isLoggedin);
        edit.commit();
    }

    public void setProfImg(String isLoggedin){
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString(FOTO, isLoggedin);
        edit.commit();
    }

    public String getUid(){
        return sPref.getString(UID, "");
    }

    public String getEmail(){
        return sPref.getString(EMAIL, "");
    }

    public String getPass(){
        return sPref.getString(PASS, "");
    }

    public String getName(){
        return sPref.getString(NAMA, "");
    }

    public String getNohp(){
        return sPref.getString(NOHP, "");
    }

    public String getProfImg(){
        return sPref.getString(FOTO, "");
    }

}
