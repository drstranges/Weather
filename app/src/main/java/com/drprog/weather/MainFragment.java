package com.drprog.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.drprog.weather.sync.ConnectionManager;
import com.drprog.weather.sync.ForecastItem;
import com.drprog.weather.sync.OpenWeatherContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements Response.ErrorListener,
        Response.Listener<JSONObject>, View.OnClickListener {

    private static final String WEATHER_REQUEST_TAG = "WEATHER_REQUEST_TAG";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");

    private TextView mViewPlace;
    private TextView mViewDay;
    private TextView mViewDate;
    private TextView mViewTemperatureHigh;
    private TextView mViewTemperatureLow;
    private TextView mViewWeatherDescription;
    private TextView mViewHumidity;
    private TextView mViewPressure;
    private TextView mViewWind;
    private NetworkImageView mViewWeatherIcon;
    private ImageButton mViewShowMap;

    private ProgressBar mProgressBar;
    private ForecastItem mForecast;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mViewPlace = (TextView) rootView.findViewById(R.id.tv_place);
        mViewDay = (TextView) rootView.findViewById(R.id.tv_day);
        mViewDate = (TextView) rootView.findViewById(R.id.tv_date);
        mViewTemperatureHigh = (TextView) rootView.findViewById(R.id.tv_high);
        mViewTemperatureLow = (TextView) rootView.findViewById(R.id.tv_low);
        mViewWeatherDescription = (TextView) rootView.findViewById(R.id.tv_description);
        mViewHumidity = (TextView) rootView.findViewById(R.id.tv_humidity);
        mViewPressure = (TextView) rootView.findViewById(R.id.tv_pressure);
        mViewWind = (TextView) rootView.findViewById(R.id.tv_wind);
        mViewWeatherIcon = (NetworkImageView) rootView.findViewById(R.id.iv_icon);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mViewShowMap = (ImageButton) rootView.findViewById(R.id.ib_show_map);
        mViewShowMap.setOnClickListener(this);
        return rootView;
    }

    public void loadWeather(String query) {
        Log.d("TAG", query);
        if (!ConnectionManager.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.error_no_internet_connection, Toast.LENGTH_SHORT)
                    .show();
        } else {
            Request request = getRequest(query);
            request.setTag(WEATHER_REQUEST_TAG);
            ConnectionManager connectionManager = ConnectionManager.getInstance(getActivity());
            connectionManager.cancelAllRequests(WEATHER_REQUEST_TAG);
            connectionManager.addToRequestQueue(request);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private Request getRequest(String place) {
        String url = OpenWeatherContract.getUrlByPlace(place);
        return new JsonObjectRequest(url, null, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(),
                       ConnectionManager.getErrorMessage(getActivity(), error),
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d("TAG", response.toString());
        mProgressBar.setVisibility(View.INVISIBLE);
        try {
            if (response.has("cod") && response.has("message")) {

                Toast.makeText(getActivity(),
                               response.getString("message"),
                               Toast.LENGTH_SHORT).show();
            }
            mForecast = new ForecastItem(response);
            refreshView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshView() {
        mViewPlace.setText(mForecast.placeName);
        mViewDay.setText(dayFormat.format(mForecast.dateTime));
        mViewDate.setText(dateFormat.format(mForecast.dateTime));
        mViewTemperatureHigh.setText(getString(R.string.format_temperature,
                                               mForecast.getTemperatureInDegree(
                                                       mForecast.highTemp)));
        mViewTemperatureLow.setText(getString(R.string.format_temperature,
                                              mForecast.getTemperatureInDegree(mForecast.lowTemp)));
        mViewWeatherDescription.setText(mForecast.description);
        mViewHumidity.setText(getString(R.string.format_humidity,
                                        mForecast.humidity));
        mViewPressure.setText(getString(R.string.format_pressure,
                                        mForecast.pressure));
        mViewWind.setText(getString(R.string.format_wind,
                                    mForecast.windSpeed, mForecast.getWindDirection()));
        mViewWeatherIcon.setImageUrl(OpenWeatherContract.getImageUrlByName(mForecast.iconName),
                                     ConnectionManager.getInstance(getActivity()).getImageLoader());
        mViewWeatherIcon.setDefaultImageResId(R.mipmap.ic_launcher);
        mViewShowMap.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_show_map) {
            showMap();
        }
    }

    private void showMap() {
        Intent intent = new Intent(getActivity(), MapsActivity.class)
                .putExtra(MapsActivity.COORD_LAT, mForecast.coordLat)
                .putExtra(MapsActivity.COORD_LON, mForecast.coordLon);
        startActivity(intent);
    }
}
