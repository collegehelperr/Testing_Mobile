package com.co.coller;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.co.coller.api.FileUtils;
import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.util.FileUriUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfilActivity extends AppCompatActivity {

    ImageButton btnBack;
    CircleImageView fotoProf;
    Button btnSimpan;
    EditText txtNama, txtEmail, txtNohp;
    sharedPref sharedPref;
    api api;
    ProgressDialog progressDoalog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        btnBack = (ImageButton) findViewById(R.id.btn_back_edit);
        fotoProf = (CircleImageView) findViewById(R.id.foto_profil_edit);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);
        txtNama = (EditText) findViewById(R.id.txt_edit_nama);
        txtEmail = (EditText) findViewById(R.id.txt_edit_email);
        txtNohp = (EditText) findViewById(R.id.txt_edit_nohp);

        sharedPref = new sharedPref(this);
        txtNama.setText(sharedPref.getName());
        txtEmail.setText(sharedPref.getEmail());
        txtNohp.setText(sharedPref.getNohp());

        String urlImg = "https://3907-20-119-63-129.ngrok.io" + sharedPref.getProfImg();
        Log.i("Url image", urlImg);

        Glide.with(this).load(urlImg).into(fotoProf);

        dialog = new Dialog(this);

        // Set up progress before call
        progressDoalog = new ProgressDialog(EditProfilActivity.this);
        progressDoalog.setMessage("Updating...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfilActivity.this, ProfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //formCheck();
                showDialog();
            }
        });

        fotoProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditProfilActivity.this)
                        .cropSquare()	//Crop square image, its same as crop(1f, 1f)
                        .start();
                }
        });
    }

    private void showDialog() {
        dialog.setContentView(R.layout.alert_dialog_simpan);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        Button btnNo = dialog.findViewById(R.id.btn_no);

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCheck();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        Glide.with(this).load(uri).into(fotoProf);
        String selectedImage = FileUtils.getPath(EditProfilActivity.this, uri);

        progressDoalog.show();
        upFile(selectedImage);
        updateProfLink(selectedImage);
    }

    private void upFile(String selectedImage) {
        File file = new File(Uri.parse(selectedImage).getPath());
        Log.i("File",""+file.getName());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/*"),file);
        MultipartBody.Part profImage = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        api = apiClient.getClient().create(api.class);
        Call<JsonObject> upProf = api.profImg(profImage, filename);

        upProf.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.i("onSuccess", response.body().toString());
                        String jsonResponse = response.body().toString();
                        try {
                            parseUpProf(jsonResponse);
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
                Toast.makeText(EditProfilActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseUpProf(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("code").equals("200")){
            progressDoalog.dismiss();
            Toast.makeText(EditProfilActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        } else if (jsonObject.optString("code").equals("404")){
            Toast.makeText(EditProfilActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(EditProfilActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfLink(String linkImg) {
        File file = new File(Uri.parse(linkImg).getPath());

        final String uid = sharedPref.getUid();
        final String email = sharedPref.getEmail();
        final String password = sharedPref.getPass();
        final String nama = sharedPref.getName();
        final String nohp = sharedPref.getNohp();
        final String profimg = "/img/profil/" + file.getName().toString();

        UpdateUser(uid, email, password, nama, nohp, profimg);
    }

    private void formCheck() {
        final String uid = sharedPref.getUid();
        final String email = txtEmail.getText().toString();
        final String password = sharedPref.getPass();
        final String nama = txtNama.getText().toString();
        final String nohp = txtNohp.getText().toString();
        final String profimg = sharedPref.getProfImg();

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Please enter email");
            txtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Enter a valid email");
            txtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nama)) {
            txtNama.setError("Please enter your name");
            txtNama.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nohp)) {
            txtNohp.setError("Please enter your number");
            txtNohp.requestFocus();
            return;
        }

        UpdateUser(uid, email, password, nama, nohp, profimg);
    }

    private void UpdateUser(String uid, String email, String password, String nama, String nohp, String profimg) {
        api = apiClient.getClient().create(api.class);
        Call<JsonObject> updateUser = api.updateUser(uid, email, password, nama, nohp, profimg);

        progressDoalog.show();

        updateUser.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.i("onSuccess", response.body().toString());
                        String jsonResponse = response.body().toString();
                        try {
                            parseUpdateData(jsonResponse);
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
                Toast.makeText(EditProfilActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseUpdateData(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("code").equals("200")){
            progressDoalog.dismiss();

            saveInfo(response);

            Toast.makeText(EditProfilActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(RegisterActivity.this,DashboardActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            this.finish();
        } else if (jsonObject.optString("code").equals("401")){
            txtEmail.setError("Email telah digunakan!");
            txtEmail.requestFocus();
        } else if (jsonObject.optString("code").equals("402")){
            txtNohp.setError("Nomor HP telah digunakan!");
            txtNohp.requestFocus();
        }else{
            Toast.makeText(EditProfilActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProfilActivity.this, ProfilActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}