package krupych.andriy.lvivweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import krupych.andriy.lvivweather.model.DailyWeatherModel;

public class MainActivity extends AppCompatActivity implements ListingFragment.ListingCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListingFragment mListingFragment;
    private WeatherFragment mContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            // first run
            setListingFragment(new LoadingFragment());
            setContentFragment(new LoadingFragment());
            loadWeather();
        }
    }

    private void loadWeather() {
        App.getInstance().loadWeather(new Runnable() {
            @Override
            public void run() {
                mListingFragment = new ListingFragment();
                setListingFragment(mListingFragment);
                mContentFragment = WeatherFragment.forCurrentWeather();
                setContentFragment(mContentFragment);
            }
        });
    }

    private void setListingFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_listing, fragment).commit();
    }

    private void setContentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment).commit();
    }

    @Override
    public void onWeatherItemSelected(DailyWeatherModel weather) {
        setContentFragment(WeatherFragment.forDate(weather));
    }
}