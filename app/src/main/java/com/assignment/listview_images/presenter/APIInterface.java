package com.assignment.listview_images.presenter;

import com.assignment.listview_images.models.MainModel;
import com.assignment.listview_images.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit API Interface
 */

public interface APIInterface {

    @GET(Constants.Url.DATA_URL)
    Call<MainModel> getJsonFromUrl();
}
