package com.example.akhirtolong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class B3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_b3);

        ImageView arrowBack = findViewById(R.id.back);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke MainActivity
                Intent intent = new Intent(B3Activity.this, BukuActivity.class);
                String nama = getIntent().getStringExtra("nama");
                String email = getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");
                String confPass = getIntent().getStringExtra("confpassword");
                intent.putExtra("nama", nama);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("confpassword", confPass);
                startActivity(intent);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}