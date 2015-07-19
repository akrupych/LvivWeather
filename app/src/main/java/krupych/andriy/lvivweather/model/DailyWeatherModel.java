package krupych.andriy.lvivweather.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DailyWeatherModel extends BaseWeatherModel {

    public int minTemperature;
    public int maxTemperature;
    public int nightTemperature;
    public int morningTemperature;
    public int dayTemperature;
    public int eveningTemperature;

    public DailyWeatherModel(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        JSONObject temperatureJson = jsonObject.getJSONObject("temp");
        pressure = (int) jsonObject.getDouble("pressure");
        humidity = jsonObject.getInt("humidity");
        windSpeed = (int) jsonObject.getDouble("speed");
        windDirection = (int) jsonObject.getDouble("deg");
        cloudsPercentage = jsonObject.getInt("clouds");
        minTemperature = (int) temperatureJson.getDouble("min");
        maxTemperature = (int) temperatureJson.getDouble("max");
        nightTemperature = (int) temperatureJson.getDouble("night");
        morningTemperature = (int) temperatureJson.getDouble("morn");
        dayTemperature = (int) temperatureJson.getDouble("day");
        eveningTemperature = (int) temperatureJson.getDouble("eve");
    }

}
