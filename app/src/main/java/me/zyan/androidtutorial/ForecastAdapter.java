package me.zyan.androidtutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.zyan.androidtutorial.details.TempSettingsManager;
import me.zyan.androidtutorial.util.ForecastUtil;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<WeeklyForecastModel> modelList = new ArrayList<>();


    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_forecast, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ViewHolder holder, int position) {
        holder.bind(modelList.get(position));

        //           Add Click Listener to each ItemView
        holder.itemView.setOnClickListener(view -> {
//            Toast.makeText(holder.itemView.getContext(),
//                    String.format(Locale.ENGLISH, "Forecast: %4.2f, %s", modelList.get(position).getTemp(), modelList.get(position).getDescription()),
//                    Toast.LENGTH_SHORT).show();

//            Go to Details Activity
            showForecastDetails(modelList.get(position));
            
        });
    }

    private void showForecastDetails(WeeklyForecastModel model) {
        MainActivity.getInstance().ifPresent(mainActivity -> mainActivity.navigateToForecastDetails(model));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void setModelList(List<WeeklyForecastModel> modelList) {
        // remove all the items first
        this.modelList.clear();
        this.modelList.addAll(modelList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tempTextView;
        private TextView decsTextView;
        private TextView dateTextView;
        private ImageView itemImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tempTextView = itemView.findViewById(R.id.tempTextView);
            decsTextView = itemView.findViewById(R.id.descTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
        }

        public void bind(WeeklyForecastModel model) {

            tempTextView.setText(ForecastUtil.formatTempValue(model.getTemp(), new TempSettingsManager(itemView.getContext()).getSettings()));
            decsTextView.setText(model.getDescription());
            dateTextView.setText( ForecastUtil.formatUnixTimeStamp(model.getDateTime(), model.getTimeZone()) );

            //                Load weather icons using Glide
            Glide
                    .with(itemView)
                    .load(BuildConfig.IMAGE_URL + model.getIcon() + "@2x.png")
                    .fitCenter()
                    .placeholder(R.drawable.cloud_icon)
                    .into(itemImageView);
        }

    }
}
