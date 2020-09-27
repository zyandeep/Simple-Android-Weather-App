package me.zyan.androidtutorial.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import me.zyan.androidtutorial.BuildConfig;
import me.zyan.androidtutorial.MainActivity;
import me.zyan.androidtutorial.R;
import me.zyan.androidtutorial.databinding.FragmentForecastDetailsBinding;
import me.zyan.androidtutorial.util.ForecastUtil;

public class ForecastDetailsFragment extends Fragment {

    //    ViewBinding reference
    private FragmentForecastDetailsBinding binding;

    //    ViewModel
    private MyViewModel viewModel;


    public ForecastDetailsFragment() {
//        Required Empty Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentForecastDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ViewModel is SCOPED/TIED to the Activity
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        ForecastDetailsFragmentArgs args = ForecastDetailsFragmentArgs.fromBundle(getArguments());

//        Update ViewModel data
        viewModel.setState(args);


//        Listen for LiveData changes
        viewModel.getState().observe(getViewLifecycleOwner(), myViewModelData -> {

            Log.d(MainActivity.TAG, "LiveData changes received from ViewModel");

            binding.tempNumberTV.setText(ForecastUtil.formatTempValue(myViewModelData.getTemp(), new TempSettingsManager(getContext()).getSettings()));
            binding.tempDescTV.setText(myViewModelData.getDesc());

            Glide
                    .with(ForecastDetailsFragment.this)
                    .load(BuildConfig.IMAGE_URL + myViewModelData.getImgURL() + "@2x.png")
                    .fitCenter()
                    .placeholder(R.drawable.cloud_icon)
                    .into(binding.myImageView);


        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}