package com.spinnycatalogue.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.spinnycatalogue.data.local.CarData;

import java.util.List;

public class CarRepository {
    
    private CarDao carDao;
    private LiveData<List<CarData>> allCars;

    public CarRepository(Application application) {
        CarDatabase database = CarDatabase.getInstance(application);
        carDao = database.carDao();
        allCars = carDao.getAllCars();
    }

   
    public void addCar(CarData carData) {
       carDao.addCar(carData);
    }

    public void updateCar(CarData carData) {
        carDao.updateCar(carData);
    }

    public void deleteCar(CarData carData) {
       carDao.deleteCar(carData);
    }

    public LiveData<List<CarData>> getAllCars() {
        return allCars;
    }
}
