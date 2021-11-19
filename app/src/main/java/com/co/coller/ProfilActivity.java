package com.co.coller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.co.coller.api.sharedPref;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {

    sharedPref sharedPref;
    TextView tvNama, tvEmail;
    ImageButton btnHome, btnBack;
    Button btnLogout, btnEditProfil;
    CircleImageView fotoProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        tvNama = (TextView) findViewById(R.id.tvNama_profil);
        tvEmail = (TextView) findViewById(R.id.tvEmail_profil);
        btnHome = (ImageButton)findViewById(R.id.btn_home_prof);
        btnBack = (ImageButton) findViewById(R.id.btn_back_profil);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnEditProfil = (Button) findViewById(R.id.btn_editprof);
        fotoProf = (CircleImageView) findViewById(R.id.foto_profil);

        sharedPref = new sharedPref(this);
        tvNama.setText(sharedPref.getName());
        tvEmail.setText(sharedPref.getEmail());

        String urlImg = "https://b182-20-119-63-129.ngrok.io" + sharedPref.getProfImg();
        Log.i("Url image", urlImg);

        Glide.with(this).load(urlImg).into(fotoProf);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilActivity.this, EditProfilActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.putIsLoggin(false);
                Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                ProfilActivity.this.finish();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfilActivity.this, DashboardActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}