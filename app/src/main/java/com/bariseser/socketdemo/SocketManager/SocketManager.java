package com.bariseser.socketdemo.SocketManager;

import android.content.Context;

import com.bariseser.config_manager.ConfigManager;
import com.bariseser.socketdemo.SocketManager.Enum.CallbackState;
import com.bariseser.socketdemo.SocketManager.Enum.CallbackType;
import com.bariseser.socketdemo.SocketManager.Listener.MainCallback;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {

    private static final String URL = "http://192.168.2.83:3000/";

    private Socket socket;

    public static SocketManager instance;

    public SocketManager() {}

    public static SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public void initialize(Context context, String room, MainCallback callback) {
        try {
            String url = URL + room;
            IO.Options options = new IO.Options();
            options.query = "nickname=" + ConfigManager.getInstance().getString("nickname", "undefined");
            options.reconnection = true;
            options.reconnectionDelay = 1000;
            options.timeout = 10000;
            socket = IO.socket(url, options);
            callback.onDone(CallbackType.SOCKET_LOGIN, CallbackState.DONE, null, null);
        } catch (URISyntaxException e) {
            callback.onDone(CallbackType.SOCKET_LOGIN, CallbackState.ERROR, null, e);
        }
    }

    public Socket getSocket() {
        return socket;
    }
}