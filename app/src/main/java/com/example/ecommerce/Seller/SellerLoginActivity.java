package com.example.ecommerce.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class SellerLoginActivity extends AppCompatActivity {
    private EditText emailInput,passwordInput;
    private Button loginSellerButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        loginSellerButton = findViewById(R.id.seller_login_btn);
        emailInput  =findViewById(R.id.seller_login_email);
        passwordInput  =findViewById(R.id.seller_login_password);

        loginSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });
    }

    private void loginSeller() {
        final String password = passwordInput.getText().toString();
        final String email = emailInput.getText().toString();


        if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email))
        {
            loadingBar.setTitle("Login Seller Account");
            loadingBar.setMessage("Please wait we are checking  your credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()) {

                              loadingBar.dismiss();
                              Toast.makeText(SellerLoginActivity.this, "You are Logged In Successfully", Toast.LENGTH_SHORT).show();

                              Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(intent);
                              finish();
                          }
                      }
                  })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SellerLoginActivity.this,"You are not Logged In Successfully",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }});
        }
        else{
            loadingBar.dismiss();
            Toast.makeText(SellerLoginActivity.this,"Please Fill out All Fields",Toast.LENGTH_SHORT).show();
        }
    }
}