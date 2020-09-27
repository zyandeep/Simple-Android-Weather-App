package me.zyan.androidtutorial.forecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.Optional;

import me.zyan.androidtutorial.BuildConfig;
import me.zyan.androidtutorial.MainActivity;
import me.zyan.androidtutorial.R;
import me.zyan.androidtutorial.details.TempSettingsManager;
import me.zyan.androidtutorial.util.ForecastUtil;

public class DailyForecastFragment extends Fragment {

    public DailyForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast_list, container, false);

        TextView placeTextView = view.findViewById(R.id.placeTextView);
        TextView latLonTextView = view.findViewById(R.id.latLonTextView);
        TextView dailyTempTextView = view.findViewById(R.id.dailyTempTextView);
        TextView dailyTempDescTextView = view.findViewById(R.id.dailyTempDescTextView);
        ImageView weatherImageView = view.findViewById(R.id.weatherImageView);
        TextView emptyTextView = view.findViewById(R.id.empty_state_textView);

//        LinearLayout
        ViewGroup myLinearLayout = view.findViewById(R.id.myLinearLayout);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);




        MainActivity.getInstance().ifPresent(mainActivity -> {
            //        Register LiveData Observer to Listen for the forecast data changes

            mainActivity.repository.loadDailyForecastData().observe(getViewLifecycleOwner(), dailyForecastModel -> {
                //            Daily forecast model updated,
                //            So, update the UI

//                Display Location Data, hide progressbar
                progressBar.setVisibility(View.GONE);
                myLinearLayout.setVisibility(View.VISIBLE);

                placeTextView.setText(dailyForecastModel.getPlace().toString());
                latLonTextView.setText(dailyForecastModel.getLatLon().toString());
                dailyTempTextView.setText(ForecastUtil.formatTempValue(dailyForecastModel.getTemp(), new TempSettingsManager(getContext()).getSettings()));
                dailyTempDescTextView.setText(dailyForecastModel.getDescription());

//                Load weather icons using Glide
                Glide
                        .with(DailyForecastFragment.this)
                        .load(BuildConfig.IMAGE_URL + dailyForecastModel.getIcon() + "@2x.png")
                        .fitCenter()
                        .placeholder(R.drawable.cloud_icon)
                        .into(weatherImageView);


            });

            //            Listen for Location data changes
            mainActivity.locationRepository.getSavedLocation().observe(getViewLifecycleOwner(), zipcode -> {

                if (!zipcode.getZipcode().isEmpty()) {
//                    Hide empty state view
//                    myLinearLayout.setVisibility(View.VISIBLE);

                    emptyTextView.setVisibility(View.GONE);

//                    Display ProgressBar
                    progressBar.setVisibility(View.VISIBLE);

                    mainActivity.repository.updateDailyForecastData(zipcode.getZipcode());
                }
                else {
//                    Display empty state view
                    myLinearLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    emptyTextView.setVisibility(View.VISIBLE);

                }

            });


        });


//        FAB Configuration

        view.findViewById(R.id.fab).setOnClickListener(fab -> {

//            Display LocationEntry Fragment
            ((MainActivity) getActivity()).showNextFragment(Optional.empty());

        });

        return view;
    }
}