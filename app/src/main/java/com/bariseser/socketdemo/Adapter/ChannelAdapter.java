package com.bariseser.socketdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bariseser.socketdemo.ChatActivity;
import com.bariseser.socketdemo.Model.ChannelModel;
import com.bariseser.socketdemo.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.MyViewHolder> {

    private List<ChannelModel> channels;
    private Context context;

    public ChannelAdapter(Context context, List<ChannelModel> channels) {
        this.channels = channels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ((MyViewHolder) holder).bind(channels.get(position));
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SimpleDraweeView avatar;
        private TextView tvChannelName, tvDescription;
        private RelativeLayout container;
        private ChannelModel channel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.ivIcon);
            tvChannelName = itemView.findViewById(R.id.tvChannelName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(this);
        }

        public void bind(ChannelModel data) {
            try {
                this.channel = data;
                tvChannelName.setText(data.getChannelName());
                tvDescription.setText(data.getChannelDescription());
                Uri uri = Uri.parse(data.getChannelAvatar());
                avatar.setImageURI(uri);
            } catch (Exception e) {
                Log.e("VIEWHOLDER", e.getMessage());
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container:
                    gotoChat();
                    break;
            }
        }

        private void gotoChat() {
            Intent gotoChat = new Intent(itemView.getContext(), ChatActivity.class);
            gotoChat.putExtra("name", channel.getChannelName());
            gotoChat.putExtra("channel", channel.getChannelNamespace());
            gotoChat.putExtra("object", channel);
            itemView.getContext().startActivity(gotoChat);
        }
    }
}