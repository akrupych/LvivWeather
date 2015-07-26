package krupych.andriy.lvivweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import krupych.andriy.lvivweather.model.DailyWeatherModel;
import krupych.andriy.lvivweather.model.InstantWeatherModel;

public class ListingFragment extends Fragment {

    public interface ListingCallback {
        void onWeatherItemSelected(DailyWeatherModel weather);
        void onCurrentWeatherItemSelected();
    }

    private ListingCallback mCallback;
    private WeatherForecastAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (ListingCallback) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mAdapter == null) {
            mAdapter = new WeatherForecastAdapter();
        }
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    private class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastViewHolder> {

        private LayoutInflater mInflater = LayoutInflater.from(getActivity());
        private int mSelectedPosition = 0;
        private int mSelectedColor = getActivity().getResources().getColor(R.color.listing_selected_item_bg);
        private int mStandardColor = getActivity().getResources().getColor(R.color.listing_standard_item_bg);

        @Override
        public WeatherForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WeatherForecastViewHolder(mInflater.inflate(R.layout.item_forecast, parent, false));
        }

        @Override
        public void onBindViewHolder(WeatherForecastViewHolder holder, int position) {
            if (position > 0) {
                onBindWeaherForecast(holder, position - 1);
            } else {
                onBindCurrentWeather(holder);
            }
        }

        private void onBindCurrentWeather(WeatherForecastViewHolder holder) {
            holder.bind((InstantWeatherModel) App.getInstance().getCurrentWeather());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPosition = 0;
                    mCallback.onCurrentWeatherItemSelected();
                    notifyDataSetChanged();
                }
            });
            holder.itemView.setBackgroundColor(mSelectedPosition == 0 ? mSelectedColor : mStandardColor);
        }

        private void onBindWeaherForecast(WeatherForecastViewHolder holder, final int position) {
            final DailyWeatherModel weather = App.getInstance().getWeatherForecast().get(position);
            holder.bind(weather);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPosition = position + 1;
                    mCallback.onWeatherItemSelected(weather);
                    notifyDataSetChanged();
                }
            });
            holder.itemView.setBackgroundColor(mSelectedPosition == position + 1 ? mSelectedColor : mStandardColor);
        }

        @Override
        public int getItemCount() {
            return App.getInstance().getWeatherForecast().size() + 1;
        }
    }

    private class WeatherForecastViewHolder extends RecyclerView.ViewHolder {

        private TextView mDateView;
        private ImageView mIconView;
        private TextView mTemperatureView;

        public WeatherForecastViewHolder(View itemView) {
            super(itemView);
            mDateView = (TextView) itemView.findViewById(R.id.item_date);
            mIconView = (ImageView) itemView.findViewById(R.id.item_icon);
            mTemperatureView = (TextView) itemView.findViewById(R.id.item_temperature);
        }

        public void bind(DailyWeatherModel weather) {
            mDateView.setText(weather.dateTime.toString("EEE, d MMM"));
            mIconView.setImageResource(App.getDrawableIdForWeatherIcon(weather.imageId));
            mTemperatureView.setText(String.format("%d - %d\u2103", weather.minTemperature, weather.maxTemperature));
        }

        public void bind(InstantWeatherModel weather) {
            mDateView.setText(getString(R.string.now));
            mIconView.setImageResource(App.getDrawableIdForWeatherIcon(weather.imageId));
            mTemperatureView.setText(String.format("%d\u2103", weather.temperature));
        }
    }
}
