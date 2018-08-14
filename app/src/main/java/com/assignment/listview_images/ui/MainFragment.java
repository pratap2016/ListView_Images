package com.assignment.listview_images.ui;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.assignment.listview_images.MyApplication;
import com.assignment.listview_images.R;
import com.assignment.listview_images.models.MainModel;
import com.assignment.listview_images.models.RowModel;
import com.assignment.listview_images.presenter.APICallBacks;
import com.assignment.listview_images.utils.AppUtil;
import com.assignment.listview_images.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Class to perform API call and updating views
 */

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    /**
     * Phone state permission
     */
    public static String PERMISSIONS[] = new String[]{Manifest.permission.READ_PHONE_STATE};
    View mView;
    ImageAdapter imageAdapter = null;

    // Butter Knife dependency injection
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_main)
    RecyclerView recyclerView ;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.ll_error)
    LinearLayout llError;
    @BindView(R.id.tv_tool_bar_title)
    AppCompatTextView tv_Heading;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(null == mView)
            mView = inflater.inflate(R.layout.fragment_main,container, false);

        ui();

        return mView;
    }

    /**
     * All Ui related operation of Fragment
     */
    private void ui() {
        initViews();
        setAdapterToView();
        getPermissions();
    }

    /**
     * Initializing all views and operations
     */
    private void initViews() {

        // Setting Toolbar to action bar
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                loadDataFromServer();
            }
        });
    }

    /**
     * Setting values to Adapter
     */
    private void setAdapterToView(){

        // Creating new instance of Image adapter and setting values to it
        imageAdapter = new ImageAdapter(new ArrayList<RowModel>(0), new ImageAdapter.PostItemListener() {

            @Override
            public void onPostClick(String str) {
                if (null != str)
                    tv_Heading.setText(str);
            }
        });

        // Recycler view Layout manager and Decorator
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * Requesting for runtime permission if SDK >= Marshmallow
     */
    private void getPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AppUtil.hasSelfPermission(getActivity(), PERMISSIONS)) {
                loadDataFromServer();
            } else {
                requestPermissions(PERMISSIONS, Constants.PermissionConatants.REQUEST_CODE);
            }
        }
        else{
            loadDataFromServer();
        }
    }

    /**
     * Method to check internet and Make api call to get feeds
     */
    private void loadDataFromServer() {

        if(AppUtil.isInternetConnected(getActivity())) { // Checking internet connection

            // Creating progress dialog to show for async call
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setMessage(getResources().getString(R.string.loading_wait));
            if(!mSwipeRefreshLayout.isRefreshing())
                dialog.show();

            // Wrapper class callback to get result
            APICallBacks.getInstance().apiCallToGetData(new APICallBacks.GetResult() {
                @Override
                public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                    if(!mSwipeRefreshLayout.isRefreshing())
                        dialog.dismiss();
                    else
                        // Stopping swipe refresh
                        mSwipeRefreshLayout.setRefreshing(false);

                    // Success response
                    if (response.isSuccessful()) {

                        // If Response is not empty or null
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
                    if(!mSwipeRefreshLayout.isRefreshing())
                        dialog.dismiss();
                    else
                    // Stopping swipe refresh
                    mSwipeRefreshLayout.setRefreshing(false);

                    Log.d("MainActivity", "error loading from API");
                }
            });
        }
        else{
            Toast.makeText(MyApplication.getInstance(),getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show error screen if API call fails
     */
    private void showErrorMessage(){
        llMain.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
    }


    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

    /**
     * Reloading the recycler view after api call.
     */
    private void loadRecyclerViewData() {

        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);
        getPermissions();
    }
}
