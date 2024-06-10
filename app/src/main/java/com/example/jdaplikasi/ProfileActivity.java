package com.example.jdaplikasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView textNamaUser;
    private TextView textEmailUser;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi TextView
        textNamaUser = findViewById(R.id.textNamaUser);
        textEmailUser = findViewById(R.id.textEmailUser);

        // Mendapatkan pengguna yang sedang masuk
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Mendapatkan referensi ke node pengguna di Realtime Database
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            // Mendapatkan informasi nama pengguna dari Realtime Database
            userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nama = snapshot.getValue(String.class);
                    textNamaUser.setText(nama);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Penanganan kesalahan
                }
            });
            // Mendapatkan informasi email pengguna dari Firebase Authentication
            String email = currentUser.getEmail();
            textEmailUser.setText(email);
        }

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Atur listener untuk menangani klik pada item navigasi
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Periksa id dari item yang diklik
                int id = item.getItemId();
                if (id == R.id.home) {
                    // Ketika item "Home" diklik, buka HomeActivity
                    openHomeActivity();
                    return true;
                } else if (id == R.id.wishlist) {
                    // Ketika item "Wishlist" diklik, buka WishlistDestinationActivity
                    openWishlistActivity();
                    return true;
                } else if (id == R.id.profile) {

                    return true;
                }
                return false;
            }
        });
        // Set item Profile sebagai yang terpilih
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Dapatkan referensi ke tombol btn_logout
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tampilkan pesan notifikasi
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String nama = textNamaUser.getText().toString();
                    Toast.makeText(ProfileActivity.this, "Anda telah logout dari akun " + nama, Toast.LENGTH_SHORT).show();
                }

                // Proses logout
                FirebaseAuth.getInstance().signOut();

                // Pindah ke halaman LoginActivity
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Selesaikan activity ini agar tidak dapat kembali lagi setelah logout
            }
        });

    }

    // Method untuk membuka HomeActivity
    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // Method untuk membuka WishlistDestinationActivity
    private void openWishlistActivity() {
        Intent intent = new Intent(this, PopularDestinationActivity.class);
        startActivity(intent);
    }

    // Method untuk kembali ke HomeActivity
    public void onclickBack(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
