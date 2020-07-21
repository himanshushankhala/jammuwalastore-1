package com.jammuwalastore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.jammuwalastore.R;

import java.util.ArrayList;
import java.util.List;

public class ProductImagesAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> productImages=new ArrayList<>();
    private LayoutInflater mLayoutInflater;


    public ProductImagesAdapter(Context context, List<String> productImages) {
        this.productImages = productImages;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_product_images, container, false);
        ImageView imageView = itemView.findViewById(R.id.productImages);
        Glide.with(mContext).load(productImages.get(position)).into(imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}