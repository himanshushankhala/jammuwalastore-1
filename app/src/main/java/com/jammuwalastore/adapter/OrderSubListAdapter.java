package com.jammuwalastore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jammuwalastore.R;
import com.jammuwalastore.model.line_items;

import java.util.ArrayList;
import java.util.List;

public class OrderSubListAdapter extends RecyclerView.Adapter<OrderSubListAdapter.MyViewHolder> {
    List<line_items> productsList = new ArrayList<>();
    private Context context;

    public OrderSubListAdapter(Context context, List<line_items> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sub_item_orders, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        line_items productsData = productsList.get(position);
        holder.txtItemName.setText(productsData.getName());
        holder.txtMRP.setText("M.R.P. : ₹" + productsData.getPrice());
        holder.txtQuantity.setText("Quantity : " + productsData.getQuantity() + "");
        if (productsData.getTotal_tax() != null
                && productsData.getTotal_tax().length() > 0
                && !(productsData.getTotal_tax().equalsIgnoreCase("0"))
                && !(productsData.getTotal_tax().equalsIgnoreCase("0.00"))
        ) {
            holder.txtGstCharges.setText("GST Charges : ₹" + productsData.getTotal_tax().replaceAll(".00", "") + "");
            holder.txtNarration.setVisibility(View.VISIBLE);
        } else {
            holder.txtGstCharges.setText("GST not applicable");
            holder.txtNarration.setVisibility(View.GONE);
        }
        try {
            long price = Long.parseLong(productsData.getTotal_tax().replace(".00", ""));
            price = price + productsData.getTotal();
            holder.txtTotalPrice.setText("Total Price : ₹" + price);
        } catch (Exception e) {
            holder.txtTotalPrice.setText("Total Price : ₹" + productsData.getTotal());
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemName;
        private TextView txtTotalPrice;
        private TextView txtMRP;
        private TextView txtNarration;
        private TextView txtQuantity;
        private TextView txtGstCharges;


        MyViewHolder(@NonNull View view) {
            super(view);
            txtItemName = view.findViewById(R.id.txtItemName);
            txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
            txtMRP = view.findViewById(R.id.txtMRP);
            txtQuantity = view.findViewById(R.id.txtQuantity);
            txtGstCharges = view.findViewById(R.id.txtGstCharges);
            txtNarration = view.findViewById(R.id.txtNarration);
        }
    }
}
