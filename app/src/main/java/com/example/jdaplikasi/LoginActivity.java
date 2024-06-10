package com.example.jdaplikasi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private EditText userEmail, userPassword;
    private Button btnSignIn, btnSignInGoogle;
    private TextView btnBack, btnSignUp;
    private ImageView btnBackButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Mendapatkan referensi ke view
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        btnSignIn = findViewById(R.id.btn_signin);
        btnSignInGoogle = findViewById(R.id.btn_signin_google);
        btnBack = findViewById(R.id.text_back);
        btnSignUp = findViewById(R.id.signup);
        btnBackButton = findViewById(R.id.btn_back);

        // Menambahkan listener klik ke tombol "Sign In"
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                signInUser(email, password);
            }
        });

        // Menambahkan listener klik ke tombol "Sign In with Google"
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        // Menambahkan listener klik ke tombol "Back"
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk kembali ke IntroActivity
                Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                // Menghapus tumpukan activity dan memulai IntroActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // Menambahkan listener klik ke teks "Sign Up"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk memulai RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                // Memulai RegisterActivity
                startActivity(intent);
            }
        });

        // Menambahkan listener klik ke tombol "Back"
        btnBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk kembali ke IntroActivity
                Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                // Menghapus tumpukan activity dan memulai IntroActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // Listener untuk tombol Forgot Password
        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Failed to send reset email. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign-In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Mendapatkan informasi profil pengguna yang masuk dengan Google
                                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                                if (account != null) {
                                    String name = account.getDisplayName();
                                    String email = account.getEmail();
                                    String photoUrl = account.getPhotoUrl().toString();

                                    // Simpan informasi pengguna ke database Firebase Realtime
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
                                    String userId = user.getUid();

                                    // Membuat objek pengguna dengan informasi yang diperoleh
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("name", name);
                                    userMap.put("email", email);
                                    userMap.put("photoUrl", photoUrl);

                                    // Menyimpan informasi pengguna ke database Firebase
                                    userRef.child(userId).setValue(userMap);
                                }

                                // Lanjutkan ke halaman beranda setelah pengguna berhasil masuk
                                fetchUserName(user.getUid());
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            fetchUserName(user.getUid());
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Username atau password salah, coba lagi! ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserName(String userId) {
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    Toast.makeText(LoginActivity.this, "Welcome, " + name, Toast.LENGTH_SHORT).show();
                    // Go to HomeActivity
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User not found in the database
                    Toast.makeText(LoginActivity.this, "User not found in database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database error.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
