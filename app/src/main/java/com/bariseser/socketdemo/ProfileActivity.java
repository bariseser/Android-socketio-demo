package com.bariseser.socketdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bariseser.config_manager.ConfigManager;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ActionBar actionBar;
    private EditText etNickname;
    private Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.myProfile);

        etNickname = findViewById(R.id.etNickname);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        etNickname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEND){
                    saveData();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        etNickname.setText(ConfigManager.getInstance().getString("nickname", "undefined"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                saveData();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveData() {
        if (etNickname.getText().toString().isEmpty()) {
            showFormValidationError();
        } else {
            ConfigManager.getInstance().setString("nickname", etNickname.getText().toString());
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
            finish();
        }
    }

    private void showFormValidationError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.nicknameErrorMessage))
                .setTitle(getResources().getString(R.string.formValidationError))
                .setPositiveButton(getResources().getString(R.string.positiveButtonText), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etNickname.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
        builder.show();
    }
}
