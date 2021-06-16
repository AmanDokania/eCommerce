package com.example.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private Button NextProcessButton;
private TextView totalAmmount,msg1;
private int overTotalPrice,Item=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        NextProcessButton = findViewById(R.id.next_process_btn);
        totalAmmount = findViewById(R.id.total_price);
        msg1=  findViewById(R.id.msg1);
        recyclerView =findViewById(R.id.cart_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        totalAmmount.setText("Total Price "+String.valueOf(overTotalPrice)+"$");
            NextProcessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Item==0)
                    {
                        Toast.makeText(CartActivity.this,"Please First add some item in your cart list",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                        intent.putExtra("totalprice", String.valueOf(overTotalPrice));
                        startActivity(intent);
                    }


                }
            });

    }

    @Override
    protected void onStart() {
        super.onStart();
       chehcOrderState();

     overTotalPrice =0;
        final DatabaseReference cartListReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListReference.child("User View").child(Prevalent.currentonlineUser.getPhone()).child("Products"),Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int position, @NonNull final Cart cart) {
               cartViewHolder.productName.setText(cart.getPname());
               cartViewHolder.productPrice.setText("Price "+cart.getPrice() +"$");
               cartViewHolder.productQuantity.setText("Quantity "+cart.getQuantity());
                Item++;

               int oneTypeProductPrice = (Integer.valueOf(cart.getPrice()))*(Integer.valueOf(cart.getQuantity()));
               overTotalPrice+=oneTypeProductPrice;
                totalAmmount.setText("Total Price "+String.valueOf(overTotalPrice)+"$");
               cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       CharSequence charSequence [] =new CharSequence []{
                               "Edit",
                               "Remove"
                       };
                       AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                       builder.setTitle("Cart Options: ");
                       builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int i) {
                               if(i==0)
                               {
                                   Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                   intent.putExtra("pid",cart.getPid());
                                   startActivity(intent);
                               }
                               if(i==1)
                               {
                                   cartListReference.child("User View").child(Prevalent.currentonlineUser.getPhone())
                                           .child("Products").child(cart.getPid())
                                           .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           cartListReference.child("Admin View").child(Prevalent.currentonlineUser.getPhone())
                                                   .child("Products").child(cart.getPid())
                                                   .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   Toast.makeText(CartActivity.this,"Item Delete Successfully",Toast.LENGTH_SHORT).show();
                                                   Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                   startActivity(intent);
                                               }
                                           });

                                       }
                                   });
                               }
                           }
                       });
                       builder.show();
                   }
               });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                CartViewHolder cartViewHolder = new CartViewHolder(view);
                        return cartViewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


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
                    String userName = snapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        totalAmmount.setText("Dear "+userName +" \n  order is shippped successfully");
                        recyclerView.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        msg1.setText("Congtrulation, Your order has been Placed Successfully. Soon you will receive your order" +
                                "at your door step");
                        NextProcessButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"you can purchase more products,once you receive your first order.",Toast.LENGTH_SHORT).show();

                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        totalAmmount.setText("Dear "+userName +" \n  Your order is not  shippped ");
                        recyclerView.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        NextProcessButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"you can purchase more products,once you receive your first order.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}