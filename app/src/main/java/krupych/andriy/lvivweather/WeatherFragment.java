package krupych.andriy.lvivweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import krupych.andriy.lvivweather.model.BaseWeatherModel;
import krupych.andriy.lvivweather.model.DailyWeatherModel;
import krupych.andriy.lvivweather.model.InstantWeatherModel;

public class WeatherFragment extends Fragment {

    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String ARG_IS_CURRENT_WEATHER = "ARG_IS_CURRENT_WEATHER";
    private static final String ARG_SELECTED_DATE = "ARG_SELECTED_DATE";

    private BaseWeatherModel mWeather;

    private View mTimesOfDayPanel;
    private TextView mNightTemperatureView;
    private TextView mMorningTemperatureView;
    private TextView mDayTemperatureView;
    private TextView mEveningTemperatureView;
    private View mSunriseSunsetPanel;
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

    public static Fragment forDate(DailyWeatherModel weather) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_CURRENT_WEATHER, false);
        args.putLong(ARG_SELECTED_DATE, weather.dateTime.getMillis());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        // times of day views
        mTimesOfDayPanel = view.findViewById(R.id.times_of_day_panel);
        mNightTemperatureView = (TextView) view.findViewById(R.id.night_temperature);
        mMorningTemperatureView = (TextView) view.findViewById(R.id.morning_temperature);
        mDayTemperatureView = (TextView) view.findViewById(R.id.day_temperature);
        mEveningTemperatureView = (TextView) view.findViewById(R.id.evening_temperature);
        // sunrise/sunset views
        mSunriseSunsetPanel = view.findViewById(R.id.sunrise_sunset_panel);
        mSunriseView = (TextView) view.findViewById(R.id.sunrise);
        mSunsetView = (TextView) view.findViewById(R.id.sunset);
        // central panel
        mTemperatureView = (TextView) view.findViewById(R.id.temperature);
        mWeatherIcon = (ImageView) view.findViewById(R.id.weather_icon);
        mDescriptionView = (TextView) view.findViewById(R.id.weather_description);
        // bottom panel
        mPressureView = (TextView) view.findViewById(R.id.pressure);
        mHumidityView = (TextView) view.findViewById(R.id.humidity);
        mWindSpeedView = (TextView) view.findViewById(R.id.wind);
        mCloudsView = (TextView) view.findViewById(R.id.clouds);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillView();
    }

    private void loadData() {
        if (getArguments().getBoolean(ARG_IS_CURRENT_WEATHER)) {
            if (App.getInstance().isCurrentWeatherActual()) {
                loadCurrentWeather();
            } else {
                App.getInstance().loadCurrentWeather(new Runnable() {
                    @Override
                    public void run() {
                        loadCurrentWeather();
                    }
                });
            }
        } else {
            loadWeatherForecast();
        }
    }

    private void loadCurrentWeather() {
        mWeather = App.getInstance().getCurrentWeather();
        fillView();
    }

    private void loadWeatherForecast() {
        DateTime date = new DateTime(getArguments().getLong(ARG_SELECTED_DATE));
        mWeather = App.getInstance().getWeatherForDate(date);
        fillView();
    }

    private void fillView() {
        if (mWeather != null && getView() != null) {
            if (mWeather instanceof InstantWeatherModel) {
                fillView((InstantWeatherModel) mWeather);
            } else if (mWeather instanceof DailyWeatherModel) {
                fillView((DailyWeatherModel) mWeather);
            }
        }
    }

    private void fillView(InstantWeatherModel weather) {
        mTimesOfDayPanel.setVisibility(View.GONE);
        mSunriseSunsetPanel.setVisibility(weather.hasSunriseSunset() ? View.VISIBLE : View.GONE);
        if (weather.hasSunriseSunset()) {
            mSunriseView.setText(weather.sunrise.toString("HH:mm"));
            mSunsetView.setText(weather.sunset.toString("HH:mm"));
        }
        mTemperatureView.setText(weather.temperature + " " + getString(R.string.temperature_unit));
        int drawableId = App.getDrawableIdForWeatherIcon(weather.imageId);
        mWeatherIcon.setImageResource(drawableId);
        mDescriptionView.setText(weather.description);
        mPressureView.setText(weather.pressure + " " + getString(R.string.pressure_unit));
        mHumidityView.setText(weather.humidity + " " + getString(R.string.humidity_unit));
        mWindSpeedView.setText(weather.windSpeed + " " + getString(R.string.wind_speed_unit));
        mCloudsView.setText(weather.cloudsPercentage + " " + getString(R.string.clouds_unit));
    }

    private void fillView(DailyWeatherModel weather) {
        mSunriseSunsetPanel.setVisibility(View.GONE);
        mTimesOfDayPanel.setVisibility(View.VISIBLE);
        mNightTemperatureView.setText(String.format("%d\u2103", weather.nightTemperature));
        mMorningTemperatureView.setText(String.format("%d\u2103", weather.morningTemperature));
        mDayTemperatureView.setText(String.format("%d\u2103", weather.dayTemperature));
        mEveningTemperatureView.setText(String.format("%d\u2103", weather.eveningTemperature));
        mTemperatureView.setText(String.format("%d - %d\u2103", weather.minTemperature, weather.maxTemperature));
        int drawableId = App.getDrawableIdForWeatherIcon(weather.imageId);
        mWeatherIcon.setImageResource(drawableId);
        mDescriptionView.setText(weather.description);
        mPressureView.setText(weather.pressure + " " + getString(R.string.pressure_unit));
        mHumidityView.setText(weather.humidity + " " + getString(R.string.humidity_unit));
        mWindSpeedView.setText(weather.windSpeed + " " + getString(R.string.wind_speed_unit));
        mCloudsView.setText(weather.cloudsPercentage + " " + getString(R.string.clouds_unit));
    }
}
