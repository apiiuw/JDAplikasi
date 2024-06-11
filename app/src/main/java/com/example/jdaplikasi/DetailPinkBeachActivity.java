package com.example.jdaplikasi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DetailPinkBeachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pink_beach);
    }

    public void onclickBack(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openMapsLink(View view) {
        String url = "https://maps.app.goo.gl/6cxXTuB7ANQsGczQA";  // Replace with your desired URL
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void openHotelLink(View view) {
        String url = "https://www.google.co.id/maps/search/Hotels/@-8.588409,119.4797289,12.73z/data=!4m8!2m7!3m5!2sPink+Beach!3s0x2db4567cf6bd1967:0x9f2eb4aca51c5fd0!4m2!1d119.5197902!2d-8.6012033!6e3?entry=ttu";  // Replace with your desired URL
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void openRestaurantLink(View view) {
        String url = "https://www.google.co.id/maps/search/Restaurants/@-8.5775375,119.5230033,12.95z/data=!4m8!2m7!3m5!2sPink+Beach!3s0x2db4567cf6bd1967:0x9f2eb4aca51c5fd0!4m2!1d119.5197902!2d-8.6012033!6e5?entry=ttu";  // Replace with your desired URL
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
