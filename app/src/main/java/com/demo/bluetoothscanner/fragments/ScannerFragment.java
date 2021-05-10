package com.demo.bluetoothscanner.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.Toast;

import com.demo.bluetoothscanner.Global;
import com.demo.bluetoothscanner.R;
import com.demo.bluetoothscanner.adapter.BlueToothInfoAdapter;
import com.demo.bluetoothscanner.model.BlueToothInfo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScannerFragment extends Fragment {
    final static int PERMISSION_ACCESS_COARSE_LOCATION_CODE = 1001;
    /*View*/
    View fragment;
    RecyclerView recBluetooth;
    Button btnSwitch;
    /*Bluetooth Module*/
    BluetoothAdapter mBluetoothAdapter;
    ScanCallback scanCallback;
    BluetoothAdapter.LeScanCallback leScanCallback;

    BlueToothInfoAdapter blueToothInfoAdapter;
    List<BlueToothInfo> data = new ArrayList<BlueToothInfo>();

    public ScannerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("BackStack", "onCreate ScannerFragment");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AlertDialog.Builder alertDialog = Global.buildAlertDialog(getContext(), "Exit", "確定要離開APP嗎?", "確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
                System.exit(0);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });;
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                alertDialog.show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        //getActivity().actionba
    }

    @Override
    public void onStart() {
        super.onStart();
        getPermission();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BackStack", "onCreateView ScannerFragment");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (fragment == null) {
            fragment = inflater.inflate(R.layout.fragment_scanner, container, false);
            btnSwitch = fragment.findViewById(R.id.btn_switch);
            recBluetooth = fragment.findViewById(R.id.rec_blue_tooth);
            btnSwitch.setOnClickListener(view -> {
                if (((Button) view).getText().equals("開啟掃描")) {
                    runScanner(true);
                } else {
                    runScanner(false);
                }
            });
            recBluetooth.setHasFixedSize(true);
            recBluetooth.setLayoutManager(new MyLinearLayoutManager(getContext()));
            blueToothInfoAdapter = new BlueToothInfoAdapter(data,this);
            recBluetooth.setAdapter(blueToothInfoAdapter);
            recBluetooth.scrollToPosition(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                scanCallback = new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        BluetoothDevice device = result.getDevice();
                        ScanRecord mScanRecord = result.getScanRecord();
                        //同MAC同Rssi 不更新List也不update recyclerView
                        if (updateInfoData(new BlueToothInfo(device.getAddress(), mScanRecord.getBytes(), result.getRssi())))
                            new Thread(() -> blueToothInfoAdapter.updateData()).start();
                    }
                };
            } else {
                leScanCallback = (device, rssi, scanRecord) -> {
                    if (updateInfoData(new BlueToothInfo(device.getAddress(), scanRecord, rssi)))
                        new Thread(() -> blueToothInfoAdapter.updateData()).start();
                };
            }
        }
        return fragment;
    }



    private boolean updateInfoData(BlueToothInfo input) {
        for (BlueToothInfo b : data) {
            if (b.getAddress().equals(input.getAddress())) {
                if (input.getmRssi() == b.getmRssi())
                    return false;
                else {
                    b.setmRssi(input.getmRssi());
                    return true;
                }
            }
        }
        data.add(input);
        return true;
    }

    public void runScanner(boolean run) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner mBluetoothLeScanner;
        if (mBluetoothAdapter == null) {
            Toast.makeText(getContext(), "您的手機沒有藍芽裝置！", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getContext(), "請先開啟您的藍芽！", Toast.LENGTH_LONG).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            if (run) {
                mBluetoothLeScanner.startScan(scanCallback);
                btnSwitch.setText("停止掃描");
            } else {
                mBluetoothLeScanner.stopScan(scanCallback);
                btnSwitch.setText("開啟掃描");
            }
        } else {
            if (run) {
                mBluetoothAdapter.startLeScan(leScanCallback);
                btnSwitch.setText("停止掃描");
            } else {
                mBluetoothAdapter.stopLeScan(leScanCallback);
                btnSwitch.setText("開啟掃描");
            }
        }

    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION_CODE:
                if (grantResults.length > 0 && (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getContext(), "請允許權限！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        Log.d("BackStack", "銷毀ScannerFragment");
        super.onDestroy();
    }

    private class MyLinearLayoutManager extends LinearLayoutManager {

        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}