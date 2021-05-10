package com.demo.bluetoothscanner;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import com.demo.bluetoothscanner.MainActivity;

public class Global {
    public static AlertDialog.Builder buildAlertDialog(Context context,String title,String message,String positiveText, DialogInterface.OnClickListener positiveListener,String neutralText,DialogInterface.OnClickListener neutralListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(positiveText,positiveListener);
        alertDialog.setNeutralButton(neutralText, neutralListener);
        alertDialog.setCancelable(false);
        return alertDialog;
    }
}
