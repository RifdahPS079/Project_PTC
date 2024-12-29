package com.example.akhirtolong;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText loginemail, loginpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginemail = findViewById(R.id.email);
        loginpass = findViewById(R.id.pass);
        Button btn_masuk = findViewById(R.id.btn_masuk);

        btn_masuk.setOnClickListener(view -> {
            String email, pass;
            email = String.valueOf(loginemail.getText());
            pass = String.valueOf(loginpass.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Masukkan Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(LoginActivity.this, "Masukkan Kata Sandi", Toast.LENGTH_SHORT).show();
                return;
            }

            checkUser(email, pass);
        });

        ImageView arrowBack = findViewById(R.id.back);
        arrowBack.setOnClickListener(view -> {
            // Kembali ke MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        TextView linkDaftar = findViewById(R.id.Link_daftar);
        linkDaftar.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrasiActivity.class);
            startActivity(intent);
        });

        TextView linklupakatasandi = findViewById(R.id.linklupakatasandi);
        linklupakatasandi.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, LupakatasandiActivity.class);
            startActivity(intent);
        });
    }

    private void checkUser(String userEmail, String userPassword) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        // Cek apakah email sesuai dengan key di Firebase
        reference.child(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginemail.setError(null);  // Menghapus error jika email ditemukan

                    // Ambil data dari Firebase
                    String passFromDB = snapshot.child("password").getValue(String.class);
                    String namaFromDB = snapshot.child("nama").getValue(String.class);
                    String confpassFromDB = snapshot.child("confirmasi password").getValue(String.class);

                    // Cek apakah password cocok
                    if (userPassword.equals(passFromDB)) {
                        // Jika password cocok, lanjut ke HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("nama", namaFromDB);
                        intent.putExtra("email", userEmail); // Menggunakan email dari input pengguna
                        intent.putExtra("password", passFromDB);
                        intent.putExtra("confirmasi password", confpassFromDB);
                        startActivity(intent);
                    } else {
                        loginpass.setError("Kata sandi salah");
                        loginpass.requestFocus();
                    }
                } else {
                    loginemail.setError("Email tidak ditemukan");
                    loginemail.requestFocus();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error jika query dibatalkan
            }
        });
    }
}