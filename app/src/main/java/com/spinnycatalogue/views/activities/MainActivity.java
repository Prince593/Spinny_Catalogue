package com.spinnycatalogue.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.spinnycatalogue.R;
import com.spinnycatalogue.data.local.CarData;
import com.spinnycatalogue.data.model.getCarMakes.CarMakeResponse;
import com.spinnycatalogue.data.model.getCarMakes.MakeResults;
import com.spinnycatalogue.data.model.getCarModels.CarModelResponse;
import com.spinnycatalogue.data.model.getCarModels.ModelResults;
import com.spinnycatalogue.databinding.ActivityMainBinding;
import com.spinnycatalogue.databinding.BottomsheetAddCarBinding;
import com.spinnycatalogue.network.APIClient;
import com.spinnycatalogue.repository.CarViewModel;
import com.spinnycatalogue.views.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    List<MakeResults> carMakes = new ArrayList<>();
    List<ModelResults> carModels = new ArrayList<>();
    ProgressDialog progressDialog;
    Context context;
    CarViewModel carViewModel;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        dataBinding.cardAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carMakes.size() == 0) {
                    Toast.makeText(context, R.string.error_went_wrong, Toast.LENGTH_SHORT).show();
                    getCarMakes();
                } else {
                    showAddBottomSheetDialog();
                }
            }
        });


        dataBinding.cardAddedCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ActivityCars.class));
            }
        });



        dataBinding.cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity(context);
                finish();
            }
        });


        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        getCarMakes();
    }

    private void showAddBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        BottomsheetAddCarBinding addCarBinding = BottomsheetAddCarBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(addCarBinding.getRoot());

        ArrayAdapter<MakeResults> makeAdapter = new ArrayAdapter<MakeResults>(context, android.R.layout.simple_list_item_1, carMakes);
        makeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        addCarBinding.spinnerCarMake.setAdapter(makeAdapter);


        ArrayAdapter<ModelResults> modelAdapter = new ArrayAdapter<ModelResults>(context, android.R.layout.simple_list_item_1, carModels);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        addCarBinding.spinnerCarModel.setAdapter(modelAdapter);


        addCarBinding.spinnerCarMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getCarModels(carMakes.get(i).getMakeId(), addCarBinding.spinnerCarModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addCarBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = addCarBinding.spinnerCarModel.getSelectedItemPosition();
                addCar(carModels.get(selected),bottomSheetDialog);
                bottomSheetDialog.dismiss();
            }
        });


        addCarBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }


    private void getCarMakes() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<CarMakeResponse> call = APIClient.getInstance().getCarMakes(
                "json"
        );

        call.enqueue(new Callback<CarMakeResponse>() {
            @Override
            public void onResponse(Call<CarMakeResponse> call, Response<CarMakeResponse> response) {
                if (response.body() != null) {
                    carMakes = response.body().getResults();
                    getCarModels(carMakes.get(0).getMakeId(), null);
                } else
                    progressDialog.hide();

            }

            @Override
            public void onFailure(Call<CarMakeResponse> call, Throwable t) {
                carMakes = new ArrayList<>();
                progressDialog.hide();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCarModels(int makeId, Spinner spinnerCarModel) {
        Call<CarModelResponse> call = APIClient.getInstance().getCarModels(
                String.valueOf(makeId),
                "json"
        );

        call.enqueue(new Callback<CarModelResponse>() {
            @Override
            public void onResponse(Call<CarModelResponse> call, Response<CarModelResponse> response) {
                progressDialog.hide();
                if (response.body() != null) {
                    carModels = response.body().getResults();

                    if (spinnerCarModel != null) {
                        ArrayAdapter<ModelResults> modelAdapter = new ArrayAdapter<ModelResults>(context, android.R.layout.simple_list_item_1, carModels);
                        modelAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinnerCarModel.setAdapter(modelAdapter);
                    }

                }
            }

            @Override
            public void onFailure(Call<CarModelResponse> call, Throwable t) {
                carModels = new ArrayList<>();
                if (spinnerCarModel != null) {
                    ArrayAdapter<ModelResults> modelAdapter = new ArrayAdapter<ModelResults>(context, android.R.layout.simple_list_item_1, carModels);
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinnerCarModel.setAdapter(modelAdapter);
                }
                progressDialog.hide();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addCar(ModelResults modelResults, BottomSheetDialog bottomSheetDialog) {
        CarData carData = new CarData(0, modelResults.getModelName(), modelResults.getModelId(), modelResults.getMakeName(), modelResults.getMakeId(), "");
        carViewModel.addCar(carData);
        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
    }
}