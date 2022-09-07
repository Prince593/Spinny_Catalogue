package com.spinnycatalogue;

import android.app.Application;
import android.os.Bundle;

public class SpinnyCatalogueApplication extends Application {

    static SpinnyCatalogueApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static SpinnyCatalogueApplication getApplication() {
        return application;
    }
}
