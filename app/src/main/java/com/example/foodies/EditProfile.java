package com.example.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodies.R;
import com.example.foodies.structures.FoodiesUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView imageView;

    boolean imageChanged = false;

    private Uri selectedImage;
    private String gender;
    private String userBio;
    private RadioButton male;
    private RadioButton female;
    private TextView bio;
    private TextView preferences;
    private TextView location;
    private TextView name;
    private TextView email;
    private TextView password;
    private TextView passwordConfirm;

    private FoodiesUser foodiesUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final String uid = mAuth.getCurrentUser().getUid();

        if (uid == null) {
            return;
        }

        final EditProfile editProfile = this;
        imageView = findViewById(R.id.editProfilePic);

        male = findViewById(R.id.radiobtnedit);
        female = findViewById(R.id.radiobtn2edit);
        bio = findViewById(R.id.editText2);
        preferences = findViewById(R.id.editText3);
        location = findViewById(R.id.editText4);
        name = findViewById(R.id.eFullnameEdit);
        email = findViewById(R.id.eEmailEdit);
        password = findViewById(R.id.ePasswordEdit);
        passwordConfirm = findViewById(R.id.eConfirmPassEdit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    foodiesUser = documentSnapshot.toObject(FoodiesUser.class);
                    if (foodiesUser != null) {
                        if (foodiesUser.getName() == null || foodiesUser.getName().isEmpty()) {
                        } else {
                            name.setText(foodiesUser.getName());
                        }

                        if (foodiesUser.getLocation() == null || foodiesUser.getLocation().isEmpty()) {
                        } else {
                            location.setText(foodiesUser.getLocation());
                        }

                        if (foodiesUser.getGender() == null || foodiesUser.getGender().isEmpty()) {
                        } else {
                            if (foodiesUser.getGender().equalsIgnoreCase("male")) {
                                gender = "Male";
                                male.toggle();
                            } else {
                                gender = "Female";
                                female.toggle();
                            }
                        }

                        userBio = foodiesUser.getBio();
                        if (userBio == null || userBio.isEmpty()) {
                        } else {
                            bio.setText(userBio);
                        }

                        List<String> listPrefs = foodiesUser.getPreferences();
                        if (listPrefs == null || listPrefs.isEmpty()) {
                        } else {
                            StringBuilder prefs = new StringBuilder();
                            for (int i = 0; i < listPrefs.size(); i++) {
                                prefs.append(listPrefs.get(i));
                                if (i < listPrefs.size() - 1) {
                                    prefs.append(", ");
                                }
                            }
                            preferences.setText(prefs.toString());
                        }

                        email.setText(mAuth.getCurrentUser().getEmail());

                        String imageUrl = foodiesUser.getImageUrl();

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(editProfile).load(imageUrl).into(imageView);
                        }


                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public static final int PICK_IMAGE = 2;

    public void onSave(View view) {
        if (foodiesUser == null) {
            Toast.makeText(this, "Failed to update information due to storage failure!", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Saving...");
        progressDialog.show();

        final String bioText = bio.getText().toString();
        final String prefText = preferences.getText().toString();
        final String locText = location.getText().toString();
        final String nameText = name.getText().toString();
        final String emailText = email.getText().toString();
        final String passText = password.getText().toString();
        final String confirmPassText = password.getText().toString();

        final String genderText;
        if (male.isChecked()) {
            genderText = "male";
        } else {
            genderText = "female";
        }

        if (nameText.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Your name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (emailText.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "You cannot delete your email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passText.isEmpty() && confirmPassText.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "You must confirm your new password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!confirmPassText.isEmpty() && passText.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "You cannot not only confirm your new password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passText.equals(confirmPassText)) {
            progressDialog.dismiss();
            Toast.makeText(this, "The password confirmation does not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String newImage;
        if (imageChanged) {
            final StorageReference ref = FirebaseStorage.getInstance().getReference()
                    .child("images/" + UUID.randomUUID().toString());
            ref.putFile(selectedImage).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(
                                UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setMessage("Uploaded " + 95 + "%");
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadPhotoUrl) {
                                    String newImage = downloadPhotoUrl.toString();

                                    final FoodiesUser newUser = new FoodiesUser(bioText, emailText, genderText, newImage, locText, nameText, Lists.newArrayList(prefText.split("\\s*,\\s*")));

                                    if (!newUser.getEmail().equalsIgnoreCase(foodiesUser.getEmail())) {
                                        mAuth.getCurrentUser().updateEmail(emailText).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfile.this, "Failed to update email!", Toast.LENGTH_SHORT).show();
                                                Log.i("image", "Failed to update email.");
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i("image", "Successfully updated email.");
                                                if (!passText.isEmpty()) {
                                                    mAuth.getCurrentUser().updatePassword(passText).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfile.this, "Failed to update password:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            db.collection("users").document(mAuth.getUid()).set(newUser).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(EditProfile.this, "Failed to store profile:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(EditProfile.this, "Profile Edited!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                } else {
                                                    db.collection("users").document(mAuth.getUid()).set(newUser).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfile.this, "Failed to store profile:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfile.this, "Profile Edited!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        if (!passText.isEmpty()) {
                                            mAuth.getCurrentUser().updatePassword(passText).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditProfile.this, "Failed to update password:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    db.collection("users").document(mAuth.getUid()).set(newUser).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfile.this, "Failed to store profile:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(EditProfile.this, "Profile Edited!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            db.collection("users").document(mAuth.getUid()).set(newUser).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditProfile.this, "Failed to store profile:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(EditProfile.this, "Profile Edited!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + ((int)progress - 6) + "%");
                        }
                    });
        } else {
            newImage = foodiesUser.getImageUrl();

            final FoodiesUser newUser = new FoodiesUser(bioText, emailText, genderText, newImage, locText, nameText, Lists.newArrayList(prefText.split("\\s*,\\s*")));


            if (!newUser.getEmail().equalsIgnoreCase(foodiesUser.getEmail())) {
                mAuth.getCurrentUser().updateEmail(emailText).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Failed to update email!", Toast.LENGTH_SHORT).show();
                        Log.i("no-image", "Failed to update email.");
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("no-image", "Successfully updated email.");
                        if (!passText.isEmpty()) {
                            mAuth.getCurrentUser().updatePassword(passText).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfile.this, "Failed to update password:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    db.collection("users").document(mAuth.getUid()).set(newUser).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProfile.this, "Failed to store profile:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(EditProfile.this, "Profile Edited!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            } else {
                if (!passText.isEmpty()) {
                    mAuth.getCurrentUser().updatePassword(passText).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Failed to update password:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.collection("users").document(mAuth.getUid()).set(newUser).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfile.this, "Failed to store profile:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfile.this, "Profile Edited!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    db.collection("users").document(mAuth.getUid()).set(newUser).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Failed to store profile:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Profile Edited!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    public void editProfilePicker(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Profile Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();

            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                selectedImage);
                imageView.setImageBitmap(bitmap);

                imageChanged = true;

            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
