package com.example.jdaplikasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailAccountActivity extends AppCompatActivity {

    private TextView textNamaUser, textEmailUser, userNama, userEmail;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_account);

        // Inisialisasi TextView
        textNamaUser = findViewById(R.id.textNamaUser);
        textEmailUser = findViewById(R.id.textEmailUser);
        userNama = findViewById(R.id.user_nama);
        userEmail = findViewById(R.id.user_email);


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
                    userNama.setText(nama);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Penanganan kesalahan
                }
            });
            // Mendapatkan informasi email pengguna dari Firebase Authentication
            String email = currentUser.getEmail();
            textEmailUser.setText(email);
            userEmail.setText(email);

            // Mendapatkan informasi foto pengguna dari Realtime Database
            userRef.child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String photoUrl = snapshot.getValue(String.class);
                    if (photoUrl != null && !photoUrl.isEmpty()) {
                        // Jika URL foto pengguna tersedia, tampilkan foto pengguna
                        Glide.with(DetailAccountActivity.this)
                                .load(photoUrl)
                                .apply(RequestOptions.circleCropTransform())
                                .into((ImageView) findViewById(R.id.image_user));
                    } else {
                        // Jika URL foto pengguna tidak tersedia, gunakan foto default dari layout XML
                        ((ImageView) findViewById(R.id.image_user)).setImageResource(R.drawable.user_1); // Ganti dengan ID foto default di layout XML
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Penanganan kesalahan
                }
            });
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

        findViewById(R.id.user_edit_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailAccountActivity.this, EditAccountActivity.class);
                startActivity(intent);
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
        Intent intent = new Intent(this, WishlistActivity.class);
        startActivity(intent);
    }

    // Method untuk kembali ke HomeActivity
    public void onclickBack(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
