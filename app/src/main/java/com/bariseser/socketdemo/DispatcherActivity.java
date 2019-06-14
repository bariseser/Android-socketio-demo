package com.bariseser.socketdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.bariseser.config_manager.ConfigManager;

public class DispatcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ConfigManager.getInstance().getBoolean("isLogin", false)) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
            finish();
        } else {
            Intent home = new Intent(this, LoginActivity.class);
            startActivity(home);
            finish();
        }
    }
}
