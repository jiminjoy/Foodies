package com.example.foodies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodies.structures.Event;
import com.example.foodies.structures.FoodiesUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class ViewEventActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        String event_id = getIntent().getStringExtra("event_id");

        if (event_id == null) {
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Event");

        event = db.collection("events").document(event_id);

        event.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Event event = snapshot.toObject(Event.class);
                    if (event != null) {
                        TextView name = findViewById(R.id.nameText);
                        String nameText = event.getName();
                        if (nameText == null || nameText.isEmpty()) {
                            name.setText("Unknown");
                        } else {
                            name.setText(nameText);
                        }

                        TextView date = findViewById(R.id.dateField);
                        if (event.getDate() != null) {
                            String dateText = DateFormat.format("dd-MM-yyyy hh:mm", event.getDate().toDate()).toString();
                            if (dateText == null || dateText.isEmpty()) {
                                date.setText("Unknown");
                            } else {
                                date.setText(dateText);
                            }
                        }

                        TextView location = findViewById(R.id.locationField);
                        String locText = event.getLocation();
                        if (locText == null || locText.isEmpty()) {
                            location.setText("Unknown");
                        } else {
                            location.setText(locText);
                        }

                        final TextView nameView = findViewById(R.id.organizerName);
                        nameView.setText("Loading");
                        DocumentReference organizer = event.getOrganizer();
                        organizer.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                nameView.setText(documentSnapshot.getString("name"));
                            }
                        });

                        TextView desc = findViewById(R.id.descField);
                        String descText = event.getDescription();
                        if (descText == null || descText.isEmpty()) {
                            desc.setText("Unknown");
                        } else {
                            desc.setText(descText);
                        }

                        Button join = findViewById(R.id.registerbtn3);
                        Button leave = findViewById(R.id.registerbtn4);

                        if (event.getGoing().contains(mAuth.getUid())) {
                            join.setClickable(false);
                            join.setVisibility(View.INVISIBLE);
                            leave.setClickable(true);
                            leave.setVisibility(View.VISIBLE);
                        } else {
                            join.setClickable(true);
                            join.setVisibility(View.VISIBLE);
                            leave.setClickable(false);
                            leave.setVisibility(View.INVISIBLE);
                        }

                        List<String> attending = event.getGoing();
                        final List<String> attendingNames = new ArrayList<>();

                        final TextView goingPeople = findViewById(R.id.goingField);
                        goingPeople.setText("");

                        for (String s : attending) {
                            db.collection("users").document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String name = documentSnapshot.getString("name");
                                    if (!goingPeople.getText().toString().contains(name)) {
                                        goingPeople.append(name + ", ");
                                    }
                                }
                            });
                        }



                        /*TextView preferences = root.findViewById(R.id.prefField);
                        List<String> listPrefs = foodiesUser.getPreferences();
                        if (listPrefs == null || listPrefs.isEmpty()) {
                            preferences.setText("Unknown");
                        } else {
                            StringBuilder prefs = new StringBuilder();
                            for (int i = 0; i < listPrefs.size(); i++) {
                                prefs.append(listPrefs.get(i));
                                if (i < listPrefs.size() - 1) {
                                    prefs.append(", ");
                                }
                            }
                            preferences.setText(prefs.toString());
                        }*/

                        ImageView imageView = findViewById(R.id.eventPic);
                        String imageUrl = event.getImageUrl();

                        if (imageUrl == null || imageUrl.isEmpty()) {
                        } else {
                            Glide.with(ViewEventActivity.this).load(imageUrl).into(imageView);
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

    public void joinEvent(View view) {
        event.update("going", FieldValue.arrayUnion(mAuth.getUid()));
        this.findViewById(R.id.registerbtn3).setVisibility(View.INVISIBLE);
        this.findViewById(R.id.registerbtn4).setVisibility(View.VISIBLE);
    }

    public void leaveEvent(View view) {
        event.update("going", FieldValue.arrayRemove(mAuth.getUid()));
        this.findViewById(R.id.registerbtn3).setVisibility(View.VISIBLE);
        this.findViewById(R.id.registerbtn4).setVisibility(View.INVISIBLE);
    }
}
