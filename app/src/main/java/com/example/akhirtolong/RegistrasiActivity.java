package com.example.akhirtolong;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrasiActivity extends AppCompatActivity {

    EditText editTextnama, editTextemail, editTextpassword, editTextConf_Password;
    FirebaseDatabase database;
    DatabaseReference referencee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        // Inisialisasi EditText
        editTextnama = findViewById(R.id.editTeksnama);
        editTextemail = findViewById(R.id.editTeksemail);
        editTextpassword = findViewById(R.id.editTekspass);
        editTextConf_Password = findViewById(R.id.editTeksconf_pass);

        // Tombol Daftar
        Button btnDaftar = findViewById(R.id.btn_daftar_regis);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = editTextnama.getText().toString().trim();
                String email = editTextemail.getText().toString().trim();
                String password = editTextpassword.getText().toString().trim();
                String conf_password = editTextConf_Password.getText().toString().trim();

                // Validasi input
                if (TextUtils.isEmpty(nama)) {
                    editTextnama.setError("Masukkan nama");
                    editTextnama.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    editTextemail.setError("Masukkan email");
                    editTextemail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextpassword.setError("Masukkan password");
                    editTextpassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(conf_password)) {
                    editTextConf_Password.setError("Masukkan konfirmasi password");
                    editTextConf_Password.requestFocus();
                    return;
                }

                if (!password.equals(conf_password)) {
                    editTextConf_Password.setError("Kata sandi tidak cocok");
                    editTextConf_Password.requestFocus();
                    return;
                }

                // Debugging: print nilai password dan konfirmasi password
                Log.d("Registrasi", "Password: " + password);
                Log.d("Registrasi", "Konfirmasi Password: " + conf_password);

                // Inisialisasi Firebase
                database = FirebaseDatabase.getInstance();
                referencee = database.getReference("users");

                // Menyimpan data user ke Firebase dengan struktur yang sesuai
                DatabaseReference userRef = referencee.child(email.replace(".", "_"));
                userRef.child("nama").setValue(nama);
                userRef.child("email").setValue(email);
                userRef.child("password").setValue(password);
                userRef.child("confpassword").setValue(conf_password);

                // Menampilkan pesan sukses dan berpindah ke LoginActivity
                Toast.makeText(RegistrasiActivity.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrasiActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Link Masuk akan membawa ke LoginActivity
        TextView linkMasuk = findViewById(R.id.link_login);
        linkMasuk.setOnClickListener(view -> {
            // Berpindah ke LoginActivity
            Intent intent = new Intent(RegistrasiActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Ikon panah ke belakang akan kembali ke MainActivity
        ImageView arrowBack = findViewById(R.id.back);
        arrowBack.setOnClickListener(view -> {
            // Kembali ke MainActivity
            Intent intent = new Intent(RegistrasiActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}