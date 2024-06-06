package com.example.jdaplikasi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName, userEmail, userPassword, userConfirmPassword;
    private Button btnSignUp;
    private TextView btnBack, btnSignIn;
    private ImageView btnBackButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Mendapatkan referensi ke view
        userName = findViewById(R.id.create_user_name);
        userEmail = findViewById(R.id.create_user_email);
        userPassword = findViewById(R.id.create_user_password);
        userConfirmPassword = findViewById(R.id.create_user_password_check);
        btnSignUp = findViewById(R.id.btn_signup);
        btnBack = findViewById(R.id.text_back);
        btnSignIn = findViewById(R.id.signin);
        btnBackButton = findViewById(R.id.btn_back);

        // Menambahkan listener klik ke tombol "Sign Up"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserAccount();
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

    private void createUserAccount() {
        String name = userName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String confirmPassword = userConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();
                            DatabaseReference currentUserDb = mDatabase.child(userId);
                            currentUserDb.child("name").setValue(name).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    // Data saved successfully
                                    Toast.makeText(RegisterActivity.this, "Welcome, " + name, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Failed to save data
                                    Toast.makeText(RegisterActivity.this, "Failed to save user data. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // User with email already exists
                            Toast.makeText(RegisterActivity.this, "Email already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other errors
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(RegisterActivity.this, "Failed to create account: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    // Metode untuk memvalidasi kompleksitas password
    private boolean isPasswordValid(String password) {
        // Password harus mengandung setidaknya satu huruf besar dan satu digit
        return password.matches(".*[A-Z].*") && password.matches(".*\\d.*");
    }

    // Metode untuk menangani email atau password yang sudah digunakan
    private void handleUserCollision(String errorCode) {
        if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
            // Jika email sudah digunakan
            Toast.makeText(RegisterActivity.this, "Email is already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
        } else if (errorCode.equals("ERROR_CREDENTIAL_ALREADY_IN_USE")) {
            // Jika password sudah digunakan
            Toast.makeText(RegisterActivity.this, "Password is already used. Please choose a different password.", Toast.LENGTH_SHORT).show();
        } else {
            // Kesalahan lainnya
            Toast.makeText(RegisterActivity.this, "Failed to create account: Unknown error", Toast.LENGTH_LONG).show();
        }
    }
}