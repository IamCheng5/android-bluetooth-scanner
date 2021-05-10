package com.demo.bluetoothscanner.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.bluetoothscanner.R;
import com.demo.bluetoothscanner.fragments.ScannerFragment;
import com.demo.bluetoothscanner.model.BlueToothInfo;

import java.util.ArrayList;
import java.util.List;

public class BlueToothInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HANDLER_MESSAGE_UPDATE = 1;
    private List<BlueToothInfo> oldData;
    private List<BlueToothInfo> newData;
    ScannerFragment scannerFragment ;

    public BlueToothInfoAdapter(List<BlueToothInfo> data,ScannerFragment scannerFragment) {
        this.oldData = cloneData(data);
        this.newData = data;
        this.scannerFragment = scannerFragment;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case HANDLER_MESSAGE_UPDATE:
                    oldData.clear();
                    oldData.addAll(cloneData(newData));
                    ((DiffUtil.DiffResult) msg.obj).dispatchUpdatesTo(BlueToothInfoAdapter.this);
                    break;
            }
        }
    };

    public void updateData() {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BlueToothDiff(oldData, newData), true);
        Message message = mHandler.obtainMessage(HANDLER_MESSAGE_UPDATE);
        message.obj = diffResult;
        message.sendToTarget();
    }
    private List<BlueToothInfo> cloneData(List<BlueToothInfo> data){
        List <BlueToothInfo> cloneData = new ArrayList<>();
        for (BlueToothInfo blueToothInfo:data){
            cloneData.add(blueToothInfo.clone());
        }
        return cloneData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /*
        * 不啟用局部更新 onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads)
        * 因每次變更都必須重設TextView以及Button onClick
        * */
        BlueToothInfo blueToothInfo = oldData.get(position);
        ((MyViewHolder)holder).tvItemInfo.setText("MAC："+blueToothInfo.getAddress()+"\nRSSI："+blueToothInfo.getmRssi());
        ((MyViewHolder)holder).btnItemDetail.setOnClickListener(view -> {
            scannerFragment.runScanner(false);
            Bundle bundle = new Bundle();
            bundle.putSerializable("BlueToothInfo",blueToothInfo);
            Navigation.findNavController(view).navigate(R.id.action_scannerFragment_to_detailInfoFragment,bundle);
        });

    }

    @Override
    public int getItemCount() {
        return oldData==null?0:oldData.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvItemInfo;
        public Button btnItemDetail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemInfo = itemView.findViewById(R.id.tv_item_info);
            btnItemDetail = itemView.findViewById(R.id.btn_item_detail);

        }
    }
}
