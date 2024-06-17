package com.example.jdaplikasi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailDestinationActivity extends AppCompatActivity {

    private String mapsUrl, hotelUrl, restaurantUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_destination);

        String detailId = getIntent().getStringExtra("detailId");
        if (detailId == null) {
            Log.e("DetailDestination", "No detailId found in intent");
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("detail_destination").child(detailId);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("DetailDestination", "No user is logged in");
            return;
        }

        String currentUserId = currentUser.getUid();

        // Initialize views
        TextView title = findViewById(R.id.detail_nama);
        TextView location = findViewById(R.id.detail_lokasi);
        TextView priceRange = findViewById(R.id.detail_range_price);
        TextView category = findViewById(R.id.category);
        TextView description = findViewById(R.id.detail_description);
        LinearLayout imageLayout1 = findViewById(R.id.image1);
        ImageView imageView2 = findViewById(R.id.image2);
        ImageView imageView3 = findViewById(R.id.image3);
        ImageView imageView4 = findViewById(R.id.image4);
        ImageView imageView5 = findViewById(R.id.image5);
        ImageView imageView6 = findViewById(R.id.image6);
        ImageView imageView7 = findViewById(R.id.image7);
        ImageView addWishlistButton = findViewById(R.id.add_wishlist);

        // Retrieve data from Firebase
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String titleText = dataSnapshot.child("detail_nama").getValue(String.class);
                    String locationText = dataSnapshot.child("detail_lokasi").getValue(String.class);
                    String priceRangeText = dataSnapshot.child("detail_range_price").getValue(String.class);
                    String categoryText = dataSnapshot.child("category").getValue(String.class);
                    String descriptionText = dataSnapshot.child("detail_description").getValue(String.class);
                    String imageUrl1 = dataSnapshot.child("image/image1").getValue(String.class);
                    String imageUrl2 = dataSnapshot.child("image/image2").getValue(String.class);
                    String imageUrl3 = dataSnapshot.child("image/image3").getValue(String.class);
                    String imageUrl4 = dataSnapshot.child("image/image4").getValue(String.class);
                    String imageUrl5 = dataSnapshot.child("image/image5").getValue(String.class);
                    String imageUrl6 = dataSnapshot.child("image/image6").getValue(String.class);
                    String imageUrl7 = dataSnapshot.child("image/image7").getValue(String.class);
                    mapsUrl = dataSnapshot.child("maps_url").getValue(String.class);
                    hotelUrl = dataSnapshot.child("hotel_url").getValue(String.class);
                    restaurantUrl = dataSnapshot.child("restaurant_url").getValue(String.class);

                    // Log retrieved data (optional)
                    Log.d("DetailDestination", "Title: " + titleText);
                    Log.d("DetailDestination", "Location: " + locationText);
                    Log.d("DetailDestination", "Price Range: " + priceRangeText);
                    Log.d("DetailDestination", "Category: " + categoryText);
                    Log.d("DetailDestination", "Description: " + descriptionText);
                    Log.d("DetailDestination", "Image1: " + imageUrl1);
                    Log.d("DetailDestination", "Maps URL: " + mapsUrl);

                    // Set data to views
                    title.setText(titleText);
                    location.setText(locationText);
                    priceRange.setText(priceRangeText);
                    category.setText(categoryText);
                    description.setText(descriptionText);

                    // Load images using Glide
                    Glide.with(DetailDestinationActivity.this)
                            .asBitmap()
                            .load(imageUrl1)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    BitmapDrawable drawable = new BitmapDrawable(getResources(), resource);
                                    imageLayout1.setBackground(drawable);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle placeholder if needed
                                }
                            });

                    Glide.with(DetailDestinationActivity.this).load(imageUrl2).into(imageView2);
                    Glide.with(DetailDestinationActivity.this).load(imageUrl3).into(imageView3);
                    Glide.with(DetailDestinationActivity.this).load(imageUrl4).into(imageView4);
                    Glide.with(DetailDestinationActivity.this).load(imageUrl5).into(imageView5);
                    Glide.with(DetailDestinationActivity.this).load(imageUrl6).into(imageView6);
                    Glide.with(DetailDestinationActivity.this).load(imageUrl7).into(imageView7);

                    // Check if the item is in the user's wishlist
                    DatabaseReference wishlistRef = database.getReference("users").child(currentUserId).child("wishlist");
                    wishlistRef.orderByChild("item_nama").equalTo(titleText).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Item is in the wishlist, set tint to red
                                addWishlistButton.setColorFilter(Color.parseColor("#C20000"));

                                // Set OnClickListener to remove item from wishlist
                                addWishlistButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (DataSnapshot wishlistItem : snapshot.getChildren()) {
                                            wishlistItem.getRef().removeValue();
                                        }
                                        // Set tint back to default color
                                        addWishlistButton.setColorFilter(null);
                                    }
                                });
                            } else {
                                // Set OnClickListener to add item to wishlist
                                addWishlistButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DatabaseReference newWishlistItemRef = wishlistRef.push();
                                        newWishlistItemRef.child("item_image").setValue(imageUrl1);
                                        newWishlistItemRef.child("item_lokasi").setValue(locationText);
                                        newWishlistItemRef.child("item_nama").setValue(titleText);

                                        // Set tint to red
                                        addWishlistButton.setColorFilter(Color.parseColor("#C20000"));

                                        // Change OnClickListener to remove item from wishlist
                                        addWishlistButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                newWishlistItemRef.removeValue();
                                                // Set tint back to default color
                                                addWishlistButton.setColorFilter(null);
                                            }
                                        });
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.e("DetailDestination", "Failed to read wishlist: " + error.getMessage());
                        }
                    });
                } else {
                    Log.e("DetailDestination", "No data found for detailId: " + detailId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DetailDestination", "Failed to read data: " + error.getMessage());
            }
        });
    }

    public void onclickBack(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openMapsLink(View view) {
        if (mapsUrl != null && !mapsUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mapsUrl));
            startActivity(intent);
        } else {
            Log.e("DetailDestination", "Maps URL is not available");
        }
    }

    public void openHotelLink(View view) {
        if (hotelUrl != null && !hotelUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(hotelUrl));
            startActivity(intent);
        } else {
            Log.e("DetailDestination", "Hotel URL is not available");
        }
    }

    public void openRestaurantLink(View view) {
        if (restaurantUrl != null && !restaurantUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(restaurantUrl));
            startActivity(intent);
        } else {
            Log.e("DetailDestination", "Restaurant URL is not available");
        }
    }
}
