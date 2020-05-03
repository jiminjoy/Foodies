package com.example.foodies;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import javax.annotation.Nonnull;

public class Forgot_Password extends AppCompatActivity {
    private EditText userEmail;
    private Button Reset;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        userEmail = findViewById(R.id.userEmail);
        Reset = findViewById(R.id.resetbutton);

        firebaseAuth = FirebaseAuth.getInstance();
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@Nonnull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Forgot_Password.this,
                                            "Password send to your email", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Forgot_Password.this,
                                            task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }

                });
            }
        });

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }
    public void onClick(View v) {
        goToLogin();


    }

    public void goToLogin(){
        Intent intent = new Intent( this , Login_Form.class);
        startActivity(intent);
    }
}
