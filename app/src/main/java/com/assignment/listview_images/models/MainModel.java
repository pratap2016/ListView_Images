package com.assignment.listview_images.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Model class for parsing the content of title and rows which implements serializable
 */

public class MainModel implements Serializable{
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