package com.example.akhirtolong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {

    // Deklarasi variabel untuk TextView
    private TextView profileNama, profileEmail, profilePassword;
    private TextView titlenama, titleemail;
    private TextView logoutText;

    private ShapeableImageView profileImg;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    // Variabel untuk data profil
    private String nama, email, password, conf_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil); // Pastikan nama layout sesuai

        // Inisialisasi TextView
        titlenama = findViewById(R.id.titlenama);
        titleemail = findViewById(R.id.titleemail);
        profileNama = findViewById(R.id.profileNama);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        profileImg = findViewById(R.id.profileImg);

        // Mengambil data dari Intent
        nama = getIntent().getStringExtra("nama");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        conf_password = getIntent().getStringExtra("confpassword");

        // Menampilkan data pada TextView
        titlenama.setText(nama);
        titleemail.setText(email);
        profileNama.setText(nama);
        profileEmail.setText(email);
        profilePassword.setText(password);

        // Inisialisasi ActivityResultLauncher untuk galeri
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            profileImg.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Inisialisasi ActivityResultLauncher untuk kamera
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        profileImg.setImageBitmap(photo);
                    }
                });

        // Button Edit untuk mengedit profil
        Button editbtn = findViewById(R.id.editButton);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengirimkan data ke EditProfilActivity
                Intent intent = new Intent(ProfilActivity.this, EditprofilActivity.class);
                intent.putExtra("nama", nama); // Mengirimkan nama
                intent.putExtra("email", email); // Mengirimkan email
                intent.putExtra("password", password); // Mengirimkan password
                intent.putExtra("confpassword", conf_password);
                startActivity(intent); // Memulai EditProfilActivity
            }
        });

        // Logout Text untuk keluar
        logoutText = findViewById(R.id.logout_text);
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke MainActivity
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView arrowBack = findViewById(R.id.back);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke MainActivity
                Intent intent = new Intent(ProfilActivity.this, HomeActivity.class);
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
    }

    public void selectImage(View view) {
        // Menampilkan opsi untuk memilih gambar dari galeri atau kamera
        String[] options = {"Pilih dari Galeri", "Ambil Foto"};
        new android.app.AlertDialog.Builder(this)
                .setTitle("Ganti Foto Profil")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Galeri
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryLauncher.launch(intent);
                    } else if (which == 1) {
                        // Kamera
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraLauncher.launch(intent);
                    }
                })
                .show();
    }
}
