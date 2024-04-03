package com.example.jdaplikasi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Mendapatkan referensi ke tombol "Get Started"
        Button btnGetStarted = findViewById(R.id.btn_getstarted);

        // Menambahkan listener klik ke tombol "Get Started"
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk memulai LoginActivity
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                // Memulai LoginActivity
                startActivity(intent);
            }
        });
    }
}
