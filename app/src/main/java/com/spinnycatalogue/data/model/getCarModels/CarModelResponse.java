package com.spinnycatalogue.data.model.getCarModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarModelResponse {

    @Expose
    @SerializedName("Results")
    private List<ModelResults> results;
    @Expose
    @SerializedName("SearchCriteria")
    private String searchcriteria;
    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Count")
    private int count;

    public List<ModelResults> getResults() {
        return results;
    }

    public void setResults(List<ModelResults> results) {
        this.results = results;
    }

    public String getSearchcriteria() {
        return searchcriteria;
    }

    public void setSearchcriteria(String searchcriteria) {
        this.searchcriteria = searchcriteria;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
