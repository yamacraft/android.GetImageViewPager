package io.github.yamacraft.getimageviewpager;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by yamacraft on 2016/03/01.
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
