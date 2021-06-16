package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.Seller.SellerProductCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {
private Button applyButton,deletebtn;
private EditText name,description,price;
private ImageView imageView;
 private String productId="";
  private   DatabaseReference ProductsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        applyButton = findViewById(R.id.apply_change_btn);
        deletebtn = findViewById(R.id.delete_btn);
        name = findViewById(R.id.product_name_maintain);
        description  =findViewById(R.id.product_description_maintain);
        price = findViewById(R.id.product_price_maintain);
        imageView = findViewById(R.id.product_image_maintain);

        productId = getIntent().getStringExtra("pid");
       ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
       displayProductInfo();

       applyButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updateProductInfo();
           }
       });

       deletebtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               deleteProduct();
           }
       });
    }


    private void displayProductInfo() {
        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String name1=  snapshot.child("pname").getValue().toString();
                    String price1=  snapshot.child("price").getValue().toString();
                    String description1=  snapshot.child("description").getValue().toString();
                    String image1=  snapshot.child("image").getValue().toString();

                    name.setText(name1);
                    description.setText(description1);
                    price.setText(price1);
                    Picasso.get().load(image1).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateProductInfo() {
        String name1=  name.getText().toString().toString();
        String price1=  price.getText().toString().toString();
        String description1=  description.getText().toString().toString();

        if(TextUtils.isEmpty(name1))
        {
            Toast.makeText(AdminMaintainProductActivity.this,"Please Write Product Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price1))
        {
            Toast.makeText(AdminMaintainProductActivity.this,"Please Write Product Price",Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(description1))
        {
            Toast.makeText(AdminMaintainProductActivity.this,"Please Write Product Description",Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid",productId);
            productMap.put("description",description1);
            productMap.put("price",price1);
            productMap.put("pname", name1);

            ProductsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AdminMaintainProductActivity.this,"Changes Applied",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminMaintainProductActivity.this, SellerProductCategoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


    private void deleteProduct() {

        ProductsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainProductActivity.this,"Product is Deleted Successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMaintainProductActivity.this, SellerProductCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}