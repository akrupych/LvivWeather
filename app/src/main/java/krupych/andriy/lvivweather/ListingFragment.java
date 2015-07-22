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

public class ListingFragment extends Fragment {

    public interface ListingCallback {
        void onWeatherItemSelected(DailyWeatherModel weather);
    }

    private ListingCallback mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (ListingCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new WeatherForecastAdapter());
        return view;
    }

    private class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastViewHolder> {

        private LayoutInflater mInflater = LayoutInflater.from(getActivity());
        private int mSelectedPosition = -1;
        private int mSelectedColor = getActivity().getResources().getColor(R.color.listing_selected_item_bg);
        private int mStandardColor = getActivity().getResources().getColor(R.color.listing_standard_item_bg);

        @Override
        public WeatherForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WeatherForecastViewHolder(mInflater.inflate(R.layout.item_forecast, parent, false));
        }

        @Override
        public void onBindViewHolder(WeatherForecastViewHolder holder, final int position) {
            final DailyWeatherModel weather = App.getInstance().getWeatherForecast().get(position);
            holder.bind(weather);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPosition = position;
                    mCallback.onWeatherItemSelected(weather);
                    notifyDataSetChanged();
                }
            });
            holder.itemView.setBackgroundColor(mSelectedPosition == position ? mSelectedColor : mStandardColor);
        }

        @Override
        public int getItemCount() {
            return App.getInstance().getWeatherForecast().size();
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
    }
}
