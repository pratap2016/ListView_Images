package com.assignment.listview_images.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vijay on 06-08-2018.
 */

public class MainModel {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rows")
    @Expose
    private List<RowModel> rows = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RowModel> getRows() {
        return rows;
    }

    public void setRows(List<RowModel> rows) {
        this.rows = rows;
    }

}