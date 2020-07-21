package com.jammuwalastore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jammuwalastore.ProductDetailActivity;
import com.jammuwalastore.R;
import com.jammuwalastore.model.ProductsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    private List<ProductsData> productsList = new ArrayList<>();
    private Context context;
    private onItemAdd onItemAdd;

    public ProductListAdapter(Context context, List<ProductsData> productsList,onItemAdd onItemAdd) {
        this.context = context;
        this.productsList = productsList;
        this.onItemAdd = onItemAdd;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_products, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductsData productsData = productsList.get(position);
        holder.txtItemName.setText(productsData.getName());
        holder.txtItemVariant.setText(productsData.getWeight());
        holder.txtCutPrice.setText("₹" + productsData.getRegular_price());
        holder.txtItemPrice.setText("₹" + productsData.getPrice());
        if (productsData.getImages() != null && productsData.getImages().size() > 0) {
            Glide.with(context)
                    .load(productsData.getImages().get(0).getSrc())
                    .into(holder.txtItemImage);
        } else {
            holder.txtItemImage.setImageDrawable(null);
        }

        if (productsData.getAttributes() != null && productsData.getAttributes().size() > 0) {
            if (productsData.getAttributes().get(0).getOptions() != null
                    && Objects.requireNonNull(productsData.getAttributes().get(0).getOptions()).size() > 0) {
                holder.txtItemVariant.setText(String.format("%s",
                        Objects.requireNonNull(productsData.getAttributes().get(0).getOptions()).get(0).toString()));
            } else {
                holder.txtItemVariant.setText("");
            }
        } else {
            holder.txtItemVariant.setText("");
        }


        holder.relAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.relAddItem.setVisibility(View.GONE);
                holder.relPlusMinus.setVisibility(View.VISIBLE);
                onItemAdd.onItemClick(true, position);
            }
        });

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
                intent.putExtra("productName", productsData.getName());
                intent.putExtra("productId", productsData.getId() + "");
                context.startActivity(intent);
            }
        });

        if (productsData.getQuantity() > 0) {
            holder.txtQuantity.setText(productsData.getQuantity() + "");
            holder.relAddItem.setVisibility(View.GONE);
            holder.relPlusMinus.setVisibility(View.VISIBLE);
        } else {
            holder.relAddItem.setVisibility(View.VISIBLE);
            holder.relPlusMinus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView txtItemImage;
        private TextView txtItemPrice;
        private TextView txtCutPrice;
        private View viewCutPrice;
        private TextView txtItemName;
        private TextView txtQuantity;
        private TextView txtItemVariant;
        private RelativeLayout relPlusMinus;
        private RelativeLayout relAddItem;
        private ImageView imgMinus;
        private ImageView imgPlus;


        MyViewHolder(@NonNull View view) {
            super(view);
            txtItemImage = (ImageView) view.findViewById(R.id.txtItemImage);
            txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
            txtCutPrice = (TextView) view.findViewById(R.id.txtCutPrice);
            txtItemVariant = (TextView) view.findViewById(R.id.txtItemVariant);
            viewCutPrice = (View) view.findViewById(R.id.viewCutPrice);
            txtItemName = (TextView) view.findViewById(R.id.txtItemName);
            relAddItem = (RelativeLayout) view.findViewById(R.id.relAddItem);
            relPlusMinus = view.findViewById(R.id.relPlusMinus);
            imgMinus = view.findViewById(R.id.imgMinus);
            imgPlus = view.findViewById(R.id.imgPlus);
            txtQuantity = view.findViewById(R.id.txtQuantity);
        }
    }

    public interface onItemAdd {
        public void onItemClick(boolean isPlus, int position);
    }
}
