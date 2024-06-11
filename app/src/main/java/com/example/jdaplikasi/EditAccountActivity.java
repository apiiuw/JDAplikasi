package com.example.jdaplikasi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditAccountActivity extends AppCompatActivity {

    private EditText userNama, userEmail;
    private TextView textNamaUser, textEmailUser;
    private ImageView imageViewUser;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Inisialisasi EditText, ImageView, dan TextView
        textNamaUser = findViewById(R.id.textNamaUser);
        textEmailUser = findViewById(R.id.textEmailUser);
        userNama = findViewById(R.id.user_nama);
        userEmail = findViewById(R.id.user_email);
        imageViewUser = findViewById(R.id.image_user);

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Atur listener untuk menangani klik pada item navigasi
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Periksa id dari item yang diklik
                int id = item.getItemId();
                if (id == R.id.home) {
                    // Ketika item "Home" diklik, buka HomeActivity
                    openHomeActivity();
                    return true;
                } else if (id == R.id.wishlist) {
                    // Ketika item "Wishlist" diklik, buka WishlistDestinationActivity
                    openWishlistActivity();
                    return true;
                } else if (id == R.id.profile) {
                    return true;
                }
                return false;
            }
        });
        // Set item Profile sebagai yang terpilih
        bottomNavigationView.setSelectedItemId(R.id.profile);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Mendapatkan referensi ke node pengguna di Realtime Database
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            // Mendapatkan informasi nama pengguna dari Realtime Database
            userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nama = snapshot.getValue(String.class);
                    userNama.setHint(nama);
                    textNamaUser.setText(nama);

                    // Mendapatkan informasi email pengguna dari Firebase Authentication
                    String email = currentUser.getEmail();
                    userEmail.setText(email);
                    textEmailUser.setText(email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Penanganan kesalahan
                }

            });

            // Mendapatkan informasi foto pengguna dari Realtime Database
            userRef.child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String photoUrl = snapshot.getValue(String.class);
                    if (photoUrl != null && !photoUrl.isEmpty()) {
                        // Jika URL foto pengguna tersedia, tampilkan foto pengguna
                        Glide.with(EditAccountActivity.this)
                                .load(photoUrl)
                                .apply(RequestOptions.circleCropTransform())
                                .into(imageViewUser);
                    } else {
                        // Jika URL foto pengguna tidak tersedia, gunakan foto default dari layout XML
                        imageViewUser.setImageResource(R.drawable.user_profile); // Ganti dengan ID foto default di layout XML
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Penanganan kesalahan
                }
            });

        }

        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Set OnClickListener untuk tombol Done
        findViewById(R.id.user_edit_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    // Method untuk membuka galeri dan memilih gambar
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Method yang dipanggil setelah memilih gambar dari galeri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageViewUser.setImageURI(selectedImageUri);
        }
    }

    // Method untuk mengunggah gambar dan memperbarui profil
    private void updateProfile() {
        // Mendapatkan UID pengguna saat ini
        String userId = mAuth.getCurrentUser().getUid();
        // Mendefinisikan path penyimpanan Firebase
        StorageReference fileReference = storageRef.child("profile_images/" + userId);

        if (selectedImageUri != null) {
            // Mengunggah gambar ke Firebase Storage
            fileReference.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Mendapatkan URL gambar yang diunggah
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    // Memperbarui URL gambar di database pengguna
                                    userRef.child("photoUrl").setValue(imageUrl);
                                    Toast.makeText(EditAccountActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditAccountActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Jika pengguna tidak memilih gambar baru, hanya perbarui nama pengguna
            updateName();
        }
    }

    // Method untuk menyimpan perubahan nama ke database Firebase
    private void updateName() {
        String newName = userNama.getText().toString().trim();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef.child("name").setValue(newName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditAccountActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditAccountActivity.this, "Failed to update name", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Method untuk membuka HomeActivity
    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // Method untuk membuka WishlistDestinationActivity
    private void openWishlistActivity() {
        Intent intent = new Intent(this, WishlistActivity.class);
        startActivity(intent);
    }

    // Method untuk kembali ke halaman sebelumnya
    public void onclickBack(View view) {
        Intent intent = new Intent(this, DetailAccountActivity.class);
        startActivity(intent);
    }
}
