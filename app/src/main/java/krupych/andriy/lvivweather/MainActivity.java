package krupych.andriy.lvivweather;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... params) {
                try {
                    String url = "http://api.openweathermap.org/data/2.5/weather?q=Lviv,ua&lang=ua";
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
        
        JSONObject sys = data.getJSONObject("sys");

        long sunrise = sys.getLong("sunrise") * 1000;
        LocalTime localTime = new LocalTime(sunrise);
        Log.d("qwerty", sunrise + " " + localTime.toString());
        
        TextView sunriseView = (TextView) findViewById(R.id.sunrise);
        LocalTime sunriseTime = new LocalTime(sys.getLong("sunrise") * 1000); // Unix timestamp is in seconds
        String sunriseString = getString(R.string.sunrise) + " " + sunriseTime.toString("HH:mm");
        sunriseView.setText(sunriseString);

        TextView sunsetView = (TextView) findViewById(R.id.sunset);
        LocalTime sunsetTime = new LocalTime(sys.getLong("sunset") * 1000); // Unix timestamp is in seconds
        String sunsetString = getString(R.string.sunset) + " " + sunsetTime.toString("HH:mm");
        sunsetView.setText(sunsetString);

    }

}
