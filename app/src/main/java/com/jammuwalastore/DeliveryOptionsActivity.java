package com.jammuwalastore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jammuwalastore.dataBase.DBHandler;
import com.jammuwalastore.helper.Constants;
import com.jammuwalastore.helper.SessionManager;
import com.jammuwalastore.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Credentials;

public class DeliveryOptionsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack;
    TextView txtCOD;
    SessionManager sessionManager;
    DBHandler dbHandler;
    LinearLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_options);
        init();
    }

    private void init() {
        dbHandler = new DBHandler(this);
        sessionManager = new SessionManager(this);
        progressLayout = findViewById(R.id.progressLayout);
        imgBack = findViewById(R.id.imgBack);
        txtCOD = findViewById(R.id.txtCOD);
        imgBack.setOnClickListener(this);
        txtCOD.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == imgBack) {
            onBackPressed();
        } else if (view == txtCOD) {
            proceedOrder();
        }
    }

    private void proceedOrder() {
        progressLayout.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            List<ProductModel> productModelList = dbHandler.getAllProducts();
            for (ProductModel productModel : productModelList) {
                try {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("product_id", Long.parseLong(productModel.getProductid()));
                    jsonObject1.put("quantity", productModel.getProductQuantity());
                    jsonArray.put(jsonObject1);
                } catch (Exception ignored) {
                }
            }
            jsonObject.put("line_items", jsonArray);
            jsonObject.put("customer_note", "");

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("first_name", sessionManager.getFirst_name());
            jsonObject1.put("last_name", sessionManager.getLast_name());
            jsonObject1.put("company", "N/A");
            jsonObject1.put("address_1", sessionManager.getAddress());
            jsonObject1.put("address_2", sessionManager.getAddress_2());
            jsonObject1.put("city", sessionManager.getCity());
            jsonObject1.put("state", sessionManager.getState());
            jsonObject1.put("postcode", sessionManager.getPostcode());
            jsonObject1.put("country", sessionManager.getCountry());
            jsonObject1.put("email", sessionManager.getEmail());
            jsonObject1.put("phone", sessionManager.getMobile_no());

            jsonObject.put("billing", jsonObject1);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("first_name", sessionManager.getFirst_name());
            jsonObject2.put("last_name", sessionManager.getLast_name());
            jsonObject2.put("address_1", sessionManager.getAddress());
            jsonObject2.put("address_2", sessionManager.getAddress_2());
            jsonObject2.put("city", sessionManager.getCity());
            jsonObject2.put("state", sessionManager.getState());
            jsonObject2.put("postcode", sessionManager.getPostcode());
            jsonObject2.put("country", sessionManager.getCountry());

            jsonObject.put("shipping", jsonObject2);

            Log.e("order parms=>", jsonObject.toString() + "");
            doOrder(jsonObject);
        } catch (Exception ignored) {
            progressLayout.setVisibility(View.GONE);
        }
    }


    private void doOrder(JSONObject jsonObject) {
        String url = Constants.Companion.getOrderURL();
        Log.e("url>>", url + "<<");
        AndroidNetworking.post(url)
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", Credentials.basic(sessionManager.getLoginToken(), sessionManager.getLoginTokenPassword()))
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("doOrder>>", response + "<<");
                        progressLayout.setVisibility(View.GONE);
                        try {
                            String orderID = response.getString("number");
                            String total = response.getString("total");
                            dbHandler.deleteAll();
                            startActivity(new Intent(DeliveryOptionsActivity.this, ThanksActivity.class)
                                    .putExtra("orderID", orderID));
                            finish();

                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressLayout.setVisibility(View.GONE);
                        Log.e("error", anError.getErrorBody().toString() + "<<");
                        try {
                            JSONObject jsonObject1 = new JSONObject(anError.getErrorBody());
                            Toast.makeText(DeliveryOptionsActivity.this, (jsonObject1.optString("message")) + "", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    }
                });
    }
}
