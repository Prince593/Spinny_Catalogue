package com.spinnycatalogue.data.model.getCarMakes;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeResults {
    @Expose
    @SerializedName("Make_Name")
    private String makeName;
    @Expose
    @SerializedName("Make_ID")
    private int makeId;

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
        return makeName;
    }
}
