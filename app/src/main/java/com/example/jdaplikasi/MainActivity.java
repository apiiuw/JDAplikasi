package com.example.jdaplikasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Atur listener untuk menangani klik pada item navigasi
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Periksa id dari item yang diklik
                int id = item.getItemId();
                if (id == R.id.home) {
                    // Ketika item "Home" diklik, tidak perlu melakukan apa-apa karena kita sudah berada di MainActivity
                    return true;
                } else if (id == R.id.wishlist) {
                    // Ketika item "Wishlist" diklik, buka WishlistDestinationActivity
                    openWishlistActivity();
                    return true;
                } else if (id == R.id.profile) {
                    // Ketika item "Profile" diklik, buka ProfileActivity
                    openProfileActivity();
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    // Method untuk membuka WishlistActivity (Sudah Fix)
    private void openWishlistActivity() {
        Intent intent = new Intent(this, WishlistDestinationActivity.class);
        startActivity(intent);
    }

    // Method untuk membuka ProfileActivity
    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}