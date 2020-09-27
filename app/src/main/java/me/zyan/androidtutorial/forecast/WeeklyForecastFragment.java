package me.zyan.androidtutorial.forecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.zyan.androidtutorial.ForecastAdapter;
import me.zyan.androidtutorial.MainActivity;
import me.zyan.androidtutorial.R;

public class WeeklyForecastFragment extends Fragment {

    public WeeklyForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false);

        //        RecycleView Settings
        RecyclerView recyclerView = view.findViewById(R.id.forecastRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ForecastAdapter adapter = new ForecastAdapter();
        recyclerView.setAdapter(adapter);

//        Empty State view
        TextView emptyTextView = view.findViewById(R.id.empty_state_textView2);
        ProgressBar progressBar = view.findViewById(R.id.progressBar2);


        MainActivity.getInstance().ifPresent(mainActivity -> {
            //        Register LiveData Observer to Listen for the Forecast data changes

            //            Daily forecast model list updated,
            //            So, update the UI
            mainActivity.repository.loadWeeklyForecastData().observe(getViewLifecycleOwner(), weeklyForecastModels -> {

                //                Display RecyclerView, hide progressbar
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

//                Update RecyclerView Adapter data
                adapter.setModelList(weeklyForecastModels);

            });

            //            Listen for Location data changes
            mainActivity.locationRepository.getSavedLocation().observe(getViewLifecycleOwner(), zipcode -> {

                if (! zipcode.getZipcode().isEmpty()) {
                    //    Hide empty state view

                    emptyTextView.setVisibility(View.GONE);

//                    Display ProgressBar
                    progressBar.setVisibility(View.VISIBLE);

                    mainActivity.repository.updateWeeklyForecastData(zipcode.getZipcode());
                }
                else {
//                    Display empty state view
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    emptyTextView.setVisibility(View.VISIBLE);

                }


            });


        });


//        FAB Configuration

        view.findViewById(R.id.fab).setOnClickListener(fab -> {

//            Display LocationEntry Fragment

            NavHostFragment.findNavController(WeeklyForecastFragment.this).navigate(R.id.action_weeklyForecastFragment_to_locationEntryFragment);

        });

        return view;
    }
}