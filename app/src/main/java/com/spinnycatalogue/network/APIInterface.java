package com.spinnycatalogue.network;

import com.spinnycatalogue.data.model.getCarMakes.CarMakeResponse;
import com.spinnycatalogue.data.model.getCarModels.CarModelResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("getallmakes")
    Call<CarMakeResponse> getCarMakes(
            @Query("format") String format
    );

    @GET("GetModelsForMakeId/{make_id}")
    Call<CarModelResponse> getCarModels(
            @Path("make_id") String makeId,
            @Query("format") String format
    );


}