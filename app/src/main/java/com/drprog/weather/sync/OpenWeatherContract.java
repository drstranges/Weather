package com.drprog.weather.sync;

/**
 * Helper class for providing OpenWeatherMap API constants
 */
public class OpenWeatherContract {

    public static final String ROOT_URL = "http://api.openweathermap.org/";
    public static final String WEATHER_BY_PLACE_ENDPOINT_URL = "data/2.5/weather/?q=";
    private static final String IMAGE_ENDPOINT_URL = "img/w/";
    private static final String IMG_EXT = ".png";

    //Json Items (see openweathermap.org/data/2.5/weather/?q=London)
    public static final String OWM_CITY_NAME = "name";
    public static final String OWM_CITY_COORD = "coord";
    public static final String OWM_CITY_COORD_LAT = "lat";
    public static final String OWM_CITY_COORD_LON = "lon";
    public static final String OWM_DATETIME = "dt";
    // The wind description are children of the "wind" object
    public static final String OWM_WIND = "wind";
    public static final String OWM_WINDSPEED = "speed";
    public static final String OWM_WIND_DIRECTION = "deg";
    // The weather description are children of the "weather" object
    public static final String OWM_WEATHER = "weather";
    public static final String OWM_DESCRIPTION = "main";
    public static final String OWM_WEATHER_ID = "id";
    public static final String OWM_WEATHER_ICON = "icon";
    // The temperature description are children of the "main" object
    public static final String OWM_MAIN = "main";
    public static final String OWM_MIN_TEMP = "temp_min";
    public static final String OWM_MAX_TEMP = "temp_max";
    public static final String OWM_PRESSURE = "pressure";
    public static final String OWM_HUMIDITY = "humidity";

    public static String getUrlByPlace(String place) {
        return ROOT_URL + WEATHER_BY_PLACE_ENDPOINT_URL + place;
    }

    public static String getImageUrlByName(String imageName) {
        return ROOT_URL + IMAGE_ENDPOINT_URL + imageName + IMG_EXT;
    }
}
