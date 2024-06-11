package com.example.jdaplikasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Atur listener untuk menangani klik pada item navigasi

    }

    // Method untuk membuka BrowseActivity (Masih Test)
    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // Method untuk membuka WishlistActivity (Sudah Fix)
    private void openWishlistActivity() {
        Intent intent = new Intent(this, WishlistActivity.class);
        startActivity(intent);
    }

    // Method untuk membuka ProfileActivity
    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onclickBack(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}