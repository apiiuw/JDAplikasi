package com.example.jdaplikasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        // Di dalam onCreate() method di WishlistActivity.java

        // Mendapatkan referensi ke database Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("wishlist");

        // Mendapatkan data dari database Firebase
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Membuat list untuk menyimpan item-item wishlist
                List<ItemList> wishlistItems = new ArrayList<>();

                // Melakukan iterasi pada setiap item di database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Mengambil data item dari database
                    String itemName = snapshot.child("item_nama").getValue(String.class);
                    String itemLocation = snapshot.child("item_lokasi").getValue(String.class);
                    String itemImage = snapshot.child("item_image").getValue(String.class);

                    // Membuat objek ItemList dan menambahkannya ke dalam list
                    ItemList item = new ItemList(itemName, itemLocation, itemImage);
                    wishlistItems.add(item);
                }

                // Mengatur RecyclerView dengan adapter
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                WishlistAdapter adapter = new WishlistAdapter(wishlistItems, databaseRef);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle jika terjadi error saat mengambil data dari database
            }
        });


        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Atur listener untuk menangani klik pada item navigasi
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Periksa id dari item yang diklik
                int id = item.getItemId();
                if (id == R.id.home) {
                    // Ketika item "Home" diklik, buka MainActivity
                    openHomeActivity();
                    return true;
                } else if (id == R.id.wishlist) {
                    // Ketika item "Wishlist" diklik, tidak perlu melakukan apa-apa karena kita sudah berada di WishlistActivity
                    return true;
                } else if (id == R.id.profile) {
                    // Ketika item "Profile" diklik, buka ProfileActivity
                    openProfileActivity();
                    return true;
                }
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.wishlist);
    }

    // Method untuk membuka MainActivity
    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // Method untuk membuka ProfileActivity
    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // Method untuk kembali ke halaman sebelumnya
    public void onclickBack(View view) {
        finish(); // Tutup WishlistActivity dan kembali ke halaman sebelumnya
    }

    // Method untuk menangani klik pada detail destinasi
    public void onclickDetailDestination(View view) {
        Intent intent = new Intent(this, DetailPinkBeachActivity.class);
        startActivity(intent);
    }
}
