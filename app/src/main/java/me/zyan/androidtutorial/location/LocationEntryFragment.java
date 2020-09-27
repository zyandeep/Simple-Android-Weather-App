package me.zyan.androidtutorial.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Optional;

import me.zyan.androidtutorial.Location;
import me.zyan.androidtutorial.MainActivity;
import me.zyan.androidtutorial.R;


public class LocationEntryFragment extends Fragment {

    private static final int PEMISSION_REQUEST_CODE = 100;

    private EditText mEditText;
    private FusedLocationProviderClient fusedLocationClient;


    //    Required empty public constructor
    public LocationEntryFragment() {
    }


    private void getDeviceLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

//        Get Device's Last Known Location
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                String latLon = location.getLatitude() + "," + location.getLongitude();
                mEditText.setText(latLon);
            } else {
                Toast.makeText(getContext(), "Can't Access Location. Make Sure GPS is On.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void saveLocation(String latLon) {
        MainActivity.getInstance().ifPresent(mainActivity -> {

            mainActivity.locationRepository.saveLocation(new Location.Zipcode(latLon));
            mainActivity.showNextFragment(Optional.of(latLon));

        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_entry, container, false);

//        Access/Update ui elements

        mEditText = view.findViewById(R.id.zipcodeEditText);
        Button button = view.findViewById(R.id.button);
        Button gpsButton = view.findViewById(R.id.gps_button);


        button.setOnClickListener(btn -> {
            String latLon = mEditText.getText().toString();

            if (latLon.isEmpty() || !latLon.contains(",")) {
                Toast.makeText(getContext(), R.string.invalid_zipcode, Toast.LENGTH_SHORT).show();
                return;
            }

//            Save Device Location
            saveLocation(latLon);

        });


//        Use Device Location button Handler
        gpsButton.setOnClickListener(btn -> {

//            Check whether LOCATION ACCESS PERMISSION is GRANTED or NOT

            if (ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

//                Permission already granted
                // Access Device location
                getDeviceLocation();

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // In an educational UI, explain to the user why your app requires the permission

                Toast.makeText(getContext(), "Need Location Access for Weather Forecast!", Toast.LENGTH_SHORT).show();

            } else {
                // You can directly ask for the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PEMISSION_REQUEST_CODE);
            }


        });


        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PEMISSION_REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission is granted.
//                Access Device Location
                getDeviceLocation();

            } else {
                // Permission is denied.

                Toast.makeText(getContext(), "Need Location Access for Weather Forecast!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}