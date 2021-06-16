package com.example.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
private ImageView productImage;
private ElegantNumberButton numberButton;
private FloatingActionButton addToCartbtn;
private TextView productPrice,productDescription,productName;
String productId="",state="normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        addToCartbtn = findViewById(R.id.product_to_cart_item);
       numberButton = findViewById(R.id.add_number_btn);
          productImage = findViewById(R.id.product_image_details);
         productPrice = findViewById(R.id.product_price_details);
            productDescription = findViewById(R.id.product_description_details);
            productName = findViewById(R.id.product_name_details);

        productId = getIntent().getStringExtra("pid");
        getProductsDetail(productId);

        addToCartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("order Placed") || state.equals("order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this,"you can purchase more products,once you receive your first order.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });
        }

    @Override
    protected void onStart() {
        super.onStart();
        chehcOrderState();
    }

    private void addingToCartList() {
        String saveCurrentDate,saveCurrentTime="";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calendar.getTime());

        final DatabaseReference cartListReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String ,Object>map = new HashMap<>();
        map.put("pid",productId);
        map.put("pname",productName.getText().toString());
        map.put("price",productPrice.getText().toString());
        map.put("date",saveCurrentDate);
        map.put("time",saveCurrentTime);
        map.put("quantity",numberButton.getNumber());
        map.put("discount","");
        cartListReference.child("User View").child(Prevalent.currentonlineUser.getPhone())
                .child("Products").child(productId).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            cartListReference.child("Admin View").child(Prevalent.currentonlineUser.getPhone())
                                    .child("Products").child(productId).updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this,"Added to cart List",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void getProductsDetail(final String productId) {
        DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        ProductsRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Products product = snapshot.getValue(Products.class);
                    productName.setText(product.getPname());
                    productPrice.setText(product.getPrice());
                    productDescription.setText(product.getDescription());
                    Picasso.get().load(product.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void  chehcOrderState()
    {
        DatabaseReference orderRef =  FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String shippingState = snapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        state = "order Shipped";


                    }
                    else if(shippingState.equals("not shipped"))
                    {

                        state = "order Placed";

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}