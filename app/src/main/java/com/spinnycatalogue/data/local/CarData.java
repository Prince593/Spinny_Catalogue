package com.spinnycatalogue.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "car_table")
public class CarData {
    @Expose
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @Expose
    @ColumnInfo(name = "Model_Name")
    private String modelName;
    @Expose
    @ColumnInfo(name = "Model_ID")
    private int modelId;
    @Expose
    @ColumnInfo(name = "Make_Name")
    private String makeName;
    @Expose
    @ColumnInfo(name = "Make_ID")
    private int makeId;
    @Expose
    @ColumnInfo(name = "Car_Image")
    private String carImage;

    public CarData(int uid, String modelName, int modelId, String makeName, int makeId, String carImage) {
        this.uid = uid;
        this.modelName = modelName;
        this.modelId = modelId;
        this.makeName = makeName;
        this.makeId = makeId;
        this.carImage = carImage;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }
}
