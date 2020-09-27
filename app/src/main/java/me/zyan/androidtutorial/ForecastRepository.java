package me.zyan.androidtutorial;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ForecastRepository {

    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private final String TAG_DAILY = "daily";
    private final String TAG_WEEKLY = "weekly";

    private MutableLiveData<DailyForecastModel> dailyForecastData = new MutableLiveData<>();
    private MutableLiveData<List<WeeklyForecastModel>> weeklyForecastData = new MutableLiveData<>();

//    private OpenWeatherMap api = new OpenWeatherMap();

    public LiveData<DailyForecastModel> loadDailyForecastData() {
        return dailyForecastData;
    }

    public LiveData<List<WeeklyForecastModel>> loadWeeklyForecastData() {
        return weeklyForecastData;
    }


    public void updateDailyForecastData(String zipcode) {

        //        http://api.openweathermap.org/data/2.5/{path}
//        zipcode : //    It's lat, lon value for now

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lat", zipcode.split(",")[0]);
        queryParams.put("lon", zipcode.split(",")[1]);
        queryParams.put("appid", BuildConfig.API_KEY);


        AndroidNetworking.get(BASE_URL + "{path}")
                .addPathParameter("path", "weather")
                .addQueryParameter(queryParams)
                .setTag(TAG_DAILY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            //                        Create Forecast Model
//                            Temp is in Kelvin

                            DailyForecastModel model = new DailyForecastModel(
                                    response.getJSONObject("main").getDouble("temp"),
                                    response.getJSONArray("weather").getJSONObject(0).getString("description"),
                                    new Location.LatLon(response.getJSONObject("coord").getDouble("lat"),
                                            response.getJSONObject("coord").getDouble("lon")),

                                    new Location.Place(response.getJSONObject("sys").getString("country"), response.getString("name")),
                                    response.getJSONArray("weather").getJSONObject(0).getString("icon")

                            );

                            ForecastRepository.this.dailyForecastData.setValue(model);


                        } catch (Exception ex) {
                            Log.e(MainActivity.TAG, ex.getMessage() );
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        MainActivity.getInstance().ifPresent(mainActivity -> {

                            String error = String.format(Locale.ENGLISH, "%s : %d", anError.getErrorDetail(), anError.getErrorCode());

                            Toast.makeText(mainActivity, error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });


    }


    public void updateWeeklyForecastData(String zipcode) {
        //   zipcode : It's lat, lon value for now

//        https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude={part}&appid={YOUR API KEY}

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lat", zipcode.split(",")[0]);
        queryParams.put("lon", zipcode.split(",")[1]);
        queryParams.put("exclude", "current,hourly,minutely");
        queryParams.put("appid", BuildConfig.API_KEY);


        AndroidNetworking.get(BASE_URL + "{path}")
                .addPathParameter("path", "onecall")
                .addQueryParameter(queryParams)
                .setTag(TAG_WEEKLY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray jsonArray = response.getJSONArray("daily");
                            List<WeeklyForecastModel> list = new ArrayList<>(jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(
                                        new WeeklyForecastModel(
                                                jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("max"),
                                                jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"),
                                                new Location.LatLon(response.getDouble("lat"),
                                                        response.getDouble("lon")),
                                                jsonArray.getJSONObject(i).getLong("dt"),
                                                response.getString("timezone"),
                                                jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
                                        )
                                );
                            }

                            ForecastRepository.this.weeklyForecastData.setValue(list);


                        } catch (Exception ex) {
                            Log.e(MainActivity.TAG, ex.getMessage() );
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
//                        Log.d(MainActivity.TAG, anError.getErrorDetail() + " : " + anError.getErrorCode());

                        MainActivity.getInstance().ifPresent(mainActivity -> {

                            String error = String.format(Locale.ENGLISH, "%s : %d", anError.getErrorDetail(), anError.getErrorCode());

                            Toast.makeText(mainActivity, error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });


    }

}
