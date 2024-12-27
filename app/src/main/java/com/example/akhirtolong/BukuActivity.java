package com.example.akhirtolong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BukuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buku);

        ImageView arrowBack = findViewById(R.id.back);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke MainActivity
                Intent intent = new Intent(BukuActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnOrganik = findViewById(R.id.btn_organik);
        btnOrganik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BukuActivity.this, OrganikActivity.class);
                startActivity(intent);
            }
        });

        Button btnAnorganik = findViewById(R.id.btn_Anorganik);
        btnAnorganik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BukuActivity.this, AnorganikActivity.class);
                startActivity(intent);
            }
        });

        Button btnB3 = findViewById(R.id.btn_B3);
        btnB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BukuActivity.this, B3Activity.class);
                startActivity(intent);
            }
        });

        Button btnDaur= findViewById(R.id.btn_Daur);
        btnDaur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BukuActivity.this, DaurulangActivity.class);
                startActivity(intent);
            }
        });

        Button btnElektronik = findViewById(R.id.btn_Elektronik);
        btnElektronik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BukuActivity.this, ElektronikActivity.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}