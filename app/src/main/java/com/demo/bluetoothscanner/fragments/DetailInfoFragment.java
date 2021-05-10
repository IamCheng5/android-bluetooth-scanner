package com.demo.bluetoothscanner.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.bluetoothscanner.R;
import com.demo.bluetoothscanner.model.BlueToothInfo;

public class DetailInfoFragment extends Fragment {

    View fragment;
    public DetailInfoFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( "BackStack","onCreateView DetailFragment");
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (fragment == null) {
            fragment = inflater.inflate(R.layout.fragment_detail_info, container, false);
            BlueToothInfo b = (BlueToothInfo) getArguments().getSerializable("BlueToothInfo");
            ((TextView)fragment.findViewById(R.id.tv_detail_info)).setText("MAC："+b.getAddress()+"\nRSSI："+b.getmRssi()+"\n"+"ScanRecord："+b.getContent());

        }
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(fragment).popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }
}