package com.example.ecommerce.Admin;

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

import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {
private RecyclerView orderList;
private DatabaseReference orderRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef  = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
        orderList.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminViewHolder>adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminViewHolder adminViewHolder, final int position, @NonNull final AdminOrders adminOrders) {
                adminViewHolder.userName.setText("Name: "+adminOrders.getName());
                adminViewHolder.userPhoneNumber.setText("Phone No. : "+adminOrders.getPhone());
                adminViewHolder.userCity.setText("Shipping Address: "+adminOrders.getAddress()+" "+adminOrders.getCity());
                adminViewHolder.userTotlPrice.setText("Total Price : "+adminOrders.getTotalAmount()+"$");
                adminViewHolder.userDateTime.setText("Oreder at: "+adminOrders.getDate() +" "+adminOrders.getTime());

                adminViewHolder.showOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            String uid=  getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserActivity.class);
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                    }
                });

                adminViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence charSequence[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("Have you shipped this order product ?");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                               if(i==0)
                               {
                                   String uid=  getRef(position).getKey();
                                   removeOrder(uid);
                               }
                               else{
                                   finish();
                               }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
               return new AdminViewHolder(view);
            }
        };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static  class AdminViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName,userPhoneNumber,userCity,userDateTime,userTotlPrice;
        private Button showOrderButton;
        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userCity= itemView.findViewById(R.id.order_address_city);
            userDateTime = itemView.findViewById(R.id.order_time_date);
            userTotlPrice = itemView.findViewById(R.id.order_total_price);
            showOrderButton = itemView.findViewById(R.id.show_all_products_btn);

        }
    }


    private void removeOrder(String uid) {

        orderRef.child(uid).removeValue();
    }

}