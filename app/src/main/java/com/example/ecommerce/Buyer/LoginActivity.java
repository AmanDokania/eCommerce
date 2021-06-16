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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.Seller.SellerProductCategoryActivity;
import com.example.ecommerce.Model.User;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton;
    private EditText InputPhoneNumber,InputPassword;
    private ProgressDialog loadingBar;
    private DatabaseReference Rootref;
    private String parentDbName = "Users";
    private TextView AdminLink,NotAdminLink,ForgotPassword;
 private CheckBox chkBoxRememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton = findViewById(R.id.login_btn);
        InputPassword =findViewById(R.id.login_password_input);
        InputPhoneNumber = findViewById(R.id.login_phone_number_input);
        loadingBar = new ProgressDialog(this);
        Rootref = FirebaseDatabase.getInstance().getReference();
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        ForgotPassword =  findViewById(R.id.forget_password_link);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
    }

    private void LoginUser() {
        String password = InputPassword.getText().toString();
        String phone= InputPhoneNumber.getText().toString();
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Please Enter Your password..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(LoginActivity.this,"Please Enter Your Phone Number..",Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait we are checking  your credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToLogin(phone,password);
        }
    }

    private void AllowAccessToLogin(final String phone, final String password) {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

         Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.child(parentDbName).child(phone).exists())
               {
                 User userData =  snapshot.child(parentDbName).child(phone).getValue(User.class);

                   if(userData.getPhone().equals(phone)) {
                       if (userData.getPassword().equals(password)) {
                           if(parentDbName.equals("Admins")) {
                               Toast.makeText(LoginActivity.this, "Welcome Admin, You are Logged in Successfully.", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();
                               Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                               startActivity(intent);
                           }
                           else if(parentDbName.equals("Users"))
                           {
                               Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();
                               Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                               Prevalent.currentonlineUser = userData;
                               startActivity(intent);
                           }
                       } else {
                           Toast.makeText(LoginActivity.this, "Password is Incorrect.", Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();
                       }
                   }
               }
               else{
                   Toast.makeText(LoginActivity.this,"Account With this "+phone+" do not exists.",Toast.LENGTH_SHORT).show();
                   loadingBar.dismiss();
               }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
             }
         });
    }
}