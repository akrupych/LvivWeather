package krupych.andriy.lvivweather.model;

import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

public class InstantWeatherModel extends BaseWeatherModel {

    public LocalTime sunrise;
    public LocalTime sunset;
    public int temperature;

    public InstantWeatherModel(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        JSONObject main = jsonObject.getJSONObject("main");
        JSONObject wind = jsonObject.getJSONObject("wind");
        JSONObject clouds = jsonObject.getJSONObject("clouds");
        pressure = (int) main.getDouble("pressure");
        humidity = main.getInt("humidity");
        windSpeed = (int) wind.getDouble("speed");
        windDirection = (int) wind.optDouble("deg", 0);
        cloudsPercentage = clouds.getInt("all");
        temperature = (int) main.getDouble("temp");
        if (jsonObject.has("sys")) {
            JSONObject sys = jsonObject.getJSONObject("sys");
            sunrise = new LocalTime(sys.getLong("sunrise") * 1000); // Unix timestamp is in seconds
            sunset = new LocalTime(sys.getLong("sunset") * 1000); // Unix timestamp is in seconds
        }
    }

    public boolean hasSunriseSunset() {
        return sunrise != null && sunset != null;
    }
}
