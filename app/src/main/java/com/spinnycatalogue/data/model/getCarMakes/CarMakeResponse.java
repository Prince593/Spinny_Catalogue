package com.spinnycatalogue.data.model.getCarMakes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CarMakeResponse {

    @Expose
    @SerializedName("Results")
    private List<MakeResults> results = new ArrayList<>();
    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Count")
    private int count;

    public List<MakeResults> getResults() {
        return results;
    }

    public void setResults(List<MakeResults> results) {
        this.results = results;
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
