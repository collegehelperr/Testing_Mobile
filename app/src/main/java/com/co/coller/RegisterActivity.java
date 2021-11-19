package com.co.coller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    EditText edEmail, edPass, edConfPass, edNama, edNoHp;
    api api;
    sharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPref = new sharedPref(this);

        btnRegister = (Button) findViewById(R.id.btn_register);
        edEmail = (EditText) findViewById(R.id.txt_email_reg);
        edPass = (EditText) findViewById(R.id.txt_pass_reg);
        edConfPass = (EditText) findViewById(R.id.txt_confpass_reg);
        edNama = (EditText) findViewById(R.id.txt_nama_reg);
        edNoHp = (EditText) findViewById(R.id.txt_notelp_reg);
        Button toLogin = (Button) findViewById(R.id.btn_login);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCheck();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void formCheck() {
        final String email = edEmail.getText().toString();
        final String pass = edPass.getText().toString();
        final String confPass = edConfPass.getText().toString();
        final String nama = edNama.getText().toString();
        final String nohp = edNoHp.getText().toString();

        if (TextUtils.isEmpty(email)) {
            edEmail.setError("Please enter email");
            edEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Enter a valid email");
            edEmail.requestFocus();
            return;
        }
        //checking if email is empty
        if (TextUtils.isEmpty(pass)) {
            edPass.setError("Please enter password");
            edPass.requestFocus();
            return;
        }
        //checking if password is empty
        if (TextUtils.isEmpty(confPass)) {
            edConfPass.setError("Please enter password");
            edConfPass.requestFocus();
            return;
        }
        //validating email

        if (!confPass.equals(pass)) {
            edConfPass.setError("Password Does not Match");
            edConfPass.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nama)) {
            edNama.setError("Please enter your name");
            edNama.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nohp)) {
            edNoHp.setError("Please enter your phone number");
            edNoHp.requestFocus();
            return;
        }

        regUser(email, pass, nama, nohp);
    }

    private void regUser(String email, String pass, String nama, String nohp) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> register = api.register(email, pass, nama, nohp);

        register.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.i("onSuccess", response.body().toString());
                        String jsonResponse = response.body().toString();
                        try {
                            parseRegData(jsonResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void parseRegData(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("code").equals("200")){

            saveInfo(response);

            Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        } else if (jsonObject.optString("code").equals("401")){
            edEmail.setError("Email telah terdaftar!");
            edEmail.requestFocus();
        } else if (jsonObject.optString("code").equals("402")){
            edNoHp.setError("Nomor HP telah terdaftar!");
            edNoHp.requestFocus();
        }else{
            Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveInfo(String response) {
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