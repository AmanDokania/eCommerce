package com.example.ecommerce.Buyer;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
private Button CreateAccountButton;
private EditText InputName,InputPhoneNumber,InputPassword;
private ProgressDialog loadingBar;
private DatabaseReference Rootref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_username_input);
        InputPassword =findViewById(R.id.register_password_input);
        InputPhoneNumber = findViewById(R.id.register_phone_number_input);
        loadingBar = new ProgressDialog(this);
        Rootref = FirebaseDatabase.getInstance().getReference();
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name  =InputName.getText().toString();
        String password = InputPassword.getText().toString();
        String phone= InputPhoneNumber.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(RegisterActivity.this,"Please Enter Your Name..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(RegisterActivity.this,"Please Enter Your password..",Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(RegisterActivity.this,"Please Enter Your Phone Number..",Toast.LENGTH_SHORT).show();
        }
       else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait we are checking  your credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatePhoneNumber(name,phone,password);
        }
    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {

      Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(!snapshot.child("Users").child(phone).exists())
              {
                  HashMap<String,Object> dataMap  = new HashMap<>();
                  dataMap.put("phone",phone);
                  dataMap.put("password",password);
                  dataMap.put("name",name);

                  Rootref.child("Users").child(phone).updateChildren(dataMap)
                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()) {
                                      Toast.makeText(RegisterActivity.this, "Congtrulation Your Account is Created Successfully", Toast.LENGTH_SHORT).show();
                                      loadingBar.dismiss();
                                      Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                      startActivity(intent);
                                  }
                                  else{
                                      Toast.makeText(RegisterActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                                      loadingBar.dismiss();
                                  }
                              }
                          });
              }
              else{
                  Toast.makeText(RegisterActivity.this,"This "+phone +" is Already Exists",Toast.LENGTH_SHORT).show();
                  loadingBar.dismiss();
                  Toast.makeText(RegisterActivity.this,"Please Try Using Another Phone Number",Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                  startActivity(intent);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }


}