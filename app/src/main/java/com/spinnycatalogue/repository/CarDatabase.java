package com.spinnycatalogue.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.spinnycatalogue.data.local.CarData;

@Database(entities = {CarData.class}, version = 1)
public abstract class CarDatabase extends RoomDatabase {
    private static CarDatabase instance;

    public static synchronized CarDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CarDatabase.class, "car_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract CarDao carDao();


}