package com.demo.bluetoothscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BackStack", "重啟MainActivity");
        setContentView(R.layout.activity_main);
        alertDialog = Global.buildAlertDialog(this, "Exit", "確定要離開APP嗎?", "確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        findViewById(R.id.btn_start).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ScannerActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}