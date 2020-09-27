package me.zyan.androidtutorial;

import java.util.Locale;

public class DailyForecastModel {
    private double temp;
    private String description;
    private Location.LatLon latLon;
    private Location.Place place;
    private String icon;

    public DailyForecastModel(double temp, String description, Location.LatLon latLon, Location.Place place, String icon) {
        this.temp = temp;
        this.description = description;
        this.latLon = latLon;
        this.place = place;
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public String getDescription() {
        return (description.charAt(0) + "").toUpperCase(Locale.ENGLISH) + this.description.substring(1);
    }

    public Location.LatLon getLatLon() {
        return this.latLon;
    }

    public Location.Place getPlace() {
        return place;
    }

    public String getIcon() {
        return this.icon;
    }

    @Override
    public String toString() {
        return "DailyForecastModel{" +
                "temp=" + temp +
                ", description='" + description + '\'' +
                ", latLon=" + latLon +
                ", place=" + place +
                '}';
    }
}
