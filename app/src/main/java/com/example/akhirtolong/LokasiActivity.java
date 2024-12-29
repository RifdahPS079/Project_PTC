package com.example.akhirtolong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class LokasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengatur layout untuk menyesuaikan sistem bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_lokasi);

        // Ambil data nama pengguna dari Intent


        // Tombol kembali
        ImageView arrowBack = findViewById(R.id.back);
        arrowBack.setOnClickListener(view -> {
            // Kembali ke HomeActivity dengan membawa data nama pengguna
            Intent intent = new Intent(LokasiActivity.this, HomeActivity.class);
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
        });

        // Menyesuaikan padding untuk elemen utama dengan sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });
    }
}
