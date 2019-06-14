package com.bariseser.socketdemo.SocketManager.Listener;

import com.bariseser.socketdemo.SocketManager.Enum.CallbackState;
import com.bariseser.socketdemo.SocketManager.Enum.CallbackType;

public interface MainCallback<T> {
    void onDone(CallbackType type, CallbackState state, T t, Exception e);
}
