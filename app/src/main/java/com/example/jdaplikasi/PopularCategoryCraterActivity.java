package com.example.jdaplikasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopularCategoryCraterActivity extends AppCompatActivity {
    ImageView[] tvDataImages = new ImageView[4];
    TextView[] tvDataNames = new TextView[4];
    TextView[] tvDataLocations = new TextView[4];
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_category_crater);

        RelativeLayout craterWhite = findViewById(R.id.popular_category_1);
        RelativeLayout craterTangkupanPerahu = findViewById(R.id.popular_category_2);
        RelativeLayout craterIjen = findViewById(R.id.popular_category_3);
        RelativeLayout craterDieng = findViewById(R.id.popular_category_4);


        craterWhite.setOnClickListener(v -> openDetailPage("detail_whitecrater"));
        craterTangkupanPerahu.setOnClickListener(v -> openDetailPage("detail_tangkupanperahucrater"));
        craterIjen.setOnClickListener(v -> openDetailPage("detail_ijencrater"));
        craterDieng.setOnClickListener(v -> openDetailPage("detail_diengcrater"));

        // Initialize image views
        tvDataImages[0] = findViewById(R.id.popular_category_image_1);
        tvDataImages[1] = findViewById(R.id.popular_category_image_2);
        tvDataImages[2] = findViewById(R.id.popular_category_image_3);
        tvDataImages[3] = findViewById(R.id.popular_category_image_4);

        // Initialize text views for names
        tvDataNames[0] = findViewById(R.id.popular_category_nama_1);
        tvDataNames[1] = findViewById(R.id.popular_category_nama_2);
        tvDataNames[2] = findViewById(R.id.popular_category_nama_3);
        tvDataNames[3] = findViewById(R.id.popular_category_nama_4);

        // Initialize text views for locations
        tvDataLocations[0] = findViewById(R.id.popular_category_lokasi_1);
        tvDataLocations[1] = findViewById(R.id.popular_category_lokasi_2);
        tvDataLocations[2] = findViewById(R.id.popular_category_lokasi_3);
        tvDataLocations[3] = findViewById(R.id.popular_category_lokasi_4);

        // Initialize title text view
        tvTitle = findViewById(R.id.text_popular_category);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Data references for each item
        DatabaseReference[] dataImageRefs = new DatabaseReference[4];
        DatabaseReference[] dataNameRefs = new DatabaseReference[4];
        DatabaseReference[] dataLocationRefs = new DatabaseReference[4];

        // Reference for title
        DatabaseReference titleRef = database.getReference("popular_category/crater/judul");
        titleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);
                tvTitle.setText(title);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                tvTitle.setText("Failed to read title: " + error.toException());
            }
        });

        // Assign database references for each item
        for (int i = 0; i < 4; i++) {
            int index = i + 1; // Firebase Realtime Database index starts from 1
            dataImageRefs[i] = database.getReference("popular_category/crater/data" + index + "/image");
            dataNameRefs[i] = database.getReference("popular_category/crater/data" + index + "/nama");
            dataLocationRefs[i] = database.getReference("popular_category/crater/data" + index + "/lokasi");
        }

        // Set up value event listeners for each item
        for (int i = 0; i < 4; i++) {
            final int index = i;
            dataNameRefs[i].addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nama = dataSnapshot.getValue(String.class);
                    tvDataNames[index].setText(nama);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    tvDataNames[index].setText("Failed to read data: " + error.toException());
                }
            });

            dataLocationRefs[i].addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String lokasi = dataSnapshot.getValue(String.class);
                    tvDataLocations[index].setText(lokasi);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    tvDataLocations[index].setText("Failed to read data: " + error.toException());
                }
            });

            final ImageView imageView = tvDataImages[i];
            dataImageRefs[i].addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imageUrl = dataSnapshot.getValue(String.class);
                    // Load image from URL using Glide
                    Glide.with(PopularCategoryCraterActivity.this)
                            .load(imageUrl)
                            .into(imageView);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle the error here if needed
                }
            });
        }
    }

    public void onclickBack(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void openDetailPage(String detailId) {
        Intent intent = new Intent(this, DetailDestinationActivity.class);
        intent.putExtra("detailId", detailId);
        startActivity(intent);
    }
}
