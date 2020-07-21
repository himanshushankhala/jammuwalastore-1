package com.jammuwalastore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.jammuwalastore.adapter.ProductListAdapter;
import com.jammuwalastore.dataBase.DBHandler;
import com.jammuwalastore.helper.Constants;
import com.jammuwalastore.model.ProductModel;
import com.jammuwalastore.model.ProductsData;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTitleKey;
    ImageView imgBack;
    String keyword = "";
    String categoryID = "";
    String type = "";
    int pageNumber = 0;
    RelativeLayout relCart;
    TextView txtCartItemsCount;
    DBHandler dbHandler;
    List<ProductsData> productsDataArrayList = new ArrayList<>();
    ProductListAdapter productListAdapter;
    RecyclerView rvProductsList;
    LinearLayout linNoItems;
    LinearLayout layoutProgress;
    int totalItemCount = 0;
    int lastVisibleItem = 0;
    private int visibleThreshold = 50;
    boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        dbHandler = new DBHandler(this);
        linNoItems = findViewById(R.id.linNoItems);
        layoutProgress = findViewById(R.id.progressLayout);
        imgBack = findViewById(R.id.imgBack);
        relCart = findViewById(R.id.relCart);
        rvProductsList = findViewById(R.id.rvProductsList);
        txtCartItemsCount = findViewById(R.id.txtCartItemsCount);
        txtTitleKey = findViewById(R.id.txtTitleKey);
        type = getIntent().getStringExtra("type");
        keyword = getIntent().getStringExtra("keyword");
        txtTitleKey.setText("\"" + keyword + "\"");


        if (type.equalsIgnoreCase("category")) {
            categoryID = getIntent().getStringExtra("categoryID");
        }

        productListAdapter = new ProductListAdapter(this, productsDataArrayList, new ProductListAdapter.onItemAdd() {
            @Override
            public void onItemClick(boolean isPlus, int position) {
                addRemoveItemFromCart(isPlus, position);
            }
        });
        rvProductsList.setAdapter(productListAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        relCart.setOnClickListener(this);
        setCartValue();


        rvProductsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvProductsList.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    //End of the items
                    pageNumber = pageNumber + 1;
                    getProducts();
                    loading = true;

                }
            }
        });

        pageNumber = pageNumber + 1;
        getProducts();
    }


    private void addRemoveItemFromCart(boolean isPlus, int position) {
        if (isPlus) {
            int quantity = 0;
            if (productsDataArrayList.get(position).getQuantity() > 0) {
                productsDataArrayList.get(position).setQuantity(productsDataArrayList.get(position).getQuantity() + 1);
                quantity = productsDataArrayList.get(position).getQuantity();
            } else {
                productsDataArrayList.get(position).setQuantity(1);
                quantity = productsDataArrayList.get(position).getQuantity();
            }
            ProductsData productAPIModel = productsDataArrayList.get(position);
            ProductModel productModel = new ProductModel();
            productModel.setProductName(productAPIModel.getName());
            productModel.setProductid(productAPIModel.getId() + "");
            productModel.setProductPrice(productAPIModel.getPrice());
            productModel.setProductActualPrice(productAPIModel.getRegular_price());
            productModel.setTax_class(productAPIModel.getTax_class());
            productModel.setTax_status(productAPIModel.getTax_status());
            try {
                productModel.setProductImage(productAPIModel.getImages().get(0).getSrc());
            } catch (Exception e) {
                productModel.setProductImage("");
            }
            productModel.setProductQuantity(quantity);
            try {
                if (productAPIModel.getAttributes().get(0).getOptions().get(0).length() > 0) {
                    productModel.setProduct_weight(productAPIModel.getAttributes().get(0).getOptions().get(0));
                } else {
                    productModel.setProduct_weight("");
                }
            } catch (Exception e) {
                productModel.setProduct_weight("");
            }

            try {
                long price = 0L;
                price = Long.parseLong(productAPIModel.getPrice());
                price *= quantity;
                productModel.setTotalPrice(Long.toString(price));
            } catch (Exception ignored) {
                productModel.setTotalPrice(productAPIModel.getPrice());
            }

            if (quantity == 1) {
                dbHandler.addProduct(productModel);
            } else {
                dbHandler.updateProducts(productModel);
            }
        } else {
            int quantity = 0;
            productsDataArrayList.get(position).setQuantity(productsDataArrayList.get(position).getQuantity() - 1);

            quantity = productsDataArrayList.get(position).getQuantity();
            ProductsData productAPIModel = productsDataArrayList.get(position);

            if (quantity < 1) {
                dbHandler.deleteProduct(productAPIModel.getId() + "", productAPIModel.getWeight());
            } else {

                ProductModel productModel = new ProductModel();
                productModel.setProductName(productAPIModel.getName());
                productModel.setProductid(productAPIModel.getId() + "");
                productModel.setProductPrice(productAPIModel.getPrice());
                productModel.setProductActualPrice(productAPIModel.getRegular_price());
                productModel.setTax_class(productAPIModel.getTax_class());
                productModel.setTax_status(productAPIModel.getTax_status());
                try {
                    productModel.setProductImage(productAPIModel.getImages().get(0).getSrc());
                } catch (Exception e) {
                    productModel.setProductImage("");
                }
                productModel.setProductQuantity(quantity);
                try {
                    if (productAPIModel.getAttributes().get(0).getOptions().get(0).length() > 0) {
                        productModel.setProduct_weight(productAPIModel.getAttributes().get(0).getOptions().get(0));
                    } else {
                        productModel.setProduct_weight("");
                    }
                } catch (Exception e) {
                    productModel.setProduct_weight("");
                }

                try {
                    long price = 0L;
                    price = Long.parseLong(productAPIModel.getPrice());
                    price *= quantity;
                    productModel.setTotalPrice(Long.toString(price));
                } catch (Exception ignored) {
                    productModel.setTotalPrice(productAPIModel.getPrice());
                }
                dbHandler.updateProducts(productModel);
            }
        }

        productListAdapter.notifyDataSetChanged();
        // setupCartList();
        setupQuantity();
        setCartValue();
    }


    @Override
    public void onClick(View view) {
        if (view == relCart) {
            Constants.Companion.setGOTOCART(true);
            finish();
        }
    }

    private void setupQuantity() {
        if (dbHandler == null || productsDataArrayList.size() < 1) {
            return;
        }

        if (productListAdapter == null) {
            return;
        }

        for (ProductsData productsData : productsDataArrayList) {
            productsData.setQuantity(0);
        }

        List<ProductModel> list = dbHandler.getAllProducts();
        if (list != null && list.size() > 0) {
            for (ProductsData productsData : productsDataArrayList) {
                for (ProductModel productModel : list) {
                    if (productModel.getProductid().equalsIgnoreCase(productsData.getId() + "")) {
                        productsData.setQuantity(productModel.getProductQuantity());
                    }
                }
            }
        }
        productListAdapter.notifyDataSetChanged();

    }

    private void setCartValue() {
        List<ProductModel> list = dbHandler.getAllProducts();
        if (list != null && list.size() > 0) {
            txtCartItemsCount.setVisibility(View.VISIBLE);
            txtCartItemsCount.setText(String.format(Locale.ENGLISH, "%d", list.size()));
        } else {
            txtCartItemsCount.setVisibility(View.GONE);
        }
    }

    private void setupData() {
        if (productsDataArrayList.size() > 0) {
            productListAdapter.notifyDataSetChanged();
        } else {
            linNoItems.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            setupQuantity();
            setCartValue();
        } catch (Exception e) {
        }
    }

    private void getProducts() {
        layoutProgress.setVisibility(View.VISIBLE);
        String url;
        if (type.equalsIgnoreCase("products")) {
            url = Constants.Companion.getKeywordSearch() + "?stock_status=instock&search=\"" + keyword + "\"&per_page=" + visibleThreshold + "&page=" + pageNumber;
        } else {
            url = Constants.Companion.getCategoriesSearch() + "?stock_status=instock&per_page=" + visibleThreshold + "&page=" + pageNumber + "&category=" + categoryID;
        }
        Log.e("url>>", url + "<<");
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<ProductsData> productModelList = fromJsonList(response.toString(), ProductsData.class);
                        if (productModelList.size() > 0 && (visibleThreshold == productModelList.size())) {
                            loading = false;
                        }
                        productsDataArrayList.addAll(productModelList);
                        setupData();
                        setupQuantity();
                        Log.e("productsDataArrayList>>", productsDataArrayList.size() + "<<");
                        layoutProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", anError.toString() + "");
                        layoutProgress.setVisibility(View.GONE);
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
}
