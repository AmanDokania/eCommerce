package com.example.ecommerce.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
private FirebaseAuth mAuth;
private ProgressDialog loadingBar;
private DatabaseReference rooRef;
private Button AlreadhaveAnAccount,registerButton;
private TextView nameInput,emailInput,passwordInput,phoneInput,addressInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

       mAuth = FirebaseAuth.getInstance();
               rooRef = FirebaseDatabase.getInstance().getReference();

               AlreadhaveAnAccount = findViewById(R.id.seller_already_have_an_account);
               registerButton = findViewById(R.id.seller_register_btn);
               nameInput  =findViewById(R.id.seller_name);
               emailInput  =findViewById(R.id.seller_email);
                phoneInput =findViewById(R.id.seller_phone);
               passwordInput  =findViewById(R.id.seller_password);
               addressInput  =findViewById(R.id.seller_address);
               loadingBar = new ProgressDialog(this);


               AlreadhaveAnAccount.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
        startActivity(intent);
        }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        registerSeller();
        }
        });
        }

    private void registerSeller() {
final String name = nameInput.getText().toString();
final String password = passwordInput.getText().toString();
final String email = emailInput.getText().toString();
      final String phone = phoneInput.getText().toString();
final String address = addressInput.getText().toString();

        if(!name.equals("") && !password.equals("") && !email.equals("") && !phone.equals("")  && !address.equals(""))
        {
        loadingBar.setTitle("Create Seller Account");
        loadingBar.setMessage("Please wait we are checking  your credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Log.d("Pass", "registerSeller: "+email +" "+password);

        mAuth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
@Override
public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()) {
        String sid= mAuth.getCurrentUser().getUid();
        loadingBar.dismiss();
        Toast.makeText(SellerRegistrationActivity.this,"You are Registered Successfully",Toast.LENGTH_SHORT).show();

                        HashMap<String,Object> map=  new HashMap<>();

                        map.put("sid",sid);
                        map.put("name",name);
                        map.put("password",password);
                      map.put("phone",phone);
                        map.put("email",email);
                        map.put("address",address);

                        rooRef.child("Sellers").child(sid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {

                                    Intent intent  = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
        }

        }
        })
        .addOnFailureListener(new OnFailureListener() {
@Override
public void onFailure(@NonNull Exception e) {
        Toast.makeText(SellerRegistrationActivity.this,"You are not Registered Successfully",Toast.LENGTH_SHORT).show();
        loadingBar.dismiss();
        }
        });
        }
        else{
        loadingBar.dismiss();
        Toast.makeText(SellerRegistrationActivity.this,"Please Fill out All Fields",Toast.LENGTH_SHORT).show();
        }
        }
    }
