package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserActivity extends AppCompatActivity {
private RecyclerView productList;
private DatabaseReference cartListRef;
String userId= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        productList = findViewById(R.id.products_list);
        productList.setHasFixedSize(true);
        productList.setLayoutManager(new LinearLayoutManager(this));

        userId = getIntent().getStringExtra("uid");
         cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                 .child("Admin View").child(userId).child("Products");
        Toast.makeText(AdminUserActivity.this,userId,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart>options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                CartViewHolder cartViewHolder = new CartViewHolder(view);
                return cartViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.productName.setText(cart.getPname());
                cartViewHolder.productPrice.setText("Price "+cart.getPrice() +"$");
                cartViewHolder.productQuantity.setText("Quantity "+cart.getQuantity());
            }
        };
        productList.setAdapter(adapter);
        adapter.startListening();
    }
}