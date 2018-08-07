package com.assignment.listview_images.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.listview_images.MyApplication;
import com.assignment.listview_images.R;
import com.assignment.listview_images.models.MainModel;
import com.assignment.listview_images.models.RowModel;
import com.assignment.listview_images.presenter.APICallBacks;
import com.assignment.listview_images.presenter.ImageAdapter;
import com.assignment.listview_images.utils.AppUtil;
import com.assignment.listview_images.utils.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageAdapter imageAdapter = null;
    RecyclerView recyclerView = null;
    Toolbar toolbar = null;
    AppCompatTextView tv_Heading = null;
    TextView tv_Refresh = null;
    /**
     * Read,write external storage and GPS permission
     */
    public static String PERMISSIONS[] = new String[]{Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ui();

    }

    private void ui() {
        initViews();
        setAdapterToView();
        loadDataFromServer();
        viewOnClickListener();
    }

    private void viewOnClickListener() {
        tv_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataFromServer();
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_main);
        toolbar = findViewById(R.id.toolbar_main);
        tv_Heading = findViewById(R.id.tv_tool_bar_title);
        tv_Refresh = findViewById(R.id.tv_refresh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



    private void setAdapterToView(){

        imageAdapter = new ImageAdapter(new ArrayList<RowModel>(0), new ImageAdapter.PostItemListener() {

            @Override
            public void onPostClick(String str) {
                if (null != str)
                    tv_Heading.setText(str);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * Requesting for runtime permission if SDK >= Marshmallow
     */
    private void getPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AppUtil.hasSelfPermission(this, PERMISSIONS)) {
                loadDataFromServer();
            } else {
                requestPermissions(PERMISSIONS, Constants.PermissionConatants.REQUEST_CODE);
            }
        }
        else{
            loadDataFromServer();
        }
    }

    private void loadDataFromServer() {

        if(AppUtil.isInternetConnected(this)) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setMessage(getResources().getString(R.string.loading_wait));
            dialog.show();

            APICallBacks.getInstance().apiCallToGetData(new APICallBacks.GetResult() {
                @Override
                public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                    dialog.dismiss();

                    if (response.isSuccessful()) {
                        if(null != response.body()) {
                            if (null != response.body().getTitle())
                                tv_Heading.setText(response.body().getTitle());
                            if (response.body().getRows().size() > 0)
                                imageAdapter.updateAnswers(response.body().getRows());
                        }
                        else{
                            showErrorMessage();
                        }

                        Log.d("MainActivity", "posts loaded from API");
                    } else {
                        int statusCode = response.code();
                        showErrorMessage();
                        // handle request errors depending on status code
                    }
                }

                @Override
                public void onFailure(Call<MainModel> call, Throwable t) {
                    showErrorMessage();
                    dialog.dismiss();
                    Log.d("MainActivity", "error loading from API");
                }
            });
        }
        else{
            Toast.makeText(MyApplication.getInstance(),getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorMessage(){
        findViewById(R.id.ll_main).setVisibility(View.GONE);
        findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
    }
}
