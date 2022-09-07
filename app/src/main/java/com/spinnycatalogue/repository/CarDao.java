package com.spinnycatalogue.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.spinnycatalogue.data.local.CarData;

import java.util.List;

@Dao
public interface CarDao {
    @Query("SELECT * FROM car_table")
    LiveData<List<CarData>> getAllCars();

    @Insert
    void addCar(CarData carData);

    @Update
    void updateCar(CarData carData);

    @Delete
    void deleteCar(CarData carData);
}