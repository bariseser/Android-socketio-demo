package com.bariseser.socketdemo.Adapter.ViewHolder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bariseser.socketdemo.Model.MessageModel;
import com.bariseser.socketdemo.R;

public class LogMessageViewHolder extends RecyclerView.ViewHolder {
    private TextView message;

    public LogMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }

    public void bind(final MessageModel row) {

        try {
            message.setText(row.getNickname() + " " + row.getMessage());

        } catch (Exception e) {
            Log.d("LogMessageViewHolder", e.getMessage());
        }
    }
}
