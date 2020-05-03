package com.example.foodies.ui.profile;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodies.R;
import com.example.foodies.structures.FoodiesUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        String uid;
        if (getArguments() == null) {
            uid = mAuth.getCurrentUser().getUid();
        } else {
            uid = getArguments().getString("uid");
            if (uid == null) {
                uid = mAuth.getCurrentUser().getUid();
            }
        }

        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FoodiesUser foodiesUser = documentSnapshot.toObject(FoodiesUser.class);
                if (foodiesUser != null) {
                    TextView name = root.findViewById(R.id.textView3);
                    if (foodiesUser.getName() == null || foodiesUser.getName().isEmpty()) {
                        name.setText("Unknown");
                    } else {
                        name.setText(foodiesUser.getName());
                    }

                    TextView location = root.findViewById(R.id.textView9);
                    if (foodiesUser.getName() == null || foodiesUser.getName().isEmpty()) {
                        location.setText("Madison, WI");
                    } else {
                        location.setText(foodiesUser.getName());
                    }
                }
            }
        });

        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        /*final TextView textView = root.findViewById(R.id.text_notifications);
        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_dashboard, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
//        // TODO: Use the ViewModel
//    }

}
