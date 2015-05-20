package krupych.andriy.lvivweather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... params) {
                try {
                    String url = "http://api.openweathermap.org/data/2.5/weather?q=Lviv,ua&lang=ua&units=metric";
                    return new JSONObject(IOUtils.toString(new URL(url).openStream()));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result == null) toast("Error retrieving data");
                else try {
                    setupView(result);
                } catch (JSONException e) {
                    toast("Error parsing data");
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void toast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void setupView(JSONObject data) throws JSONException {
        setupSysView(data);
        setupWeatherView(data);
        setupMainView(data);
    }

    private void setupSysView(JSONObject data) throws JSONException {
        JSONObject sys = data.getJSONObject("sys");

        TextView sunriseView = (TextView) findViewById(R.id.sunrise);
        LocalTime sunriseTime = new LocalTime(sys.getLong("sunrise") * 1000); // Unix timestamp is in seconds
        String sunriseString = getString(R.string.sunrise) + " " + sunriseTime.toString("HH:mm");
        sunriseView.setText(sunriseString);

        TextView sunsetView = (TextView) findViewById(R.id.sunset);
        LocalTime sunsetTime = new LocalTime(sys.getLong("sunset") * 1000); // Unix timestamp is in seconds
        String sunsetString = getString(R.string.sunset) + " " + sunsetTime.toString("HH:mm");
        sunsetView.setText(sunsetString);
    }

    private void setupWeatherView(JSONObject data) throws JSONException {
        JSONObject weather = data.getJSONArray("weather").getJSONObject(0);

        final ImageView iconView = (ImageView) findViewById(R.id.weather_icon);
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    String imageId = params[0];
                    String url = String.format("http://openweathermap.org/img/w/%s.png", imageId);
                    Log.d(TAG, "loading image: " + url);
                    return BitmapFactory.decodeStream(new URL(url).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) iconView.setImageBitmap(bitmap);
            }
        }.execute(weather.getString("icon"));

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(weather.getString("description"));
    }

    private void setupMainView(JSONObject data) throws JSONException {
        JSONObject main = data.getJSONObject("main");

        TextView temperatureView = (TextView) findViewById(R.id.temperature);
        int temperature = (int) main.getDouble("temp");
        temperatureView.setText(String.format("%s %d \u2103", getString(R.string.temperature), temperature));

        TextView pressureView = (TextView) findViewById(R.id.pressure);
        int pressure = (int) main.getDouble("pressure");
        pressureView.setText(String.format("%s %d %s",
                getString(R.string.pressure), pressure, getString(R.string.pressure_units)));

        TextView humidityView = (TextView) findViewById(R.id.humidity);
        int humidity = main.getInt("humidity");
        humidityView.setText(String.format("%s %d %%", getString(R.string.humidity), humidity));
    }

}
