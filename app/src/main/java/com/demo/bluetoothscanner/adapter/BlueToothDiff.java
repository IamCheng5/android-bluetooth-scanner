package com.demo.bluetoothscanner.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.demo.bluetoothscanner.model.BlueToothInfo;

import java.util.Arrays;
import java.util.List;

public class BlueToothDiff extends DiffUtil.Callback {
    List<BlueToothInfo> oldData, newData;

    public BlueToothDiff(List<BlueToothInfo> oldData, List<BlueToothInfo> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData == null ? 0 : oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData == null ? 0 : newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition==newItemPosition;
        //return oldData.get(oldItemPosition).getAddress().equals(newData.get(newItemPosition).getAddress());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

}
