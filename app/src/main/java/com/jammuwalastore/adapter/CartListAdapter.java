package com.jammuwalastore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jammuwalastore.ProductDetailActivity;
import com.jammuwalastore.R;
import com.jammuwalastore.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
    List<ProductModel> productsList = new ArrayList<>();
    private Context context;
    private onItemAdd onItemAdd;

    public CartListAdapter(Context context, List<ProductModel> productsList, onItemAdd onItemAdd) {
        this.context = context;
        this.productsList = productsList;
        this.onItemAdd = onItemAdd;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductModel productsData = productsList.get(position);
        holder.txtItemName.setText(productsData.getProductName());
        holder.txtTotalPrice.setText("Total Price : ₹" + productsData.getTotalPrice());
        holder.txtItemPrice.setText("Price : ₹" + productsData.getProductPrice());
        if (productsData.getTax_status().length() > 0
                && productsData.getTax_status().equalsIgnoreCase("taxable")) {
            try {
                long price = 0L;
                price = Long.parseLong(productsData.getTotalPrice());
                price *= Long.parseLong(productsData.getTax_class());
                price /= 100;
                holder.txtGST.setText("GST Charges: ₹" + price);
                holder.txtNarration.setVisibility(View.VISIBLE);
            } catch (Exception ignored) {
                holder.txtGST.setText("GST not applicable");
                holder.txtNarration.setVisibility(View.GONE);
            }
        } else {
            holder.txtGST.setText("GST not applicable");
            holder.txtNarration.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(productsData.getProductImage())
                .into(holder.txtItemImage);
        holder.txtItemVariant.setText(productsData.getProduct_weight() + "");


        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemAdd.onItemClick(true, position);
            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemAdd.onItemClick(false, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productName", productsData.getProductName());
                intent.putExtra("productId", productsData.getProductid() + "");
                context.startActivity(intent);
            }
        });

        holder.txtQuantity.setText(productsData.getProductQuantity() + "");

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView txtItemImage;
        private TextView txtItemPrice;
        private TextView txtGST;
        private TextView txtNarration;
        private TextView txtTotalPrice;
        private View viewCutPrice;
        private TextView txtItemName;
        private TextView txtQuantity;
        private TextView txtItemVariant;
        private ImageView imgMinus;
        private ImageView imgPlus;


        MyViewHolder(@NonNull View view) {
            super(view);
            txtItemImage = (ImageView) view.findViewById(R.id.txtItemImage);
            txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
            txtGST = (TextView) view.findViewById(R.id.txtGST);
            txtNarration = (TextView) view.findViewById(R.id.txtNarration);
            txtTotalPrice = (TextView) view.findViewById(R.id.txtTotalPrice);
            txtItemVariant = (TextView) view.findViewById(R.id.txtItemVariant);
            viewCutPrice = (View) view.findViewById(R.id.viewCutPrice);
            txtItemName = (TextView) view.findViewById(R.id.txtItemName);
            imgMinus = view.findViewById(R.id.imgMinus);
            imgPlus = view.findViewById(R.id.imgPlus);
            txtQuantity = view.findViewById(R.id.txtQuantity);
        }
    }

    public interface onItemAdd {
        public void onItemClick(boolean isPlus, int position);
    }
}
