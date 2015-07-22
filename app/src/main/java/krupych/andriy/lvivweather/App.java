package krupych.andriy.lvivweather;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import krupych.andriy.lvivweather.model.BaseWeatherModel;
import krupych.andriy.lvivweather.model.DailyWeatherModel;
import krupych.andriy.lvivweather.model.InstantWeatherModel;

/**
 * Singleton which stores all the data for the application.
 *
 * Created by Andrew on 5/21/2015.
 */
public class App extends Application {

    private static final int REFRESH_INTERVAL_MINUTES = 10;
    private static App sInstance;

    private InstantWeatherModel mCurrentWeather;
    private List<InstantWeatherModel> mShortForecast;
    private List<DailyWeatherModel> mLongForecast;

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public void loadWeather(Runnable onComplete) {
        new LoadDataTask(false, onComplete).execute();
    }

    public void loadCurrentWeather(Runnable onComplete) {
        new LoadDataTask(true, onComplete).execute();
    }

    public boolean isCurrentWeatherActual() {
        int minutesElapsed = Minutes.minutesBetween(DateTime.now(), mCurrentWeather.dateTime).getMinutes();
        return minutesElapsed < REFRESH_INTERVAL_MINUTES;
    }

    public BaseWeatherModel getCurrentWeather() {
        return mCurrentWeather;
    }

    public List<DailyWeatherModel> getWeatherForecast() {
        return mLongForecast;
    }

    public DailyWeatherModel getWeatherForDate(DateTime date) {
        for (DailyWeatherModel weather : mLongForecast) {
            if (weather.dateTime.equals(date)) {
                return weather;
            }
        }
        return null;
    }

    public static int getDrawableIdForWeatherIcon(String iconId) {
        String drawableName = "weather_" + iconId;
        return sInstance.getResources().getIdentifier(drawableName, "drawable", sInstance.getPackageName());
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

        private final Runnable mOnComplete;
        private final boolean mCurrentWeatherOnly;

        public LoadDataTask(boolean currentWeatherOnly, Runnable onComplete) {
            mCurrentWeatherOnly = currentWeatherOnly;
            mOnComplete = onComplete;
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadCurrentWeather();
            if (!mCurrentWeatherOnly) loadWeatherForecast();
            return null;
        }

        private void loadWeatherForecast() {
            try {
                String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Lviv,ua&lang=ua&units=metric";
                JSONObject jsonObject = new JSONObject(IOUtils.toString(new URL(url).openStream()));
                JSONArray list = jsonObject.getJSONArray("list");
                mLongForecast = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    mLongForecast.add(new DailyWeatherModel(list.getJSONObject(i)));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        private void loadCurrentWeather() {
            try {
                String url = "http://api.openweathermap.org/data/2.5/weather?q=Lviv,ua&lang=ua&units=metric";
                mCurrentWeather = new InstantWeatherModel(new JSONObject(IOUtils.toString(new URL(url).openStream())));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mCurrentWeather == null) {
                Toast.makeText(App.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
            } else {
                mOnComplete.run();
            }
        }
    }
}
