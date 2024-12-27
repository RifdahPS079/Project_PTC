package com.example.akhirtolong;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditprofilActivity extends AppCompatActivity {
    EditText editTextNama, editTextEmail, editTextPassword, editTextConfPass;
    FirebaseDatabase database;
    DatabaseReference reference;

    String oldEmailKey; // Email lama yang digunakan sebagai kunci di Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofil);

        // Inisialisasi EditText
        editTextNama = findViewById(R.id.editNama);
        editTextEmail = findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        editTextConfPass = findViewById(R.id.editConffPass);
        Button saveButton = findViewById(R.id.saveButton);

        // Ambil data dari Intent yang dikirim dari ProfilActivity
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        // Simpan email lama sebagai kunci
        oldEmailKey = email.replace(".", "_");

        // Set data ke EditText
        editTextNama.setText(nama);
        editTextEmail.setText(email);
        editTextPassword.setText(password);
        editTextConfPass.setText(password);

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Menyimpan perubahan ke Firebase
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedNama = editTextNama.getText().toString().trim();
                String updatedEmail = editTextEmail.getText().toString().trim();
                String updatedPassword = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfPass.getText().toString().trim();

                // Validasi input
                if (TextUtils.isEmpty(updatedNama)) {
                    editTextNama.setError("Masukkan nama");
                    editTextNama.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(updatedEmail)) {
                    editTextEmail.setError("Masukkan email");
                    editTextEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(updatedPassword)) {
                    editTextPassword.setError("Masukkan password");
                    editTextPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    editTextConfPass.setError("Masukkan konfirmasi password");
                    editTextConfPass.requestFocus();
                    return;
                }

                // Memastikan password dan konfirmasi password sama
                if (!updatedPassword.equals(confirmPassword)) {
                    editTextConfPass.setError("Kata sandi tidak cocok");
                    editTextConfPass.requestFocus();
                    return;
                }

                ImageView arrowBack = findViewById(R.id.back);
                arrowBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Kembali ke MainActivity
                        Intent intent = new Intent(EditprofilActivity.this, ProfilActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                // Membuat HelperClass untuk data yang diperbarui
                HelperClass updatedUser = new HelperClass(updatedNama, updatedEmail, updatedPassword, confirmPassword);

                // Jika email diperbarui, hapus data lama dan tambahkan data baru
                String newEmailKey = updatedEmail.replace(".", "_");
                if (!newEmailKey.equals(oldEmailKey)) {
                    // Hapus data lama
                    reference.child(oldEmailKey).removeValue();

                    // Tambahkan data baru
                    reference.child(newEmailKey).setValue(updatedUser);
                } else {
                    // Jika email tidak berubah, hanya perbarui data
                    reference.child(oldEmailKey).setValue(updatedUser);
                }

                // Menampilkan pesan sukses
                Toast.makeText(EditprofilActivity.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();

                // Kembali ke ProfilActivity setelah menyimpan
                Intent intentToProfile = new Intent(EditprofilActivity.this, ProfilActivity.class);
                intentToProfile.putExtra("nama", updatedNama);
                intentToProfile.putExtra("email", updatedEmail);
                intentToProfile.putExtra("password", updatedPassword);
                startActivity(intentToProfile);
                finish();
            }
        });
    }
}