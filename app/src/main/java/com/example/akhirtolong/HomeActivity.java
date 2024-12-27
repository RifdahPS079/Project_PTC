package com.example.akhirtolong;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.akhirtolong.Models.NewsResponse;
import com.example.akhirtolong.adapters.NewsAdapter;
import com.example.akhirtolong.api.ApiClient;
import com.example.akhirtolong.api.ApiInterface;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String API_KEY = "4204495a62d94c24ab09a19df13abbf6"; // Ganti dengan API Key Anda
    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    LinearProgressIndicator progressIndicator;
    TextView namaExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView_berita);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressIndicator = findViewById(R.id.progress_indicator);
        namaExtra = findViewById(R.id.nama_extra);
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");

        if (nama != null && !nama.isEmpty()) {
            namaExtra.setText(" " + nama);
        } else {

            Toast.makeText(this, "Nama tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        // Fetch news
        fetchNews();

        // Setup button actions
        findViewById(R.id.akun).setOnClickListener(v -> openProfileActivity());
        findViewById(R.id.notif).setOnClickListener(v -> openNotifActivity());
        findViewById(R.id.buku).setOnClickListener(v -> openBukuActivity());
        findViewById(R.id.lokasi).setOnClickListener(v -> openLokasiActivity());
        findViewById(R.id.jadwal).setOnClickListener(v -> openJadwalActivity());
        findViewById(R.id.jemput).setOnClickListener(v -> openJemputActivity());
    }

    private void fetchNews() {
        progressIndicator.setVisibility(View.VISIBLE);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsResponse> call = apiInterface.getNews("sampah", "id", "4204495a62d94c24ab09a19df13abbf6");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                progressIndicator.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<NewsResponse.Article> articles = response.body().getArticles();
                    if (articles != null && !articles.isEmpty()) {
                        adapter = new NewsAdapter(articles, HomeActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(HomeActivity.this, "Tidak ada berita ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Gagal mendapatkan berita", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                progressIndicator.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "Fetch news error", t);
            }
        });
    }

    private void openProfileActivity() {
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        String confPass = intent.getStringExtra("confpassword");

        Intent profileIntent = new Intent(HomeActivity.this, ProfilActivity.class);
        profileIntent.putExtra("nama", nama);
        profileIntent.putExtra("email", email);
        profileIntent.putExtra("password", password);
        profileIntent.putExtra("confpassword", confPass);
        startActivity(profileIntent);
    }

    private void openBukuActivity() {
        startActivity(new Intent(this, BukuActivity.class));
    }

    private void openLokasiActivity() {
        startActivity(new Intent(this, LokasiActivity.class));
    }

    private void openJadwalActivity() {
        startActivity(new Intent(this, JadwalActivity.class));
    }

    private void openJemputActivity() {
        startActivity(new Intent(this, JemputActivity.class));
    }

    private void openNotifActivity() {
        startActivity(new Intent(this, NotifActivity.class));
    }
}
