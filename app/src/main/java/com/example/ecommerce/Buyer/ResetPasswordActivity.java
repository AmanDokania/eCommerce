package com.example.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
private String check = "";
private TextView pageTitle,titleQuestion;
private EditText question1,question2,phoneNumber;
private Button verifyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check=  getIntent().getStringExtra("check").toString();
        pageTitle = findViewById(R.id.page_title);
        titleQuestion = findViewById(R.id.title_question);
        question1 = findViewById(R.id.question1);
        question2 = findViewById(R.id.question2);
        phoneNumber = findViewById(R.id.find_phone_number);
        verifyButton = findViewById(R.id.verify_btn);


    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);
        if(check.equals("settings"))
        {
           pageTitle.setText("Set Questions");
           titleQuestion.setText("Please set the Answer for the Following Security Question");
           verifyButton.setText("Set");
            displayPreviousAnswer();
           verifyButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
           setAnswer();

               }
           });
        }
        else if(check.equals("login"))
        {
              phoneNumber.setVisibility(View.VISIBLE);
              verifyButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      verfiyUser();
                  }
              });
        }
    }

    private void setAnswer() {
        String Answer1=question1.getText().toString();
        String Answer2=question2.getText().toString();

        if(TextUtils.isEmpty(Answer1) && TextUtils.isEmpty(Answer2))
        {
            Toast.makeText(ResetPasswordActivity.this,"Please Write Answer of both Questions",Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference ref=  FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.currentonlineUser.getPhone());

            HashMap<String,Object> dataMap  = new HashMap<>();
            dataMap.put("answer1",Answer1);
            dataMap.put("answer2",Answer2);

            ref.child("Security Question").updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this,"You have answer the Question Successfully.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displayPreviousAnswer()
    {
        DatabaseReference ref=  FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.currentonlineUser.getPhone()).child("Security Question");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String ans1=snapshot.child("answer1").getValue().toString();
                            String ans2=snapshot.child("answer2").getValue().toString();
                            question1.setText(ans1);
                            question2.setText(ans2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void verfiyUser() {
        final String phone = phoneNumber.getText().toString();
        final String Answer1=question1.getText().toString();
        final String Answer2=question2.getText().toString();

        if(!phone.equals("") && !Answer1.equals("") && !Answer2.equals("")) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        //  String mphone = snapshot.child("phone").getValue().toString();

                        if (snapshot.hasChild("Security Question")) {
                            String ans1 = snapshot.child("Security Question").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Question").child("answer2").getValue().toString();
                            if (!ans1.equals(Answer1)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your First Answer is Wrong", Toast.LENGTH_SHORT).show();
                            } else if (!ans2.equals(Answer2)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your Second Answer is Wrong", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password ");
                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write Password here..");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (!newPassword.getText().toString().equals("")) {
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this, "Password is updated successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the Sequrity Questions", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                            Toast.makeText(ResetPasswordActivity.this, "This Phone Number is not exist.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(ResetPasswordActivity.this, " Please Complete the form", Toast.LENGTH_SHORT).show();
        }
    }

}