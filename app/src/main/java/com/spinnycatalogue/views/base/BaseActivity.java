package com.spinnycatalogue.views.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.spinnycatalogue.R;
import com.spinnycatalogue.SpinnyCatalogueApplication;
import com.spinnycatalogue.utils.Constants;
import com.spinnycatalogue.utils.LocaleHelper;
import com.spinnycatalogue.utils.NetworkCheck;
import com.spinnycatalogue.utils.PreferencesManager;
import com.spinnycatalogue.views.activities.LoginActivity;
import com.spinnycatalogue.views.activities.MainActivity;

import java.io.InputStream;
import java.util.Scanner;


public abstract class BaseActivity<D extends ViewDataBinding> extends AppCompatActivity implements Constants {

    /*
        BaseActivity is the parent class for all activities.
        All Activities will extend this class and have access to all the methods.
        Shared Preferences and DataBinding Class can be accessed by extending this class.
     */

    @SuppressWarnings("unused")
    public D dataBinding;
    protected SpinnyCatalogueApplication application;
    protected PreferencesManager mPreferencesManager;
    protected NetworkCheck mNetworkCheck;

    // All activities will override this method
    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() != null) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        application = SpinnyCatalogueApplication.getApplication();


        mPreferencesManager = new PreferencesManager(this);
        mNetworkCheck = new NetworkCheck();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected boolean isInternetAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();

    }


    public String readRawResource(@RawRes int res) {
        return readStream(getResources().openRawResource(res));
    }

    private String readStream(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    protected void hideKeyboard(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    protected boolean isValidEmail(String check_email) {
        return (!TextUtils.isEmpty(check_email) && Patterns.EMAIL_ADDRESS.matcher(check_email).matches());
    }


    protected boolean isLoggedIn() {
        return mPreferencesManager.getBooleanValue(LOGIN_STATUS);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;

            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;

        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    protected Drawable tintDrawable(Drawable item, int color) {

        Drawable wrapDrawable = DrawableCompat.wrap(item);
        DrawableCompat.setTint(wrapDrawable, color);

        return wrapDrawable;
    }


    public void goToMainActivity(Context context) {
        //Go to main activity
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void goToLoginActivity(Context context) {
        FirebaseAuth.getInstance().signOut();
        mPreferencesManager.clear();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    protected void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(Context context, int message) {
        Toast.makeText(context, getString(message), Toast.LENGTH_SHORT).show();
    }


    protected void somethingWentWrong() {
        if (application != null)
            Toast.makeText(application, R.string.error_went_wrong, Toast.LENGTH_SHORT).show();
    }


    public SpinnyCatalogueApplication geApplication() {
        return application;
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}
