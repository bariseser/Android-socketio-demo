package com.bariseser.socketdemo.Adapter.ViewHolder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bariseser.socketdemo.Model.MessageModel;
import com.bariseser.socketdemo.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class TheirMessageViewHolder extends RecyclerView.ViewHolder {
    private TextView username, message;
    private SimpleDraweeView avatar;

    public TheirMessageViewHolder(@NonNull View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.username);
        message = itemView.findViewById(R.id.message);
    }

    public void bind(final MessageModel row) {

        try {
            username.setText(row.getNickname());
            message.setText(row.getMessage());
        } catch (Exception e) {
            Log.d("BARISESER", e.getMessage());
        }
    }
}
