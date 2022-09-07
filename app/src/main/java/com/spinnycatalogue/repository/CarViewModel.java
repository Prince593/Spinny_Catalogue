package com.spinnycatalogue.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.spinnycatalogue.data.local.CarData;

import java.util.List;

public class CarViewModel extends AndroidViewModel {

    private final CarRepository repository;

    private final LiveData<List<CarData>> allCars;

    public CarViewModel(@NonNull Application application) {
        super(application);
        repository = new CarRepository(application);
        allCars = repository.getAllCars();
    }

    public void addCar(CarData model) {
        repository.addCar(model);
    }

    public void updateCar(CarData model) {
        repository.updateCar(model);
    }

    public void deleteCar(CarData model) {
        repository.deleteCar(model);
    }

    public LiveData<List<CarData>> getAllCourses() {
        return allCars;
    }
}