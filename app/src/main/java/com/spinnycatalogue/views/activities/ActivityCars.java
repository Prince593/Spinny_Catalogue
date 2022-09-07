package com.spinnycatalogue.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.spinnycatalogue.R;
import com.spinnycatalogue.data.local.CarData;
import com.spinnycatalogue.databinding.ActivityCarsBinding;
import com.spinnycatalogue.repository.CarViewModel;
import com.spinnycatalogue.utils.OnClickImageUtils;
import com.spinnycatalogue.views.adapters.CarAdapter;
import com.spinnycatalogue.views.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityCars extends BaseActivity<ActivityCarsBinding> {
    CarAdapter carAdapter;
    List<CarData> list = new ArrayList<>();
    CarViewModel carViewModel;
    int imagePosition = -1;
    int REQ_CODE_READ_ES = 54;
    int RESULT_LOAD_IMAGE = 125;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cars;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(dataBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Added Cars");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24);
        }


        carAdapter = new CarAdapter(this);
        carAdapter.setData(list);
        carAdapter.setOnClickImageUtils(new OnClickImageUtils() {
            @Override
            public void addImage(int position) {
                imagePosition = position;
                if (checkPermission()) {
                    showImagePicker();
                }
            }

            @Override
            public void deleteCar(int position) {
                carViewModel.deleteCar(list.get(position));
            }
        });


        dataBinding.recyclerView.setHasFixedSize(true);
        dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.recyclerView.setAdapter(carAdapter);


        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);


        carViewModel.getAllCourses().observe(this, new Observer<List<CarData>>() {
            @Override
            public void onChanged(List<CarData> carData) {
                if (carData != null) {
                    list = carData;
                    carAdapter.setData(list);
                    carAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private boolean checkPermission() {
        boolean read = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CODE_READ_ES);
        } else
            read = true;
        return read;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_READ_ES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (imagePosition != -1) {
                    showImagePicker();
                }
            }
        }
    }

    private void showImagePicker() {
        Intent i = new Intent(
                Intent.ACTION_OPEN_DOCUMENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImageUri = data.getData();
            final String path = getPathFromURI(selectedImageUri);
            if (path != null) {
                File f = new File(path);
                selectedImageUri = Uri.fromFile(f);
            }
            CarData carData = list.get(imagePosition);
            carData.setCarImage(selectedImageUri.toString());
            carViewModel.updateCar(carData);
            Toast.makeText(ActivityCars.this, "Image Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}