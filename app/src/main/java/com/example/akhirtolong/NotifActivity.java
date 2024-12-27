package com.example.akhirtolong;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NotifActivity extends AppCompatActivity {

    private LinearLayout notificationContainer;
    private FirebaseDatabase database;
    private DatabaseReference sensorRef, notificationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        // Inisialisasi UI
        notificationContainer = findViewById(R.id.notificationContainer);

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance();
        sensorRef = database.getReference("DATA_SENSOR"); // Jalur data sensor
        notificationsRef = database.getReference("NOTIFICATIONS"); // Jalur penyimpanan notifikasi

        // Ambil dan tampilkan notifikasi yang sudah ada dari Firebase
        loadExistingNotifications();

        // Tambahkan listener untuk mendengarkan perubahan status sensor
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Ambil status tempat sampah dari Firebase
                String statusNonLogam = dataSnapshot.child("STATUS_NON_LOGAM").getValue(String.class);
                String statusLogam = dataSnapshot.child("STATUS_LOGAM").getValue(String.class);

                // Jika status penuh, tambahkan notifikasi baru dengan warna yang sesuai
                if ("SAMPAH PENUH".equals(statusNonLogam)) {
                    addNotification("Tempat Sampah Non-Logam Penuh", "#5FFD6C"); // Hijau soft
                }

                if ("SAMPAH PENUH".equals(statusLogam)) {
                    addNotification("Tempat Sampah Logam Penuh", "#FBEF6B"); // Kuning soft
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Menangani error jika terjadi
                Toast.makeText(NotifActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Ambil notifikasi yang sudah disimpan di Firebase dan tampilkan
     */
    private void loadExistingNotifications() {
        notificationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationContainer.removeAllViews(); // Bersihkan notifikasi lama

                // Iterasi semua notifikasi yang disimpan
                for (DataSnapshot notificationSnapshot : dataSnapshot.getChildren()) {
                    String message = notificationSnapshot.child("message").getValue(String.class);
                    String color = notificationSnapshot.child("color").getValue(String.class);
                    if (message != null && color != null) {
                        displayNotification(message, color);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NotifActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Tambahkan notifikasi baru dan simpan ke Firebase
     */
    private void addNotification(String message, String backgroundColor) {
        // Simpan notifikasi ke Firebase
        String key = notificationsRef.push().getKey(); // Generate key unik
        if (key != null) {
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("message", message);
            notificationData.put("color", backgroundColor);
            notificationsRef.child(key).setValue(notificationData);
        }

        // Tampilkan notifikasi ke layar
        displayNotification(message, backgroundColor);
    }

    /**
     * Tampilkan notifikasi di layar
     */
    private void displayNotification(String message, String backgroundColor) {
        // Buat layout horizontal untuk ikon dan teks
        LinearLayout notificationLayout = new LinearLayout(this);
        notificationLayout.setOrientation(LinearLayout.HORIZONTAL);
        notificationLayout.setBackgroundColor(Color.parseColor(backgroundColor)); // Warna latar belakang dinamis
        notificationLayout.setPadding(20, 16, 20, 16);
        notificationLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Tambahkan margin antar notifikasi
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) notificationLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 16); // Margin bawah antar notifikasi
        notificationLayout.setLayoutParams(layoutParams);

        // Tambahkan ikon (ImageView)
        ImageView trashIcon = new ImageView(this);
        trashIcon.setId(View.generateViewId());
        trashIcon.setImageResource(R.drawable.ic_trash); // Pastikan drawable ada
        trashIcon.setLayoutParams(new LinearLayout.LayoutParams(
                64, // Lebar ikon
                64  // Tinggi ikon
        ));

        // Tambahkan teks (TextView)
        TextView notificationText = new TextView(this);
        notificationText.setText(message);
        notificationText.setTextSize(16);
        notificationText.setPadding(16, 0, 0, 0); // Jarak antara teks dan ikon
        notificationText.setTextColor(getResources().getColor(android.R.color.black));
        notificationText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Tambahkan ikon dan teks ke layout horizontal
        notificationLayout.addView(trashIcon);
        notificationLayout.addView(notificationText);

        // Tambahkan layout horizontal ke notificationContainer
        notificationContainer.addView(notificationLayout, 0); // Tambahkan di atas notifikasi lama
    }
}
