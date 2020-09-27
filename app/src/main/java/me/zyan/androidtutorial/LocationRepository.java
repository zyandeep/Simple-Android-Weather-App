package me.zyan.androidtutorial;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class LocationRepository {
    private SharedPreferences preferences;

    private MutableLiveData<Location.Zipcode> savedLocation = new MutableLiveData<>();

    public LocationRepository(Context context) {
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);


//        SharePref value may change (in disk?). So we need a listener.
/*        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {

            if ("KEY_ZIPCODE".equals(key)) {
                //                    Update live data
                this.savedLocation.setValue(new Location.Zipcode(this.preferences.getString(key, "785001")));
            }

        });*/
    }


    public LiveData<Location.Zipcode> getSavedLocation() {
        this.savedLocation.setValue(new Location.Zipcode(this.preferences.getString("KEY_ZIPCODE", "")));

        return savedLocation;
    }

//    For now, location format is zipcode
    public void saveLocation(Location.Zipcode zipcode) {
//        Update LiveData
        savedLocation.setValue(zipcode);

//        Save in sharedPref
        preferences.edit().putString("KEY_ZIPCODE", zipcode.getZipcode()).apply();
    }
}
