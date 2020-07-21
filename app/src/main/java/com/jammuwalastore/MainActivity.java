package com.jammuwalastore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;
import com.jammuwalastore.dataBase.DBHandler;
import com.androidnetworking.AndroidNetworking;
import com.jammuwalastore.helper.Constants;
import com.jammuwalastore.helper.SessionManager;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.Credentials;

import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SessionManager sessionManager;
    public TextView txtUserName;
    public TextView txtTitle;
    public TextView txtCartItemsCount;
    public RelativeLayout relCart;
    public ImageView imgSearch;
    NavController navController;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        dbHandler = new DBHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  toolbar.setTitleTextAppearance(this, R.style.ToolbarCustomStyle);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        txtTitle = findViewById(R.id.txtTitle);
        relCart = findViewById(R.id.relCart);
        imgSearch = findViewById(R.id.imgSearch);
        txtCartItemsCount = findViewById(R.id.txtCartItemsCount);
        NavigationView navigationView = findViewById(R.id.nav_view);
        txtUserName = navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        MenuItem logOutItem = navigationView.getMenu().findItem(R.id.nav_logout);
        logOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dbHandler.deleteAll();
                sessionManager.logoutUser(MainActivity.this);
                return false;
            }
        });


        MenuItem nav_login = navigationView.getMenu().findItem(R.id.nav_login);
        nav_login.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                startActivity(loginIntent);
                finish();
                return false;
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_cart, R.id.nav_my_orders, R.id.nav_my_profile, R.id.nav_contact_us)
                .setDrawerLayout(drawer)
                .build();

        navController = findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (sessionManager.isLoggedIn()) {
            txtUserName.setText("");
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            getUserDetails();
        } else {
            txtUserName.setText(getString(R.string.guest));
            navigationView.getMenu().findItem(R.id.nav_my_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_my_orders).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_my_cart).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
        relCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != R.id.nav_my_cart) {
                    navController.navigate(R.id.nav_my_cart);
                }

            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void shareAPP() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out Minzare UK app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void getUserDetails() {
        String url = Constants.Companion.getUser_Details_url();
        Log.e("url>>", url + "<<");
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", Credentials.basic(sessionManager.getLoginToken(), sessionManager.getLoginTokenPassword()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("getUserDetails>>", response + "<<");
                        try {
                            JSONObject jsonObject = response.getJSONObject("data");
                            String first_name = jsonObject.getString("first_name");
                            String last_name = jsonObject.getString("last_name");
                            String address = jsonObject.getString("address");
                            String address_2 = jsonObject.getString("address_2");
                            String city = jsonObject.getString("city");
                            String mobile_no = jsonObject.getString("mobile_no");
                            String postcode = jsonObject.getString("postcode");
                            String state = jsonObject.getString("state");
                            String country = jsonObject.getString("country");
                            String email = jsonObject.getString("email");
                            txtUserName.setText(first_name + " " + last_name);

                            sessionManager.updateUserDetail(first_name,
                                    last_name,
                                    address,
                                    address_2,
                                    city,
                                    mobile_no,
                                    postcode,
                                    state,
                                    country,
                                    email);
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", anError.toString() + "<<");
                    }
                });
    }

    @Override
    protected void onResume() {
        if (Constants.Companion.getGOTOCART()) {
            Constants.Companion.setGOTOCART(false);
            Log.e("cart", "go to cart");
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.nav_my_cart) {
                navController.popBackStack();
                navController.navigate(R.id.nav_my_cart);
            } else {
                navController.navigate(R.id.nav_my_cart);
            }
        }
        super.onResume();

    }

}