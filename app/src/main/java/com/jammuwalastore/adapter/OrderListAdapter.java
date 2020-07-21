package com.jammuwalastore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jammuwalastore.R;
import com.jammuwalastore.model.OrderModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    List<OrderModel> productsList = new ArrayList<>();
    private Context context;

    public OrderListAdapter(Context context, List<OrderModel> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_orders, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderModel productsData = productsList.get(position);
        holder.txtOrderId.setText("Order Id : #" + productsData.getNumber());
        holder.txtTotalPrice.setText("Total Price : ₹" + productsData.getTotal());
        holder.txtStatus.setText(productsData.getStatus());
        holder.txtDate.setText("Date : " + changeDateFormat(productsData.getDate_created()));
        holder.txtPaymentMethod.setText("Payment Method : " + productsData.getPayment_method_title());
        holder.orderSubListAdapter = new OrderSubListAdapter(context, productsData.getLine_items());
        holder.rvItems.setAdapter(holder.orderSubListAdapter);
        holder.txtSeeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.txtSeeDetails.setVisibility(View.GONE);
                holder.linDetails.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderId;
        private TextView txtStatus;
        private TextView txtTotalPrice;
        private TextView txtDate;
        private TextView txtPaymentMethod;
        private TextView txtSeeDetails;
        private LinearLayout linDetails;
        private RecyclerView rvItems;
        OrderSubListAdapter orderSubListAdapter;

        MyViewHolder(@NonNull View view) {
            super(view);
            txtOrderId = view.findViewById(R.id.txtOrderId);
            txtStatus = view.findViewById(R.id.txtStatus);
            txtTotalPrice = (TextView) view.findViewById(R.id.txtTotalPrice);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtPaymentMethod = (TextView) view.findViewById(R.id.txtPaymentMethod);
            txtSeeDetails = (TextView) view.findViewById(R.id.txtSeeDetails);
            rvItems = view.findViewById(R.id.rvItems);
            linDetails = view.findViewById(R.id.linDetails);
        }
    }

    public String changeDateFormat(String time) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
