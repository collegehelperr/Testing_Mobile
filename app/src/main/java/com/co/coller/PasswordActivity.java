package com.co.coller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class PasswordActivity extends AppCompatActivity {

    Button btnSimpan;
    ImageButton btnBack;
    EditText txtLastPass, txtNewPass, txtConfNewPass;
    sharedPref sharedPref;
    api api;
    Dialog dialog;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        btnSimpan = (Button) findViewById(R.id.btn_simpan_pass);
        btnBack = (ImageButton) findViewById(R.id.btn_back_pass);
        txtLastPass = (EditText)findViewById(R.id.txt_lastpass);
        txtNewPass = (EditText) findViewById(R.id.txt_newpass);
        txtConfNewPass = (EditText) findViewById(R.id.txt_conf_newpass);

        sharedPref = new sharedPref(this);

        dialog = new Dialog(this);

        progressDoalog = new ProgressDialog(PasswordActivity.this);
        progressDoalog.setMessage("Updating...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordActivity.this, ProfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCheck();
            }
        });
    }

    private void showDialog() {
    }

    private void formCheck() {
        //new password
        final String newpass = txtNewPass.getText().toString();

        //data edit profil
        final String uid = sharedPref.getUid();
        final String email = sharedPref.getEmail();
        final String nama = sharedPref.getName();
        final String nohp = sharedPref.getNohp();
        final String profimg = sharedPref.getProfImg();

        if (TextUtils.isEmpty(txtLastPass.getText().toString())) {
            txtLastPass.setError("Please enter password");
            txtLastPass.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newpass)) {
            txtNewPass.setError("Please enter new password");
            txtNewPass.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(txtConfNewPass.getText().toString())) {
            txtConfNewPass.setError("Please enter password");
            txtConfNewPass.requestFocus();
            return;
        }

        if (!txtConfNewPass.getText().toString().equals(newpass)) {
            txtConfNewPass.setError("Password Does not Match");
            txtConfNewPass.requestFocus();
            return;
        }

        if(!txtLastPass.getText().toString().equals(sharedPref.getPass())){
            txtLastPass.setError("Wrong password!");
            txtLastPass.requestFocus();
            return;
        }

        dialog.setContentView(R.layout.alert_dialog_simpan);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        Button btnNo = dialog.findViewById(R.id.btn_no);

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass(uid, email, newpass, nama, nohp, profimg);
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void changePass(String uid, String email, String newpass, String nama, String nohp, String profimg) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> changePass = api.updateUser(uid, email, newpass, nama, nohp, profimg);

        progressDoalog.show();

        changePass.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.i("onSuccess", response.body().toString());
                        String jsonResponse = response.body().toString();
                        try {
                            parseChangePass(jsonResponse);
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
                Toast.makeText(PasswordActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseChangePass(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("code").equals("200")){
            progressDoalog.dismiss();
            saveInfo(response);

            Toast.makeText(PasswordActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            this.finish();
        }else{
            Toast.makeText(PasswordActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    sharedPref.setPass(userObj.getString("password"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PasswordActivity.this, ProfilActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}