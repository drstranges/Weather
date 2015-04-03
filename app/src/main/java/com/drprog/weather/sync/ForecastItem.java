package com.drprog.weather.sync;

/**
 * Created on 03.04.2015.
 */

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class store current weather forecast for a certain place
 * Data takes from json Object like this:
 * {
 * "coord": {
 * "lon": -81.23,
 * "lat": 42.98
 * },
 * "sys": {
 * "message": 0.0149,
 * "country": "CA",
 * "sunrise": 1428058951,
 * "sunset": 1428105219
 * },
 * "weather": [
 * {
 * "id": 802,
 * "main": "Clouds",
 * "description": "scattered clouds",
 * "icon": "03n"
 * }
 * ],
 * "base": "stations",
 * "main": {
 * "temp": 275.129,
 * "temp_min": 275.129,
 * "temp_max": 275.129,
 * "pressure": 987.23,
 * "sea_level": 1025.31,
 * "grnd_level": 987.23,
 * "humidity": 94
 * },
 * "wind": {
 * "speed": 1.76,
 * "deg": 228.5
 * },
 * "clouds": {
 * "all": 48
 * },
 * "dt": 1428053994,
 * "id": 6058560,
 * "name": "London",
 * "cod": 200
 * }
 * (see openweathermap.org/data/2.5/weather/?q=London)
 * Temperature in Kelvin (subtract 273.15 to convert to Celsius)
 */
public class ForecastItem {
    public String placeName;    // City name
    public double coordLon;      // City geo location, lon
    public double coordLat;      // City geo location, lat
    public long dateTime;       // dataTime in milliseconds
    public int weatherId;       // Weather condition codes
    public String description;  // Weather condition description
    public String iconName;     // name of icon to show a weather condition
    public double pressure;     // Atmospheric pressure, hPa
    public int humidity;        // Humidity, %
    public double windSpeed;    // Wind speed, mps
    public double windDirection; // Wind direction, degrees (meteorological)
    public double highTemp;      // Maximum temperature at the moment.
    public double lowTemp;      // Minimum temperature at the moment.


    public ForecastItem(JSONObject jsonObject) throws JSONException {
        parseJsonWeather(jsonObject);
    }


    public void parseJsonWeather(JSONObject jsonForecast) throws JSONException {

        placeName = jsonForecast.getString(OpenWeatherContract.OWM_CITY_NAME);
        JSONObject coordObject = jsonForecast.getJSONObject(OpenWeatherContract.OWM_CITY_COORD);
        coordLat = coordObject.getDouble(OpenWeatherContract.OWM_CITY_COORD_LAT);
        coordLon = coordObject.getDouble(OpenWeatherContract.OWM_CITY_COORD_LON);

        // The date/time is returned as a long in unixtime GMT (for convert multiply 1000).
        dateTime = jsonForecast.getLong(OpenWeatherContract.OWM_DATETIME) * 1000;

        // Description is in a child array called "weather", which is 1 element long.
        JSONObject weatherObject =
                jsonForecast.getJSONArray(OpenWeatherContract.OWM_WEATHER).getJSONObject(0);
        description = weatherObject.getString(OpenWeatherContract.OWM_DESCRIPTION);
        weatherId = weatherObject.getInt(OpenWeatherContract.OWM_WEATHER_ID);
        iconName = weatherObject.getString(OpenWeatherContract.OWM_WEATHER_ICON);

        // Temperatures are in a child object called "main".
        JSONObject temperatureObject =
                jsonForecast.getJSONObject(OpenWeatherContract.OWM_MAIN);
        highTemp = temperatureObject.getDouble(OpenWeatherContract.OWM_MAX_TEMP);
        lowTemp = temperatureObject.getDouble(OpenWeatherContract.OWM_MIN_TEMP);

        pressure = temperatureObject.getDouble(OpenWeatherContract.OWM_PRESSURE);
        humidity = temperatureObject.getInt(OpenWeatherContract.OWM_HUMIDITY);

        JSONObject windObject =
                jsonForecast.getJSONObject(OpenWeatherContract.OWM_WIND);
        windSpeed = windObject.getDouble(OpenWeatherContract.OWM_WINDSPEED);
        windDirection = windObject.getDouble(OpenWeatherContract.OWM_WIND_DIRECTION);
    }

    public String getWindDirection() {
        // From wind direction in degrees, determine compass direction as a string (e.g NW)
        String direction = "";
        if (windDirection >= 337.5 || windDirection < 22.5) {
            direction = "N";
        } else if (windDirection >= 22.5 && windDirection < 67.5) {
            direction = "NE";
        } else if (windDirection >= 67.5 && windDirection < 112.5) {
            direction = "E";
        } else if (windDirection >= 112.5 && windDirection < 157.5) {
            direction = "SE";
        } else if (windDirection >= 157.5 && windDirection < 202.5) {
            direction = "S";
        } else if (windDirection >= 202.5 && windDirection < 247.5) {
            direction = "SW";
        } else if (windDirection >= 247.5 && windDirection < 292.5) {
            direction = "W";
        } else if (windDirection >= 292.5 || windDirection < 337.5) {
            direction = "NW";
        }
        return direction;
    }

    public double getTemperatureInDegree(double temp) {
        return temp - 273.15;
    }
}
