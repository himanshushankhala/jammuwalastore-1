package com.jammuwalastore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jammuwalastore.MainActivity;
import com.jammuwalastore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InquiryFragment extends Fragment {

    public InquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inquiry, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.txtTitle.setText(getString(R.string.menu_contact_us));
        }
        init(view);
    }

    private void init(View view) {

    }

}
