package krupych.andriy.lvivweather;

import android.app.Application;

/**
 * Singleton which stores all the data for the application.
 *
 * Created by Andrew on 5/21/2015.
 */
public class App extends Application {

    private static App sInstance;

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
