package com.spinnycatalogue.data.model.getCarModels;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelResults {
    @Expose
    @SerializedName("Model_Name")
    private String modelName;
    @Expose
    @SerializedName("Model_ID")
    private int modelId;
    @Expose
    @SerializedName("Make_Name")
    private String makeName;
    @Expose
    @SerializedName("Make_ID")
    private int makeId;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    @NonNull
    @Override
    public String toString() {
        return modelName;
    }
}
