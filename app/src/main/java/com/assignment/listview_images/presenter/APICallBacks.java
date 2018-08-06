package com.assignment.listview_images.presenter;

import com.assignment.listview_images.MyApplication;
import com.assignment.listview_images.models.MainModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 06-08-2018.
 */

public class APICallBacks {

    private static GetResult getResult;
    private static APICallBacks mInstance;

    private APICallBacks (){

    }

    public static synchronized APICallBacks getInstance(){
        if(null == mInstance) {
            mInstance = new APICallBacks();
        }
        return mInstance;
    }

    public interface GetResult{

        void onResponse(Call<MainModel> call, Response<MainModel> response);
        void onFailure(Call<MainModel> call, Throwable t);
    }

    public void apiCallToGetData(GetResult result){
        getResult  = result;
        MyApplication.getRetrofitService().getJsonFromUrl().enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                getResult.onResponse(call, response);

            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                getResult.onFailure(call, t);

            }
        });
    }
}
