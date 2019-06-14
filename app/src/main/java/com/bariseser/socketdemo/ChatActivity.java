package com.bariseser.socketdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bariseser.config_manager.ConfigManager;
import com.bariseser.socketdemo.Adapter.MessageAdapter;
import com.bariseser.socketdemo.Model.ChannelModel;
import com.bariseser.socketdemo.Model.MessageModel;
import com.bariseser.socketdemo.SocketManager.Enum.CallbackState;
import com.bariseser.socketdemo.SocketManager.Enum.CallbackType;
import com.bariseser.socketdemo.SocketManager.Listener.MainCallback;
import com.bariseser.socketdemo.SocketManager.SocketManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    public static final String TAG = "DDD";
    private ActionBar actionBar;
    private ChannelModel channelData;
    private RelativeLayout progress;
    private RelativeLayout inputContainer;
    private ImageView sendButton;
    private EditText message;
    private List<MessageModel> messageModel = new ArrayList<>();
    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private TextView activeUserCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        channelData = (ChannelModel) intent.getSerializableExtra("object");

        actionBar = getSupportActionBar();
        actionBar.setTitle(channelData.getChannelName());
        actionBar.setSubtitle(channelData.getChannelDescription());
        actionBar.setDisplayHomeAsUpEnabled(true);

        adapter = new MessageAdapter(this, messageModel);
        progress = findViewById(R.id.progress_container);
        activeUserCount = findViewById(R.id.tvActiveUser);
        progress.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        inputContainer = findViewById(R.id.input_container);
        sendButton = findViewById(R.id.send_button);
        message = findViewById(R.id.message);
        sendButton.setOnClickListener(this);
        message.setOnEditorActionListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            SocketManager.getInstance().initialize(this, channelData.getChannelNamespace(), new MainCallback() {
                @Override
                public void onDone(CallbackType type, CallbackState state, Object o, Exception e) {
                    if (e == null) {
                        SocketManager.getInstance().getSocket().on(Socket.EVENT_CONNECT, onConnect);
                        SocketManager.getInstance().getSocket().on("new_message", newMessage);
                        SocketManager.getInstance().getSocket().on("join", join);
                        SocketManager.getInstance().getSocket().on("left", left);
                        SocketManager.getInstance().getSocket().on("activeUser", activeUser);
                        SocketManager.getInstance().getSocket().on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                        SocketManager.getInstance().getSocket().on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                        SocketManager.getInstance().getSocket().connect();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.connectionError, Toast.LENGTH_LONG).show();
                        gotoHome();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.connectionError, Toast.LENGTH_LONG).show();
            gotoHome();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            SocketManager.getInstance().getSocket().off(Socket.EVENT_CONNECT, onConnect);
            SocketManager.getInstance().getSocket().off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            SocketManager.getInstance().getSocket().on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            SocketManager.getInstance().getSocket().off("new_message", newMessage);
            SocketManager.getInstance().getSocket().off("join", join);
            SocketManager.getInstance().getSocket().off("left", left);
            SocketManager.getInstance().getSocket().disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                emitMessage();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.send || id == EditorInfo.IME_NULL) {
            emitMessage();
            return true;
        }
        return false;
    }

    private void gotoHome() {
        Intent home = new Intent(ChatActivity.this, MainActivity.class);
        startActivity(home);
        finish();
    }

    private void emitMessage() {

        if (!SocketManager.getInstance().getSocket().connected()) {
            return;
        }

        String messageValue = message.getText().toString().trim();

        if (TextUtils.isEmpty(messageValue)) {
            message.requestFocus();
            return;
        }

        message.setText("");
        addMessageToList(ConfigManager.getInstance().getString("nickname", "undefined"), messageValue, MessageModel.MY_MESSAGE);
        try {
            JSONObject params = new JSONObject();
            params.put("nickname", ConfigManager.getInstance().getString("nickname", "undefined"));
            params.put("message", messageValue);
            SocketManager.getInstance().getSocket().emit("new_message", params);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void addMessageToList(String nickname, String message, int type) {
        messageModel.add(new MessageModel(nickname, message, type));
        adapter.notifyItemInserted(messageModel.size() - 1);
        scrollToBottom();
    }

    private void addLogtoList(String nickname, String message) {
        messageModel.add(new MessageModel(nickname, message, MessageModel.LOG));
        adapter.notifyItemInserted(messageModel.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.GONE);
                    inputContainer.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                    JSONObject params = new JSONObject();
                    SocketManager.getInstance().getSocket().emit("join", params);
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), R.string.connectionError, Toast.LENGTH_LONG).show();
                    gotoHome();
                }
            });
        }
    };

    private Emitter.Listener newMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String nickname;
                    String message;
                    try {
                        nickname = data.getString("nickname");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    addMessageToList(nickname, message, MessageModel.THEIR_MESSAGE);
                }
            });
        }
    };

    private Emitter.Listener join = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String nickname;
                    try {
                        nickname = data.getString("nickname");
                        activeUserCount.setText(getResources().getString(R.string.activeUser) + " " + data.getInt("count"));
                        addLogtoList(nickname, "has joined");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }
    };

    private Emitter.Listener activeUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        activeUserCount.setText(getResources().getString(R.string.activeUser) + " " + data.getInt("count"));
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }
    };

    private Emitter.Listener left = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String nickname;
                    try {
                        nickname = data.getString("nickname");
                        activeUserCount.setText(getResources().getString(R.string.activeUser) + " " + data.getInt("count"));
                        addLogtoList(nickname, "has left");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                gotoProfile();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void gotoProfile() {
        Intent home = new Intent(ChatActivity.this, ProfileActivity.class);
        startActivity(home);
    }

}
