package com.demo.bluetoothscanner.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class BlueToothInfo implements Cloneable, Serializable {
    private String address ;
    private byte[] content ;
    private int mRssi;

    public BlueToothInfo(String address, byte[] content, int mRssi) {
        this.address = address;
        this.content = content;
        this.mRssi = mRssi;
    }
    @Override
    public BlueToothInfo clone() {
        BlueToothInfo info = null;
        try {
            info = (BlueToothInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return info;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getmRssi() {
        return mRssi;
    }

    public void setmRssi(int mRssi) {
        this.mRssi = mRssi;
    }

    @Override
    public String toString() {
        return "BlueToothInfo{" +
                "address='" + address + '\'' +
                ", content=" + Arrays.toString(content) +
                ", mRssi=" + mRssi +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlueToothInfo that = (BlueToothInfo) o;
        return mRssi == that.mRssi &&
                address.equals(that.address)&&
                Arrays.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + address.hashCode();
        result = 31 * result + mRssi;
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
