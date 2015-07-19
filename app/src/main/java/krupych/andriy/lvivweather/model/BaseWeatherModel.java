package krupych.andriy.lvivweather.model;

import android.graphics.Bitmap;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseWeatherModel {

    public DateTime dateTime;
    public String imageId;
    public String description;
    public int pressure;
    public int humidity;
    public int windSpeed;
    public int windDirection;
    public int cloudsPercentage;

    public BaseWeatherModel(JSONObject jsonObject) throws JSONException {
        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        dateTime = jsonObject.has("dt") ?
                new DateTime(jsonObject.getLong("dt")) : DateTime.now();
        imageId = weather.getString("icon");
        description = weather.getString("description");
    }

}
