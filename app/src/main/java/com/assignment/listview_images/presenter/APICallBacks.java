package com.assignment.listview_images.presenter;

import com.assignment.listview_images.MyApplication;
import com.assignment.listview_images.models.MainModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Wrapper class to hold all API calls
 */

public class APICallBacks {

    private static GetResult getResult;
    private static APICallBacks mInstance;

    private APICallBacks (){

    }

    // Creating instance of Wrapper class
    public static synchronized APICallBacks getInstance(){
        if(null == mInstance) {
            mInstance = new APICallBacks();
        }
        return mInstance;
    }

    // Interface to give call back and data to implementing class
    public interface GetResult{

        void onResponse(Call<MainModel> call, Response<MainModel> response);
        void onFailure(Call<MainModel> call, Throwable t);
    }

    /**
     * Retrofit API call
     * @param result
     */
    public void apiCallToGetData(GetResult result){
        getResult  = result;
        MyApplication.getRetrofitService().getJsonFromUrl().enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                // Passing data on success
                getResult.onResponse(call, response);

            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                // Passing data on failure
                getResult.onFailure(call, t);

            }
        });
    }
}
