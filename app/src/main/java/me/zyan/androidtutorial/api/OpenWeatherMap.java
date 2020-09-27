package me.zyan.androidtutorial.api;

import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.zyan.androidtutorial.BuildConfig;
import me.zyan.androidtutorial.DailyForecastModel;
import me.zyan.androidtutorial.MainActivity;

public class OpenWeatherMap {

    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private final String TAG_DAILY = "daily";
    private final String TAG_WEEKLY = "weekly";

//    It's lat, lon value for now
    private String zipcode;


    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public DailyForecastModel getDailyForecast() {
//        http://api.openweathermap.org/data/2.5/{path}

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lat", this.zipcode.split(",")[0]);
        queryParams.put("lon", this.zipcode.split(",")[1]);
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
                        Log.d(MainActivity.TAG, response.toString());
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

        return null;
    }

    public List<DailyForecastModel> getWeeklyForecast() {
        return null;
    }
}
