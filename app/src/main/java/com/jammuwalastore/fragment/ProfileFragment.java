package com.jammuwalastore.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.jammuwalastore.MainActivity;
import com.jammuwalastore.R;
import com.jammuwalastore.adapter.CountryAdapter;
import com.jammuwalastore.helper.Constants;
import com.jammuwalastore.helper.SessionManager;
import com.jammuwalastore.model.CitiesModel;
import com.jammuwalastore.model.City;
import com.jammuwalastore.model.State;
import com.jammuwalastore.model.StatesModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Credentials;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private EditText edt_first_name;
    private EditText edt_last_name;
    private EditText edt_address_one;
    private EditText edt_address_two;
    private EditText edt_post_code_et;
    private Spinner edtState;
    private TextView txtState;
    private Spinner edtCity;
    private TextView txtCity;
    private Button btn_update_profile;
    private StatesModel statesModel;
    private CitiesModel citiesModel;
    private String state = "";
    private String city = "";
    ArrayList<State> stateArrayList = new ArrayList<>();
    private SessionManager sessionManager;
    boolean isCustom = true;
    private LinearLayout progressLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.txtTitle.setText(getString(R.string.menu_my_profile));
        }
        init(view);
    }

    private void init(View view) {
        sessionManager = new SessionManager(view.getContext());
        progressLayout = view.findViewById(R.id.progressLayout);
        edt_first_name = view.findViewById(R.id.edt_first_name);
        edt_last_name = view.findViewById(R.id.edt_last_name);
        edt_address_one = view.findViewById(R.id.edt_address_one);
        edt_address_two = view.findViewById(R.id.edt_address_two);
        edt_post_code_et = view.findViewById(R.id.edt_post_code_et);

        edt_first_name.setText(sessionManager.getFirst_name());
        edt_last_name.setText(sessionManager.getLast_name());
        edt_address_one.setText(sessionManager.getAddress());
        edt_address_two.setText(sessionManager.getAddress_2());
        edt_post_code_et.setText(sessionManager.getPostcode());

        edtState = view.findViewById(R.id.edtState);
        txtState = view.findViewById(R.id.txtState);
        edtCity = view.findViewById(R.id.edtCity);
        txtCity = view.findViewById(R.id.txtCity);
        btn_update_profile = view.findViewById(R.id.btn_update_profile);

        edtState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!isCustom) {
                    if (position != 0) {
                        state = stateArrayList.get(position).getName();
                        txtState.setText(state);
                        setCityAdapter(stateArrayList.get(position).getId());
                    } else {
                        state = "";
                        txtState.setText(state);
                        setCityAdapter("0");
                        city = "";
                        txtCity.setText(city);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object user = adapterView.getAdapter().getItem(position);
                if (user instanceof City) {
                    city = ((City) user).getName();
                    txtCity.setText(city);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Gson gson = new Gson();
        statesModel = gson.fromJson(Constants.Companion.
                loadJSONFromAsset("cdata/states.json", getActivity()), StatesModel.class);

        citiesModel = gson.fromJson(Constants.Companion.
                loadJSONFromAsset("cdata/cities.json", getActivity()), CitiesModel.class);
        Log.e("statesModel", statesModel.getStates().size() + "<<");
        setStatesAdapter();

        state = sessionManager.getState();
        city = sessionManager.getCity();
        txtState.setText(state);
        txtCity.setText(city);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isCustom = false;
            }
        }, 3000);
        btn_update_profile.setOnClickListener(this);
    }


    private void setStatesAdapter() {
        Log.e("stateModelBfShow", Objects.requireNonNull(statesModel.getStates()).size() + "<<");
        for (State state : statesModel.getStates()) {
            if (Objects.requireNonNull(state.getCountry_id()).equalsIgnoreCase("101")) {
                stateArrayList.add(state);
            }
        }
        CountryAdapter<State> statesAdapter = new CountryAdapter<>(getActivity(), R.layout.spinner_layout, stateArrayList);
        edtState.setAdapter(statesAdapter);
    }

    private void setCityAdapter(String id) {
        ArrayList<City> cityList = new ArrayList<>();
        for (City city : Objects.requireNonNull(citiesModel.getCities())) {
            if (Objects.requireNonNull(city.getState_id()).equalsIgnoreCase(id)) {
                cityList.add(city);
            }
        }
        CountryAdapter<City> cityAdapter = new CountryAdapter<>(getActivity(), R.layout.spinner_layout, cityList);
        edtCity.setAdapter(cityAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_update_profile) {
            String firstName = edt_first_name.getText().toString().trim();
            String lastName = edt_last_name.getText().toString().trim();
            String addressOne = edt_address_one.getText().toString().trim();
            String addressTwo = edt_address_two.getText().toString().trim();
            String state = txtState.getText().toString().trim();
            String city = txtCity.getText().toString().trim();
            String postalCode = edt_post_code_et.getText().toString().trim();
            if (firstName.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.enter_first_name), Toast.LENGTH_SHORT).show();
                return;
            }
            if (lastName.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.enter_last_name), Toast.LENGTH_SHORT).show();
                return;
            }

            if (state.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.enter_state), Toast.LENGTH_SHORT).show();
                return;
            }

            if (city.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.enter_city), Toast.LENGTH_SHORT).show();
                return;
            }

            if (postalCode.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.enter_postal_code), Toast.LENGTH_SHORT).show();
                return;
            }
            progressLayout.setVisibility(View.VISIBLE);
            updateProfile(firstName, lastName, addressOne, addressTwo, state, city, postalCode);

        }
    }

    private void updateProfile(String firstName, String lastName, String addressOne,
                               String addressTwo, String state, String city, String postalCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fname", firstName);
            jsonObject.put("lname", lastName);
            jsonObject.put("address", addressOne + "");
            jsonObject.put("address_1", addressTwo + "");
            jsonObject.put("city", city + "");
            jsonObject.put("state", state + "");
            jsonObject.put("postcode", postalCode + "");
        } catch (Exception ignored) {
        }

        String url = Constants.Companion.getUser_Details_url();
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
                        sessionManager.updateUserDetail(firstName, lastName, addressOne, addressTwo, state, city, postalCode);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        if (mainActivity != null) {
                            mainActivity.txtUserName.setText(firstName + " " + lastName);
                        }
                        try {
                            Toast.makeText(getActivity(), (response.optString("message")) + "", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressLayout.setVisibility(View.GONE);
                        Log.e("error", anError.getErrorBody().toString() + "<<");
                        try {
                            JSONObject jsonObject1 = new JSONObject(anError.getErrorBody());
                            Toast.makeText(getActivity(), (jsonObject1.optString("message")) + "", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    }
                });
    }
}
