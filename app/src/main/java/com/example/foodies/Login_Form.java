package com.example.foodies;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Login_Form extends AppCompatActivity {
    EditText Name ;
    EditText Password;
//    TextView Info;
    Button Login;
    private Button SignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        mAuth = FirebaseAuth.getInstance();
        Name =  (EditText)findViewById(R.id.userName);
        Password = (EditText)findViewById(R.id.userPassword);
//        Info = (TextView)findViewById(R.id.attempt);
        Login = (Button)findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();

            }
        });
        SignUp = findViewById(R.id.button2);

        SignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToSinup();
            }
        });
    }
    public void goToSinup() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
    private void startSignIn(){
        String emailOrUserName = Name.getText().toString();
        String password = Password.getText().toString();
        if(TextUtils.isEmpty(emailOrUserName) || TextUtils.isEmpty(password)) {
            Toast.makeText(Login_Form.this,"Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(emailOrUserName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(Login_Form.this,"Login unsuccessful, please try again: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(Login_Form.this, Home.class));
                    }
                }
            });
        }
    }
    public void onClick(View v) {
        goToForgotPassword();
    }
    public void goToForgotPassword(){
        Intent intent = new Intent( this , Forgot_Password.class);
        startActivity(intent);
    }
}
