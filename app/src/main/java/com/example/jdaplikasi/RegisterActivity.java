package com.example.jdaplikasi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Mendapatkan referensi ke tombol dan teks yang dibutuhkan
        Button btnSignUp = findViewById(R.id.btn_signup);
        TextView btnBack = findViewById(R.id.text_back);
        TextView btnSignIn = findViewById(R.id.signin);
        ImageView btnBackButton = findViewById(R.id.btn_back);

        // Menambahkan listener klik ke tombol "Sign Up"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk memulai MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                // Memulai MainActivity
                startActivity(intent);
            }
        });

        // Menambahkan listener klik ke tombol "Back"
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk kembali ke IntroActivity
                Intent intent = new Intent(RegisterActivity.this, IntroActivity.class);
                // Menghapus tumpukan activity dan memulai IntroActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // Menambahkan listener klik ke teks "Sign In"
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk memulai LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                // Memulai LoginActivity
                startActivity(intent);
            }
        });

        // Menambahkan listener klik ke tombol "Back"
        btnBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk kembali ke IntroActivity
                Intent intent = new Intent(RegisterActivity.this, IntroActivity.class);
                // Menghapus tumpukan activity dan memulai IntroActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }
}
