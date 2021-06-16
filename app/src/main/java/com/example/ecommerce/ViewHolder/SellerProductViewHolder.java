package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class SellerProductViewHolder extends RecyclerView.ViewHolder {
    public TextView txtProductName, txtProductDescription, txtProductPrice,txtProductstate;
    public ImageView imageView;
    public ItemClickListner listner;
    public SellerProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.product_seller_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_seller_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_seller_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_seller_price);
        txtProductstate = (TextView) itemView.findViewById(R.id.product_seller_state);
    }
}
