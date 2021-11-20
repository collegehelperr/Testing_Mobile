package com.co.coller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.co.coller.api.sharedPref;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    ImageButton btnProfil;
    TextView tvNama;
    sharedPref sharedPref;
    CircleImageView fotoProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPref = new sharedPref(this);

        tvNama = (TextView) findViewById(R.id.tv_nama);
        btnProfil = (ImageButton) findViewById(R.id.btn_prof);
        fotoProf = (CircleImageView) findViewById(R.id.foto_profil_dashboard);

        tvNama.setText("Hai, " + sharedPref.getName());

        String urlImg = "https://3907-20-119-63-129.ngrok.io" + sharedPref.getProfImg();
        Log.i("Url image", urlImg);

        Glide.with(this).load(urlImg).into(fotoProf);

        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, ProfilActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}