package com.bariseser.socketdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.bariseser.socketdemo.Adapter.ChannelAdapter;
import com.bariseser.socketdemo.Model.ChannelModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RelativeLayout progress_container;
    private ChannelAdapter adapter;
    private List<ChannelModel> channels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.channels);
        progress_container = findViewById(R.id.progress_container);
        progress_container.setVisibility(View.VISIBLE);

        setupChannelList();
    }

    private void setupChannelList() {

        adapter = new ChannelAdapter(this, channels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        channels.add(new ChannelModel("General", "General Channel", "http://icons.iconarchive.com/icons/custom-icon-design/mono-general-2/512/document-icon.png", "general"));
        channels.add(new ChannelModel("Football", "Football Channel", "https://cdn1.iconfinder.com/data/icons/education-259/100/education-19-512.png","football"));
        channels.add(new ChannelModel("Basketball", "Basketball Channel", "https://cdn4.iconfinder.com/data/icons/sports-balls/1024/BasketBall.png", "basketball"));

        adapter.notifyDataSetChanged();
        progress_container.setVisibility(View.GONE);
    }

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
        }
        return true;
    }

    private void gotoProfile() {
        Intent home = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(home);
    }
}