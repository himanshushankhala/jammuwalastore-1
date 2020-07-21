package com.jammuwalastore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jammuwalastore.DeliveryOptionsActivity;
import com.jammuwalastore.MainActivity;
import com.jammuwalastore.R;
import com.jammuwalastore.adapter.CartListAdapter;
import com.jammuwalastore.dataBase.DBHandler;
import com.jammuwalastore.helper.SessionManager;
import com.jammuwalastore.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    RecyclerView rvCartItems;
    private DBHandler dbHandler;
    List<ProductModel> productDataList = new ArrayList<>();
    CartListAdapter cartListAdapter;
    private LinearLayout linNoItems;
    private LinearLayout relPlaceOrder;
    private LinearLayout relTopLayout;
    private TextView txtMRP;
    private TextView txtGstCharges;
    private TextView txtPlaceOrder;
    private TextView txtTotalPrice;
    SessionManager sessionManager;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.txtTitle.setText(getString(R.string.menu_my_cart));
        }
        init(view);
    }

    private void init(View view) {
        dbHandler = new DBHandler(view.getContext());
        sessionManager = new SessionManager(view.getContext());
        rvCartItems = view.findViewById(R.id.rvCartItems);
        linNoItems = view.findViewById(R.id.linNoItems);
        relPlaceOrder = view.findViewById(R.id.relPlaceOrder);
        relTopLayout = view.findViewById(R.id.relTopLayout);
        txtMRP = view.findViewById(R.id.txtMRP);
        txtGstCharges = view.findViewById(R.id.txtGstCharges);
        txtPlaceOrder = view.findViewById(R.id.txtPlaceOrder);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
        if (cartListAdapter == null) {
            cartListAdapter = new CartListAdapter(getActivity(), productDataList, new CartListAdapter.onItemAdd() {
                @Override
                public void onItemClick(boolean isPlus, int position) {
                    addRemoveItemFromCart(isPlus, position);
                }
            });
            rvCartItems.setAdapter(cartListAdapter);
        }
        setupCartList();
        setCartValue();
        setPlaceOrderButton();
    }

    private void setPlaceOrderButton() {
        if (sessionManager.isLoggedIn()) {
            txtPlaceOrder.setText(getString(R.string.place_order));
            relPlaceOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), DeliveryOptionsActivity.class));
                }
            });
        } else {
            txtPlaceOrder.setText(getString(R.string.login_to_place_order));
            relPlaceOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManager.logoutUser(getActivity());

                }
            });
        }
    }

    private void setupCartList() {
        if (dbHandler == null) return;

        productDataList.clear();
        if (dbHandler.getAllProducts() != null) {
            productDataList.addAll(dbHandler.getAllProducts());
        }
        if (productDataList != null && productDataList.size() > 0) {
            cartListAdapter.notifyDataSetChanged();
        } else {
            //show empty view
            rvCartItems.setVisibility(View.GONE);
            linNoItems.setVisibility(View.VISIBLE);
            relPlaceOrder.setVisibility(View.GONE);
            relTopLayout.setVisibility(View.GONE);
        }
    }

    private void addRemoveItemFromCart(boolean isPlus, int position) {
        if (isPlus) {
            int quantity = 0;
            if (productDataList.get(position).getProductQuantity() > 0) {
                productDataList.get(position).setProductQuantity(productDataList.get(position).getProductQuantity() + 1);
                quantity = productDataList.get(position).getProductQuantity();
            } else {
                productDataList.get(position).setProductQuantity(1);
                quantity = productDataList.get(position).getProductQuantity();
            }

            try {
                long price = 0L;
                price = Long.parseLong(productDataList.get(position).getProductPrice());
                price *= quantity;
                productDataList.get(position).setTotalPrice(Long.toString(price));
            } catch (Exception ignored) {
            }
            if (quantity == 1) {
                dbHandler.addProduct(productDataList.get(position));
            } else {
                dbHandler.updateProducts(productDataList.get(position));
            }
        } else {
            int quantity = 0;
            productDataList.get(position).setProductQuantity(productDataList.get(position).getProductQuantity() - 1);
            quantity = productDataList.get(position).getProductQuantity();

            if (quantity < 1) {
                dbHandler.deleteProduct(productDataList.get(position).getProductid() + "",
                        productDataList.get(position).getProduct_weight());
                productDataList.remove(position);
            } else {

                try {
                    long price = 0L;
                    price = Long.parseLong(productDataList.get(position).getProductPrice());
                    price *= quantity;
                    productDataList.get(position).setTotalPrice(Long.toString(price));
                } catch (Exception ignored) {
                }

                dbHandler.updateProducts(productDataList.get(position));
            }


        }

        cartListAdapter.notifyDataSetChanged();
        // setupCartList();
        if (productDataList.size() < 1) {
            linNoItems.setVisibility(View.VISIBLE);
            relPlaceOrder.setVisibility(View.GONE);
            relTopLayout.setVisibility(View.GONE);
        }
        setCartValue();
    }


    private void setCartValue() {
        if (dbHandler == null) return;
        productDataList.clear();
        if (dbHandler.getAllProducts() != null) {
            productDataList.addAll(dbHandler.getAllProducts());
        }
        MainActivity parentActivity = (MainActivity) getActivity();
        assert parentActivity != null;
        if (productDataList != null && productDataList.size() > 0) {
            parentActivity.txtCartItemsCount.setVisibility(View.VISIBLE);
            parentActivity.txtCartItemsCount.setText(productDataList.size() + "");
        } else {
            parentActivity.txtCartItemsCount.setVisibility(View.GONE);
        }
        setMRPAndTotalPrice();
    }

    private void setMRPAndTotalPrice() {
        long totalPrice = 0;

        long totalGST = 0;
        if (productDataList != null && productDataList.size() > 0) {
            for (ProductModel productModel :
                    productDataList) {
                try {
                    long price = 0L;
                    price = Long.parseLong(productModel.getTotalPrice());
                    totalPrice = totalPrice + price;

                    if (productModel.getTax_status().length() > 0
                            && productModel.getTax_status().equalsIgnoreCase("taxable")) {
                        long priceST = 0L;
                        priceST = (Long.parseLong(productModel.getTax_class()) * Long.parseLong(productModel.getTotalPrice())) / 100;
                        totalGST = totalGST + priceST;
                    }

                } catch (Exception ignored) {
                }
            }
        }
        txtMRP.setText("₹" + totalPrice + "");
        txtGstCharges.setText("₹" + totalGST + "");
        txtTotalPrice.setText("₹" + (totalPrice + totalGST) + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            setupCartList();
            setCartValue();
        } catch (Exception e) {
        }
    }
}
