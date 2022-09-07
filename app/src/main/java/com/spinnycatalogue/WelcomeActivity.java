package com.spinnycatalogue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.spinnycatalogue.utils.Constants;
import com.spinnycatalogue.utils.PreferencesManager;
import com.spinnycatalogue.views.activities.LoginActivity;
import com.spinnycatalogue.views.activities.MainActivity;


public class WelcomeActivity extends AppCompatActivity implements Constants {


    Context context;
    PreferencesManager sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() != null) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
        setContentView(R.layout.activity_welcome);

        context = this;
        sharedPreferences = new PreferencesManager(context);

        finalTask();
    }


    public void finalTask() {
        new Handler().postDelayed(() -> {

            boolean logged_in = sharedPreferences.getBooleanValue(LOGIN_STATUS);
            if (logged_in) {

                startActivity(new Intent(context, MainActivity.class));

            } else {
                startActivity(new Intent(context, LoginActivity.class));
            }

        }, 2000);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
