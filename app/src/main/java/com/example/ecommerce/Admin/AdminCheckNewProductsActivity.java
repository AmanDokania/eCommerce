package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewProductsActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private DatabaseReference unverifiedProductRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_products);

        recyclerView = findViewById(R.id.admin_products_check_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        unverifiedProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products>options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedProductRef.orderByChild("productstatus").equalTo("Not Approved"),Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
               new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products model) {
                       holder.txtProductName.setText(model.getPname());
                       holder.txtProductDescription.setText(model.getDescription());
                       holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                       Picasso.get().load(model.getImage()).into(holder.imageView);

                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               final String productid = model.getPid();
                               CharSequence charSequence[]=new CharSequence[]{
                                       "Yes",
                                       "No"
                               };
                               AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewProductsActivity.this);
                               builder.setTitle("Do you wnat to approved this product.  Are you sure??");
                               builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int i) {
                                       if(i==0)
                                       {
                                          CahgeProductstate(productid);
                                       }
                                       if(i==1)
                                       {

                                       }
                                   }
                               });
                               builder.show();
                           }
                       });
                   }

                   @NonNull
                   @Override
                   public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item, parent, false);
                       ProductViewHolder holder = new ProductViewHolder(view);
                       return holder;
                   }
               };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CahgeProductstate(String productid) {
        unverifiedProductRef.child(productid).child("productstatus").setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminCheckNewProductsActivity.this,
                                "This Item has been Approved and now it is available for sale from seller",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}





