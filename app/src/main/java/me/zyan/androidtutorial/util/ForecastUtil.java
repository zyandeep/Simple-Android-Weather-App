package me.zyan.androidtutorial.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import me.zyan.androidtutorial.details.TempDisplayUnits;

public class ForecastUtil {
    public static String formatTempValue(double temp, TempDisplayUnits unit) {

//        Log.d(MainActivity.TAG, "formatTempValue: " + unit);

        switch (unit) {
            case FAHRENHEIT:
//                1.8(K - 273) + 32.

                return String.format(Locale.ENGLISH, "%4.2f°F", (temp - 273) * 1.8 + 32);
            case CELSIUS:

//                K - 273.15
                return String.format(Locale.ENGLISH, "%4.2f°C", temp - 273.15);
            default:
                return "0.00°C";
        }
    }

    public static String formatUnixTimeStamp(long timeStamp, String timeZone) {

        /*
        * Unix timestamp => Instant => LocalDateTime w/ TimeZone => Format it.
        * */

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.of(timeZone));
        return  dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    }
}
