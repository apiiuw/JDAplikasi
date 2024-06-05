package com.example.jdaplikasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
    }

    // Method untuk membuka WishlistActivity (Sudah Fix)
    private void openWishlistActivity() {
        Intent intent = new Intent(this, PopularDestinationActivity.class);
        startActivity(intent);
    }

    // Method untuk membuka ProfileActivity
    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onclickPopularMountainCategory(View view) {
        Intent intent = new Intent(this, PopularCategoryMountainActivity.class);
        startActivity(intent);
    }

    public void onclickPopularBeachCategory(View view) {
        Intent intent = new Intent(this, PopularCategoryBeachActivity.class);
        startActivity(intent);
    }

    public void onclickPopularLakeCategory(View view) {
        Intent intent = new Intent(this, PopularCategoryLakeActivity.class);
        startActivity(intent);
    }

    public void onclickPopularCraterCategory(View view) {
        Intent intent = new Intent(this, PopularCategoryCraterActivity.class);
        startActivity(intent);
    }

    public void onclickPopularParkCategory(View view) {
        Intent intent = new Intent(this, PopularCategoryParkActivity.class);
        startActivity(intent);
    }

    public void onclickNotification(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    public void onclickExploreCities(View view) {
        Intent intent = new Intent(this, DetailPinkBeachActivity.class);
        startActivity(intent);
    }

    public void onclickPopularDestination(View view) {
        Intent intent = new Intent(this, PopularDestinationActivity.class);
        startActivity(intent);
    }
    public void onclickProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}