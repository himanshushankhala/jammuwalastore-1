package com.jammuwalastore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.jammuwalastore.adapter.CategoryListAdapter;
import com.jammuwalastore.helper.Constants;
import com.jammuwalastore.model.CategoryModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    LinearLayout progressLayout;
    ImageView imgBack;
    List<CategoryModel> categoryModelList = new ArrayList<>();
    RecyclerView rvCategory;
    CategoryListAdapter categoryListAdapter;
    EditText edtSearchBar;
    boolean clickable = false;
    TextView txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressLayout = findViewById(R.id.progressLayout);
        imgBack = findViewById(R.id.imgBack);
        txtSearch = findViewById(R.id.txtSearch);
        rvCategory = findViewById(R.id.rvCategory);
        edtSearchBar = findViewById(R.id.edtSearchBar);
        txtSearch.setTextColor(Color.LTGRAY);
        edtSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtSearchBar.getText().toString().trim().length() > 0) {
                    clickable = true;
                    txtSearch.setTextColor(Color.WHITE);
                } else {
                    clickable = false;
                    txtSearch.setTextColor(Color.LTGRAY);
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickable) {
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("keyword", edtSearchBar.getText().toString().trim());
                    intent.putExtra("type", "products");
                    startActivity(intent);
                }
            }
        });
        getCategories();
    }

    private void getCategories() {
        progressLayout.setVisibility(View.VISIBLE);
        String url = Constants.Companion.getCategories();
        Log.e("url>>", url + "<<");
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressLayout.setVisibility(View.GONE);
                        Log.e("getOrdersDetail>>", response + "<<");
                        try {
                            categoryModelList = fromJsonList(response.toString(), CategoryModel.class);
                            if (categoryModelList.size() > 0) {
                                categoryListAdapter = new CategoryListAdapter(SearchActivity.this, categoryModelList, new CategoryListAdapter.onItemAdd() {
                                    @Override
                                    public void onItemClick(boolean isPlus, int position) {
                                        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                                        intent.putExtra("categoryID", categoryModelList.get(position).getId()+"");
                                        intent.putExtra("keyword", categoryModelList.get(position).getName()+"");
                                        intent.putExtra("type", "category");
                                        startActivity(intent);
                                    }
                                });
                                rvCategory.setAdapter(categoryListAdapter);
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressLayout.setVisibility(View.GONE);
                    }
                });
    }

    private static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        Object[] array = (Object[]) java.lang.reflect.Array.newInstance(clazz, 0);
        array = new Gson().fromJson(json, array.getClass());
        List<T> list = new ArrayList<T>();
        for (Object o : array) list.add(clazz.cast(o));
        return list;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Constants.Companion.getGOTOCART()) {
            finish();
        }
    }
}
