package me.zyan.androidtutorial;

import java.util.Locale;

public class WeeklyForecastModel {

//    Max temp
    private double temp;
    private String description;
    private Location.LatLon latLon;
    private long dateTime;
    private String timeZone;
    private String icon;

    public WeeklyForecastModel(double temp, String description, Location.LatLon latLon, long dateTime, String timeZone, String icon) {
        this.temp = temp;
        this.description = description;
        this.latLon = latLon;
        this.dateTime = dateTime;
        this.timeZone = timeZone;
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public String getDescription() {
        return (description.charAt(0) + "").toUpperCase(Locale.ENGLISH) + this.description.substring(1);
    }

    public Location.LatLon getLatLon() {
        return latLon;
    }

    public long getDateTime() {
        return dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getIcon() {
        return this.icon;
    }

    @Override
    public String toString() {
        return "WeeklyForecastModel{" +
                "temp=" + temp +
                ", description='" + description + '\'' +
                ", latLon=" + latLon +
                ", dateTime=" + dateTime +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }
}
