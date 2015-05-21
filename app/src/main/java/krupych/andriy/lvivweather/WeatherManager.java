package krupych.andriy.lvivweather;

/**
 * Singleton which handles weather data loading.
 *
 * Created by Andrew on 5/21/2015.
 */
public class WeatherManager {

    //////////////////////////// STATIC //////////////////////////////////

    private static final int UPDATE_INTERVAL = 10; // minutes

    private static WeatherManager sInstance = new WeatherManager();

    public static WeatherManager getInstance() {
        return sInstance;
    }

    private WeatherManager() {}

    /////////////////////////// INSTANCE //////////////////////////////////

    private WeatherModel mWeather;

    public boolean isWeatherValid() {
        return mWeather != null && mWeather.updated.plusMinutes(UPDATE_INTERVAL).isAfterNow();
    }

    public WeatherModel getWeather() {
        return mWeather;
    }

    public void loadWeather() {

    }
}
