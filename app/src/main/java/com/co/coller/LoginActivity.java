package com.co.coller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edEmail, edPass;
    api api;
    sharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = new sharedPref(this);

        btnLogin = (Button) findViewById(R.id.btn_login);
        edEmail = (EditText) findViewById(R.id.txt_email_login);
        edPass = (EditText) findViewById(R.id.txt_pass_login);

        String email = edEmail.getText().toString();
        String password = edPass.getText().toString();

        //button register
        Button toReg = (Button) findViewById(R.id.btn_daftar);

        toReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCheck();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void formCheck() {
        final String email = edEmail.getText().toString();
        final String password = edPass.getText().toString();

        //checking if email is empty
        if (TextUtils.isEmpty(email)) {
            edEmail.setError("Please enter your email");
            edEmail.requestFocus();
            btnLogin.setEnabled(true);
            return;
        }
        //checking if password is empty
        if (TextUtils.isEmpty(password)) {
            edPass.setError("Please enter your password");
            edPass.requestFocus();
            btnLogin.setEnabled(true);
            return;
        }

        mainLogin(email,password);
    }

    private void mainLogin(String email, String password) {
        api = apiClient.getClient().create(api.class);

        Call<JsonObject> login = api.postLogin(email, password);

        login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());

                if(response.isSuccessful()){
                    if (response.body() != null){
                        Log.i("onSucces", response.body().toString());

                        String jsonResponse = response.body().toString();
                        parseLoginData(jsonResponse);
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseLoginData(String response) {
        try {
            JSONObject jObj = new JSONObject(response);
            if (jObj.getString("code").equals("200")){
                saveInfo(response);

                Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();
            } else if (jObj.getString("code").equals("401")){
                edPass.setError("Password salah!");
                edPass.requestFocus();
            } else if (jObj.getString("code").equals("404")){
                edEmail.setText("");
                edPass.setText("");
                edEmail.setError("email tidak terdaftar!\nSilahkan registrasi terlebih dahulu.");
                edEmail.requestFocus();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveInfo(String response){
        sharedPref.putIsLoggin(true);
        try {
            JSONObject jObj = new JSONObject(response);
            if (jObj.getString("code").equals("200")){
                JSONArray jArray = jObj.getJSONArray("data");
                for (int i = 0; i < jArray.length(); i++){
                    JSONObject userObj = jArray.getJSONObject(i);
                    sharedPref.setUid(userObj.getString("uid"));
                    sharedPref.setEmail(userObj.getString("email"));
                    sharedPref.setPass(userObj.getString("password"));
                    sharedPref.setName(userObj.getString("nama_lengkap"));
                    sharedPref.setNohp(userObj.getString("no_hp"));
                    sharedPref.setProfImg(userObj.getString("profile_img"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}