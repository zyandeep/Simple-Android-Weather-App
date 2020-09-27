package me.zyan.androidtutorial.details;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.zyan.androidtutorial.MainActivity;

class MyViewModelData {
    private double temp;
    private String desc;
    private String imgURL;

    public MyViewModelData(double temp, String desc, String imgURL) {
        this.temp = temp;
        this.desc = desc;
        this.imgURL = imgURL;
    }

    public double getTemp() {
        return temp;
    }

    public String getDesc() {
        return desc;
    }

    public String getImgURL() {
        return imgURL;
    }
}


public class MyViewModel extends ViewModel {

    public MyViewModel() {
//        Log.d(MainActivity.TAG, "ViewModel created");
    }

    private MutableLiveData<MyViewModelData> state = new MutableLiveData<>();

//    Load data from Repository
    public void setState(ForecastDetailsFragmentArgs args) {

        Log.d(MainActivity.TAG, "ViewModel data updated");

        this.state.setValue(new MyViewModelData(
                args.getTemp(),
                args.getDesc(),
                args.getIcon()
        ));
    }

//    Publish data to UI Controller
    public LiveData<MyViewModelData> getState() {
        return state;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

//        Log.d(MainActivity.TAG, "ViewModel destroyed");
    }
}
