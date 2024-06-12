package com.example.jdaplikasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class HomeActivity extends AppCompatActivity {

    private TextView textNamaUser;
    private ImageView imageViewUser;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout mountBromo = findViewById(R.id.detail_mountbromo);
        LinearLayout pinkBeach = findViewById(R.id.detail_pinkbeach);
        LinearLayout painemoBeach = findViewById(R.id.detail_painemobeach);
        LinearLayout mountMerbabu = findViewById(R.id.detail_mountmerbabu);
        LinearLayout mountRinjani = findViewById(R.id.detail_mountrinjani);
        LinearLayout nusapenidaBeach = findViewById(R.id.detail_nusapenidabeach);
        LinearLayout baronBeach = findViewById(R.id.detail_baronbeach);
        LinearLayout mountLawu = findViewById(R.id.detail_mountlawu);

        mountBromo.setOnClickListener(v -> openDetailPage("detail_mountbromo"));
        pinkBeach.setOnClickListener(v -> openDetailPage("detail_pinkbeach"));
        painemoBeach.setOnClickListener(v -> openDetailPage("detail_pianemobeach"));
        mountMerbabu.setOnClickListener(v -> openDetailPage("detail_mountmerbabu"));
        mountRinjani.setOnClickListener(v -> openDetailPage("detail_mountrinjani"));
        nusapenidaBeach.setOnClickListener(v -> openDetailPage("detail_nusapenidabeach"));
        baronBeach.setOnClickListener(v -> openDetailPage("detail_baronbeach"));
        mountLawu.setOnClickListener(v -> openDetailPage("detail_mountlawu"));

        // Inisialisasi TextView
        textNamaUser = findViewById(R.id.textNamaUser);
        imageViewUser = findViewById(R.id.image_user);

        // Mendapatkan referensi ke user saat ini di Realtime Database
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            // Mendengarkan perubahan pada data pengguna
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nama = snapshot.child("name").getValue(String.class);
                        // Mengatur teks pada TextView sesuai dengan nama pengguna dari Realtime Database
                        textNamaUser.setText("Hi! " + nama);

                        // Mendapatkan informasi foto pengguna dari Realtime Database
                        String photoUrl = snapshot.child("photoUrl").getValue(String.class);
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            // Jika URL foto pengguna tersedia, tampilkan foto pengguna
                            Glide.with(HomeActivity.this)
                                    .load(photoUrl)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(imageViewUser);
                        } else {
                            // Jika URL foto pengguna tidak tersedia, gunakan foto default dari layout XML
                            imageViewUser.setImageResource(R.drawable.user_profile); // Ganti dengan ID foto default di layout XML
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Penanganan kesalahan
                }
            });
        } else {
            textNamaUser.setText("Hi! Guest");
        }

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
        Intent intent = new Intent(this, WishlistActivity.class);
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
        Intent intent = new Intent(this, DetailDestinationActivity.class);
        startActivity(intent);
    }

    public void onclickPopularDestination(View view) {
        Intent intent = new Intent(this, DetailDestinationActivity.class);
        startActivity(intent);
    }

    public void onclickProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openDetailPage(String detailId) {
        Intent intent = new Intent(this, DetailDestinationActivity.class);
        intent.putExtra("detailId", detailId);
        startActivity(intent);
    }
}
