package me.zyan.androidtutorial.details;

import android.content.Context;


public class TempSettingsManager {
    private Context context;

    public TempSettingsManager(Context context) {
        this.context = context;
    }

    public void saveSettings(TempDisplayUnits unit) {
        this.context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .edit()
                .putString("KEY_TEMP_UNIT", unit.name())
                .apply();
    }

    public TempDisplayUnits getSettings() {
        return TempDisplayUnits.valueOf(
                this.context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                        .getString("KEY_TEMP_UNIT", TempDisplayUnits.CELSIUS.name())
        );
    }
}
