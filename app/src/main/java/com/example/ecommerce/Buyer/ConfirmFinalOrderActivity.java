package com.example.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
private Button confirmOrderbtn;
private String totalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        confirmOrderbtn = findViewById(R.id.confirm_final_btn);
        nameEditText = findViewById(R.id.shipment_name);
        phoneEditText = findViewById(R.id.shipment_phone_number);
        addressEditText = findViewById(R.id.shipment_address);
        cityEditText = findViewById(R.id.shipment_city);

        totalAmount = getIntent().getStringExtra("totalprice");
        Toast.makeText(ConfirmFinalOrderActivity.this,totalAmount+"$",Toast.LENGTH_SHORT).show();

        confirmOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please write Your Name",Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please write Your Phone Number",Toast.LENGTH_SHORT).show();
        }
       else  if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please write Your Proper Address",Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please write Your City Name",Toast.LENGTH_SHORT).show();
        }
       else{
           confirmOrder();
        }
    }

    private void confirmOrder() {

        String saveCurrentDate,saveCurrentTime="";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentonlineUser.getPhone());

        final HashMap<String ,Object> map = new HashMap<>();
        map.put("totalAmount",totalAmount);
        map.put("name",nameEditText.getText().toString());
        map.put("phone",phoneEditText.getText().toString());
        map.put("date",saveCurrentDate);
        map.put("time",saveCurrentTime);
        map.put("city",cityEditText.getText().toString());
        map.put("Address",addressEditText.getText().toString());
        map.put("state","not shipped");
        orderReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                .child(Prevalent.currentonlineUser.getPhone()).child("Products")
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ConfirmFinalOrderActivity.this,
                                    "Your Final Order has been Completed Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}