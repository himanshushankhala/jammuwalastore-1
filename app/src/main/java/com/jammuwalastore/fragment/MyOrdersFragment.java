package com.jammuwalastore.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.jammuwalastore.MainActivity;
import com.jammuwalastore.R;
import com.jammuwalastore.adapter.OrderListAdapter;
import com.jammuwalastore.helper.Constants;
import com.jammuwalastore.helper.SessionManager;
import com.jammuwalastore.model.OrderModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Credentials;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {
    private RecyclerView rvMyOrders;
    private SessionManager sessionManager;
    private LinearLayout linNoItems;
    private List<OrderModel> orderModelList = new ArrayList<>();
    OrderListAdapter orderListAdapter;
    LinearLayout progressLayout;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.txtTitle.setText(getString(R.string.menu_my_orders));
        }
        sessionManager = new SessionManager(view.getContext());
        rvMyOrders = view.findViewById(R.id.rvMyOrders);
        linNoItems = view.findViewById(R.id.linNoItems);
        progressLayout = view.findViewById(R.id.progressLayout);
        getOrdersDetail();
    }

    private void getOrdersDetail() {
        progressLayout.setVisibility(View.VISIBLE);
        String url = Constants.Companion.getOrderURL();
        Log.e("url>>", url + "<<");
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", Credentials.basic(sessionManager.getLoginToken(), sessionManager.getLoginTokenPassword()))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressLayout.setVisibility(View.GONE);
                        Log.e("getOrdersDetail>>", response + "<<");
                        try {
                            orderModelList = fromJsonList(response.toString(), OrderModel.class);
                            if (orderModelList.size() > 0) {
                                orderListAdapter=new OrderListAdapter(getActivity(),orderModelList);
                                rvMyOrders.setAdapter(orderListAdapter);
                            } else {
                                linNoItems.setVisibility(View.VISIBLE);
                            }
                            Log.e("orderModelList size >", orderModelList.size() + "<<");
                        } catch (Exception e) {
                            linNoItems.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressLayout.setVisibility(View.GONE);
                        linNoItems.setVisibility(View.VISIBLE);
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
