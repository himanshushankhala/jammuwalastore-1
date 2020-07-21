package com.jammuwalastore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jammuwalastore.R;
import com.jammuwalastore.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {
    List<CategoryModel> productsList = new ArrayList<>();
    private Context context;
    private onItemAdd onItemAdd;

    public CategoryListAdapter(Context context, List<CategoryModel> productsList, onItemAdd onItemAdd) {
        this.context = context;
        this.productsList = productsList;
        this.onItemAdd = onItemAdd;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryModel productsData = productsList.get(position);
        holder.txtCategory.setText(productsData.getName());
        holder.txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemAdd.onItemClick(true,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCategory;

        MyViewHolder(@NonNull View view) {
            super(view);
            txtCategory = view.findViewById(R.id.txtCategory);
        }
    }

    public interface onItemAdd {
        public void onItemClick(boolean isPlus, int position);
    }
}
