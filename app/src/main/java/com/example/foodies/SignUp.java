package com.example.foodies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText Name ;
    private EditText Password;
    private EditText ConfirmPass;
    private EditText UserName;
    private EditText Email;
    private Button Register;
    private RadioButton Male;
    private RadioButton Female;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);

        Name =  (EditText)findViewById(R.id.eFullname);
        UserName = (EditText)findViewById(R.id.eUsername);
        Email = (EditText) findViewById(R.id.eEmail);
        Password = (EditText)findViewById(R.id.ePassword);
        ConfirmPass = (EditText)findViewById(R.id.eConfirmPass);
        Register = (Button)findViewById(R.id.registerbtn);
        Male = (RadioButton) findViewById(R.id.radiobtn);
        Female = (RadioButton) findViewById(R.id.radiobtn2);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String username = UserName.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                String confirmPass = ConfirmPass.getText().toString();
                if (email.isEmpty()) {
                    Email.setError("Please enter your email");
                    Email.requestFocus();
                }
                else if (name.isEmpty()) {
                    Password.setError("Please enter your full name");
                    Password.requestFocus();
                }
                else if (username.isEmpty()) {
                    Password.setError("Please enter your username");
                    Password.requestFocus();
                }
                else if (password.isEmpty()) {
                    Password.setError("Please enter your password");
                    Password.requestFocus();
                }
                else if(validate(email, password, confirmPass)){
                    Log.d("checking", "come????");
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                task.getException().printStackTrace();
                                Toast.makeText(SignUp.this,"Signup unsuccessful, Please Try Again: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("checking,", task.getException().getMessage());
                            } else {
                                startActivity(new Intent(SignUp.this, Home.class));
                            }
                        }
                    });


                    // startActivity(new Intent(RegistrationForm.this, Home.class));
                } else {
                    Toast.makeText(SignUp.this,"Error Occured!", Toast.LENGTH_SHORT).show();
                }


            }

            public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                    "[a-zA-Z0-9+._%-+]{1,256}" +
                            "@" +
                            "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                            "(" +
                            "." +
                            "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                            ")+"
            );

            private boolean validate(String email, String password, String confirmPass) {
                boolean temp = true;

                if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()){
                    Toast.makeText(SignUp.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
                    temp=false;
                    Log.d("checking", "where?");
                }
                else if(!password.equals(confirmPass)){
                    Toast.makeText(SignUp.this,"Passwords Don't Match",Toast.LENGTH_SHORT).show();
                    temp=false;
                    Log.d("checking", "goes here?");
                }
                return temp;

            }

        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void onClick(View v) {
        goToActivity2();


    }

    public void goToActivity2(){
        Intent intent = new Intent( this , Login_Form.class);
        startActivity(intent);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"You signed in successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,Home.class));
        }else {
            Toast.makeText(this,"You didn't sign in", Toast.LENGTH_LONG).show();
        }
    }





}
