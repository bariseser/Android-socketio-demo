package com.bariseser.socketdemo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bariseser.socketdemo.Adapter.ViewHolder.LogMessageViewHolder;
import com.bariseser.socketdemo.Adapter.ViewHolder.MyMessageViewHolder;
import com.bariseser.socketdemo.Adapter.ViewHolder.TheirMessageViewHolder;
import com.bariseser.socketdemo.Model.MessageModel;
import com.bariseser.socketdemo.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageAdapter";
    private List<MessageModel> messages;

    public MessageAdapter(Context context, List<MessageModel> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, String.valueOf(viewType));

        if (viewType == MessageModel.MY_MESSAGE) {
            return new MyMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, parent, false));
        } else if (viewType == MessageModel.THEIR_MESSAGE) {
            return new TheirMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_their_message, parent, false));
        } else {
            return new LogMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_log, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == MessageModel.MY_MESSAGE) {
            ((MyMessageViewHolder) holder).bind(messages.get(position));
        } else if (getItemViewType(position) == MessageModel.THEIR_MESSAGE) {
            ((TheirMessageViewHolder) holder).bind(messages.get(position));
        } else {
            ((LogMessageViewHolder) holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }
}
