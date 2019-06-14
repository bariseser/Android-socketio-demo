package com.bariseser.socketdemo;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nickname;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nickname = findViewById(R.id.etNickname);
        loginButton = findViewById(R.id.btnJoinChat);
        loginButton.setOnClickListener(this);

        nickname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEND){
                    joinChat();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnJoinChat:
                joinChat();
        }
    }

    private void joinChat() {
        if (nickname.getText().toString().isEmpty()) {
            showFormValidationError();
        } else {
            ConfigManager.getInstance().setBoolean("isLogin", true);
            ConfigManager.getInstance().setString("nickname", nickname.getText().toString());
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
                        nickname.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
        builder.show();
    }
}
