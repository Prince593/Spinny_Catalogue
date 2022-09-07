package com.spinnycatalogue.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper implements Constants {




    public static Context setLocale(Context context) {
        return updateResources(context, getLanguagePref(context));
    }


    public static Context setNewLocale(Context context, String language) {
        setLanguagePref(context, language);
        language+="   ";
        language =  language.substring(0,2);

        return updateResources(context, language);
    }

    private static void setLanguagePref(Context context, String language) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        preferencesManager.setStringValue(SELECTED_LANGUAGE, language);
    }

    public static String getLanguagePref(Context context) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        return preferencesManager.getStringValue(SELECTED_LANGUAGE,"en-gb");
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration configuration = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return context;
    }

    public static Locale getLocale(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? configuration.getLocales().get(0) : configuration.locale;
    }

}
