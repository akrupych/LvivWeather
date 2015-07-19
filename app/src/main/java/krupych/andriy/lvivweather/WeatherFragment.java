package krupych.andriy.lvivweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import krupych.andriy.lvivweather.model.BaseWeatherModel;
import krupych.andriy.lvivweather.model.InstantWeatherModel;

public class WeatherFragment extends Fragment {

    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String ARG_IS_CURRENT_WEATHER = "ARG_IS_CURRENT_WEATHER";

    private BaseWeatherModel mWeather;

    private TextView mSunriseView;
    private TextView mSunsetView;
    private ImageView mWeatherIcon;
    private TextView mDescriptionView;
    private TextView mTemperatureView;
    private TextView mPressureView;
    private TextView mHumidityView;
    private TextView mWindSpeedView;
    private TextView mCloudsView;

    public static WeatherFragment forCurrentWeather() {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_CURRENT_WEATHER, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        mSunriseView = (TextView) view.findViewById(R.id.sunrise);
        mSunsetView = (TextView) view.findViewById(R.id.sunset);
        mTemperatureView = (TextView) view.findViewById(R.id.temperature);
        mWeatherIcon = (ImageView) view.findViewById(R.id.weather_icon);
        mDescriptionView = (TextView) view.findViewById(R.id.weather_description);
        mPressureView = (TextView) view.findViewById(R.id.pressure);
        mHumidityView = (TextView) view.findViewById(R.id.humidity);
        mWindSpeedView = (TextView) view.findViewById(R.id.wind);
        mCloudsView = (TextView) view.findViewById(R.id.clouds);
        loadData();
        fillView();
        return view;
    }

    private void loadData() {
        if (getArguments().getBoolean(ARG_IS_CURRENT_WEATHER)) {
            if (App.getInstance().isCurrentWeatherActual()) {
                mWeather = App.getInstance().getCurrentWeather();
                fillView();
            } else {
                App.getInstance().loadCurrentWeather(new Runnable() {
                    @Override
                    public void run() {
                        mWeather = App.getInstance().getCurrentWeather();
                        fillView();
                    }
                });
            }
        }
    }

    private void fillView() {
        if (mWeather != null && mWeather instanceof InstantWeatherModel) {
            InstantWeatherModel instantWeather = (InstantWeatherModel) mWeather;
            if (instantWeather.hasSunriseSunset()) {
                mSunriseView.setText(instantWeather.sunrise.toString("HH:mm"));
                mSunsetView.setText(instantWeather.sunset.toString("HH:mm"));
            }
            mTemperatureView.setText(instantWeather.temperature + " " + getString(R.string.temperature_unit));
            String drawableName = "weather_" + instantWeather.imageId;
            int drawableId = getResources().getIdentifier(drawableName, "drawable", App.getInstance().getPackageName());
            mWeatherIcon.setImageResource(drawableId);
            mDescriptionView.setText(instantWeather.description);
            mPressureView.setText(instantWeather.pressure + " " + getString(R.string.pressure_unit));
            mHumidityView.setText(instantWeather.humidity + " " + getString(R.string.humidity_unit));
            mWindSpeedView.setText(instantWeather.windSpeed + " " + getString(R.string.wind_speed_unit));
            mCloudsView.setText(instantWeather.cloudsPercentage + " " + getString(R.string.clouds_unit));
        }
    }
}
