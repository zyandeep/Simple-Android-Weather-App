package me.zyan.androidtutorial;

import java.util.Locale;

public abstract class Location {

    public static class Zipcode {
        private String zipcode;

        public Zipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getZipcode() {
            return zipcode;
        }
    }

//    One more inner class for Location of type LAT-LON

    public static class LatLon {
        private double lat;
        private double lon;

        public LatLon(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        @Override
        public String toString() {
            return String.format(Locale.ENGLISH, "%.2f, %.2f", this.lat, this.lon);
        }
    }


    //    Country and Place Name
    public static class Place {

        private String country;
        private String place;

        public Place(String country, String place) {
            this.country = country;
            this.place = place;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        @Override
        public String toString() {
            return String.format(Locale.ENGLISH, "%s, %s", this.place, this.country);
        }
    }
}
