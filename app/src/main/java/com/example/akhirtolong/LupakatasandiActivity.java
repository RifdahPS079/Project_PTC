package com.example.akhirtolong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LupakatasandiActivity extends AppCompatActivity {
    private EditText Email, KataSandiBaru, KonfirmasiKataSandi;
    private Button btnResetKataSandi;
    private TextView tvKembaliLogin;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupakatasandi);

        Email = findViewById(R.id.emaillupasandi);
        KataSandiBaru = findViewById(R.id.katasandibaru);
        KonfirmasiKataSandi = findViewById(R.id.konfirmasikatasandibaru);
        btnResetKataSandi = findViewById(R.id.btn_reset_kata_sandi);
        tvKembaliLogin = findViewById(R.id.link_kembali_login);

        // Inisialisasi Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        btnResetKataSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim().replace(".", "_");
                String kataSandiBaru = KataSandiBaru.getText().toString().trim();
                String konfirmasiKataSandi = KonfirmasiKataSandi.getText().toString().trim();

                if (email.isEmpty() || kataSandiBaru.isEmpty() || konfirmasiKataSandi.isEmpty()) {
                    Toast.makeText(LupakatasandiActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!kataSandiBaru.equals(konfirmasiKataSandi)) {
                    Toast.makeText(LupakatasandiActivity.this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show();
                    return;
                }

                cekEmailDanGantiSandi(email, kataSandiBaru);
            }
        });

        tvKembaliLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kembaliKeLogin();
            }
        });
    }

    private void cekEmailDanGantiSandi(String email, String kataSandiBaru) {
        databaseReference.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email ditemukan, update password dan confpassword
                    HashMap<String, Object> updateData = new HashMap<>();
                    updateData.put("password", kataSandiBaru); // Selalu update password

                    // Periksa apakah confpassword ada sebelum memperbaruinya
                    if (dataSnapshot.hasChild("confpassword")) {
                        updateData.put("confpassword", kataSandiBaru);
                    }

                    // Perbarui data di Firebase
                    dataSnapshot.getRef().updateChildren(updateData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LupakatasandiActivity.this, "Kata sandi berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                            kembaliKeLogin();
                        } else {
                            Toast.makeText(LupakatasandiActivity.this, "Gagal memperbarui kata sandi", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Email tidak ditemukan
                    Toast.makeText(LupakatasandiActivity.this, "Email tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LupakatasandiActivity.this, "Terjadi kesalahan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void kembaliKeLogin() {
        Intent intent = new Intent(LupakatasandiActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}