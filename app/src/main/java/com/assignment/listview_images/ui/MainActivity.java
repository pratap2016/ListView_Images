package com.assignment.listview_images.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.assignment.listview_images.R;
import com.assignment.listview_images.models.RowModel;
import com.assignment.listview_images.presenter.ImageAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageAdapter imageAdapter = null;
    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ui();

    }

    private void ui() {
        initViews();
        setAdapterToView();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_main);
    }

    private void setAdapterToView(){

        imageAdapter = new ImageAdapter(this, new ArrayList<RowModel>(0), new ImageAdapter.PostItemListener() {

            @Override
            public void onPostClick(String str) {
                Toast.makeText(MainActivity.this, "Clicked Item" + str, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }
}
