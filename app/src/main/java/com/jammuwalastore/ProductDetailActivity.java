package com.jammuwalastore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.jammuwalastore.adapter.ProductImagesAdapter;
import com.jammuwalastore.dataBase.DBHandler;
import com.jammuwalastore.helper.Constants;
import com.jammuwalastore.model.ProductModel;
import com.jammuwalastore.model.ProductsData;
import com.jammuwalastore.model.images;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
    int currentPage;
    Timer timer;
    ImageView imgBack;
    TextView txtTitle;
    TextView txtProductName;
    String productId = "";
    String productName = "";
    ViewPager viewPagerProductImages;
    List<String> productImages = new ArrayList<>();
    ProductImagesAdapter productImagesAdapter;
    LinearLayout layoutProgress;
    private TextView txtItemPrice;
    private TextView txtCutPrice;
    private TextView txtDetails;
    private TextView txtItemVariant;
    private TextView txtCartItemsCount;
    private DBHandler dbHandler;
    ProductsData productsData;
    private RelativeLayout relAddItem;
    private RelativeLayout relPlusMinus;
    ImageView imgSearch;
    ImageView imgMinus;
    TextView txtQuantity;
    ImageView imgPlus;
    RelativeLayout relCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        init();
    }

    private void init() {
        dbHandler = new DBHandler(this);
        imgSearch = findViewById(R.id.imgSearch);
        imgMinus = findViewById(R.id.imgMinus);
        txtQuantity = findViewById(R.id.txtQuantity);
        relCart = findViewById(R.id.relCart);
        imgPlus = findViewById(R.id.imgPlus);
        imgBack = findViewById(R.id.imgBack);
        txtTitle = findViewById(R.id.txtTitle);
        relAddItem = findViewById(R.id.relAddItem);
        relPlusMinus = findViewById(R.id.relPlusMinus);
        txtCartItemsCount = findViewById(R.id.txtCartItemsCount);
        txtProductName = findViewById(R.id.txtProductName);
        viewPagerProductImages = findViewById(R.id.viewPagerProductImages);
        layoutProgress = findViewById(R.id.layoutProgress);
        txtItemPrice = findViewById(R.id.txtItemPrice);
        txtCutPrice = findViewById(R.id.txtCutPrice);
        txtDetails = findViewById(R.id.txtDetails);
        txtItemVariant = findViewById(R.id.txtItemVariant);

        productId = getIntent().getStringExtra("productId");
        productName = getIntent().getStringExtra("productName");

        txtTitle.setText(productName);
        txtProductName.setText(productName);
        imgBack.setOnClickListener(this);
        relAddItem.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
        imgPlus.setOnClickListener(this);
        relCart.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        layoutProgress.setVisibility(View.VISIBLE);
        getProductDetails();


    }

    private void setUpViewPagerAutoScroll() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == productImages.size()) {
                    currentPage = 0;
                }
                viewPagerProductImages.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onClick(View view) {
        if (view == imgBack) {
            onBackPressed();
        } else if (view == relAddItem) {
            relAddItem.setVisibility(View.GONE);
            relPlusMinus.setVisibility(View.VISIBLE);
            addRemoveItemFromCart(true);
        } else if (view == imgPlus) {
            addRemoveItemFromCart(true);
        } else if (view == imgMinus) {
            addRemoveItemFromCart(false);
        } else if (view == relCart) {
            Constants.Companion.setGOTOCART(true);
            finish();
        } else if (view == imgSearch) {
            startActivity(new Intent(ProductDetailActivity.this, SearchActivity.class));
            finish();
        }
    }

    private void getProductDetails() {
        String url = Constants.Companion.getProduct_Details_url()+"/" + productId;
        Log.e("url>>", url + "<<");
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        productsData = new Gson().fromJson(response.toString(), ProductsData.class);
                        setupProductPage();
                        Log.e("productsData>>", productsData.getName() + "<<");
                        layoutProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", anError.toString() + "<<");
                        layoutProgress.setVisibility(View.GONE);
                    }
                });
    }

    private void setupProductPage() {
        txtItemPrice.setText("₹" + productsData.getPrice());
        txtCutPrice.setText("₹" + productsData.getRegular_price());
        txtProductName.setText(productsData.getName());
        txtDetails.setText(Html.fromHtml(productsData.getDescription()));

        if (productsData.getImages() != null && productsData.getImages().size() > 0) {
            for (images url : productsData.getImages()) {
                productImages.add(url.getSrc());
            }
            productImagesAdapter = new ProductImagesAdapter(this, productImages);
            viewPagerProductImages.setAdapter(productImagesAdapter);
            if (productImages.size() > 1) {
                setUpViewPagerAutoScroll();
            }
        }

        if (productsData.getAttributes() != null && productsData.getAttributes().size() > 0) {
            if (productsData.getAttributes().get(0).getOptions() != null
                    && Objects.requireNonNull(productsData.getAttributes().get(0).getOptions()).size() > 0) {
                productsData.setWeight(Objects.requireNonNull(productsData.getAttributes().get(0).getOptions()).get(0).toString());
                txtItemVariant.setVisibility(View.VISIBLE);
                txtItemVariant.setText(String.format("%s",
                        Objects.requireNonNull(productsData.getAttributes().get(0).getOptions()).get(0).toString()));
            } else {
                txtItemVariant.setVisibility(View.GONE);
            }
        } else {
            txtItemVariant.setVisibility(View.GONE);
        }

        setCartValue();
    }

    private void addRemoveItemFromCart(boolean isPlus) {
        if (isPlus) {
            int quantity = 0;
            if (productsData == null) {
                return;
            }

            if (productsData.getQuantity() > 0) {
                productsData.setQuantity(productsData.getQuantity() + 1);
                quantity = productsData.getQuantity();
            } else {
                productsData.setQuantity(1);
                quantity = productsData.getQuantity();
            }

            ProductModel productModel = new ProductModel();
            productModel.setProductName(productsData.getName());
            productModel.setProductid(productsData.getId() + "");
            productModel.setProductPrice(productsData.getPrice());
            productModel.setProductActualPrice(productsData.getRegular_price());
            productModel.setTax_class(productsData.getTax_class());
            productModel.setTax_status(productsData.getTax_status());
            try {
                productModel.setProductImage(productsData.getImages().get(0).getSrc());
            } catch (Exception ignored) {
                productModel.setProductImage("");
            }
            productModel.setProductQuantity(productsData.getQuantity());
            productModel.setProduct_weight(productsData.getWeight());
            try {
                long price = 0L;
                price = Long.parseLong(productsData.getPrice());
                price *= quantity;
                productModel.setTotalPrice(Long.toString(price));
            } catch (Exception e) {
                productModel.setTotalPrice(productsData.getPrice());
            }
            if (quantity == 1) {
                dbHandler.addProduct(productModel);
            } else {
                dbHandler.updateProducts(productModel);
            }
        } else {
            int quantity = 0;
            productsData.setQuantity(productsData.getQuantity() - 1);
            quantity = productsData.getQuantity();

            if (quantity < 1) {
                dbHandler.deleteProduct(productsData.getId() + "", productsData.getWeight());
            } else {
                ProductModel productModel = new ProductModel();
                productModel.setProductName(productsData.getName());
                productModel.setProductid(productsData.getId() + "");
                productModel.setProductPrice(productsData.getPrice());
                productModel.setProductActualPrice(productsData.getRegular_price());
                productModel.setTax_class(productsData.getTax_class());
                productModel.setTax_status(productsData.getTax_status());
                try {
                    productModel.setProductImage(productsData.getImages().get(0).getSrc());
                } catch (Exception ignored) {
                    productModel.setProductImage("");
                }
                productModel.setProductQuantity(productsData.getQuantity());
                productModel.setProduct_weight(productsData.getWeight());
                try {
                    long price = 0L;
                    price = Long.parseLong(productsData.getPrice());
                    price *= quantity;
                    productModel.setTotalPrice(Long.toString(price));
                } catch (Exception e) {
                    productModel.setTotalPrice(productsData.getPrice());
                }

                dbHandler.updateProducts(productModel);
            }


        }
        setCartValue();
    }

    private void setCartValue() {
        List<ProductModel> list = dbHandler.getAllProducts();
        if (list != null && list.size() > 0) {
            txtCartItemsCount.setVisibility(View.VISIBLE);
            txtCartItemsCount.setText(String.format(Locale.ENGLISH, "%d", list.size()));
        } else {
            txtCartItemsCount.setVisibility(View.GONE);
        }
        boolean haveItems = false;
        assert list != null;
        for (ProductModel productModel : list) {
            if (productModel.getProductid().equalsIgnoreCase(productsData.getId() + "")) {
                if (productModel.getProductQuantity() > 0) {
                    productsData.setQuantity(productModel.getProductQuantity());
                    txtQuantity.setText(String.format(Locale.ENGLISH, "%d", productModel.getProductQuantity()));
                    relAddItem.setVisibility(View.GONE);
                    relPlusMinus.setVisibility(View.VISIBLE);
                    haveItems = true;
                } else {
                    relAddItem.setVisibility(View.VISIBLE);
                    relPlusMinus.setVisibility(View.GONE);
                }
                break;
            }
        }
        if (!haveItems) {
            relAddItem.setVisibility(View.VISIBLE);
            relPlusMinus.setVisibility(View.GONE);
        }
    }
}
