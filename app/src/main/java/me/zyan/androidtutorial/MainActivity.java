package me.zyan.androidtutorial;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Optional;

import me.zyan.androidtutorial.details.TempDisplayUnits;
import me.zyan.androidtutorial.details.TempSettingsManager;
import me.zyan.androidtutorial.forecast.DailyForecastFragmentDirections;
import me.zyan.androidtutorial.forecast.WeeklyForecastFragmentDirections;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MY_APP";

    private TempSettingsManager settingsManager;
    private static MainActivity instance;
    private BottomNavigationView bottomNav;

//    Data Repositories
    public LocationRepository locationRepository;
    public final ForecastRepository repository = new ForecastRepository();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constraint_layout_demo);

        settingsManager = new TempSettingsManager(this);

        MainActivity.instance = this;
        locationRepository = new LocationRepository(MainActivity.this);


//        Connect ToolBar with NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

//        Multiple top-level unrelated screens
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.forecastListFragment, R.id.weeklyForecastFragment).build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

//        Connect BottomNavBar with NavController
        this.bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);

/*

//        Add LocationEntryFragment Dynamically
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new LocationEntryFragment())
                .commit();*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tempUnit_menuItem) {
            //                Display AlertDialog
            displayTempUnitDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayTempUnitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Choose Temperature Unit")
                .setMessage("Which unit to use for temperature display?")
                .setCancelable(true)
                .setPositiveButton("C°", (dialogInterface, i) -> {

                    settingsManager.saveSettings(TempDisplayUnits.CELSIUS);

                })
                .setNeutralButton("F°", (dialogInterface, i) -> {

                    settingsManager.saveSettings(TempDisplayUnits.FAHRENHEIT);

                })
                .setOnDismissListener(dialogInterface -> {
                    Toast.makeText(this, "Settings will take affect after app restart.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }


    public void showNextFragment(Optional<String> zipCode) {
        Fragment nextFragment;

        if (zipCode.isPresent()) {
            // Show ForecastListFragment
//            nextFragment = new ForecastListFragment(this.repository);

//            NAVIGATE TO DESTINATIONS(OTHER FRAGMENTS) USING NavController

            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
                    .navigateUp();

        } else {
//            Show LocationEntryFragment
//            nextFragment = new LocationEntryFragment();

            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
                    .navigate(R.id.action_forecastListFragment_to_locationEntryFragment);

        }

       /* getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, nextFragment)
                .commit();*/
    }


    public void navigateToForecastDetails(WeeklyForecastModel model) {

        switch (this.bottomNav.getSelectedItemId()) {
            case R.id.forecastListFragment:

                DailyForecastFragmentDirections.ActionForecastListFragmentToForecastDetailsFragment action =
                        DailyForecastFragmentDirections.actionForecastListFragmentToForecastDetailsFragment();

                action.setTemp((float) model.getTemp());
                action.setDesc(model.getDescription());


                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
                        .navigate(action);

                break;

            case R.id.weeklyForecastFragment:

                WeeklyForecastFragmentDirections.ActionWeeklyForecastFragmentToForecastDetailsFragment action2 =
                        WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment();

                action2.setTemp((float) model.getTemp());
                action2.setDesc(model.getDescription());
                action2.setIcon(model.getIcon());

                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
                        .navigate(action2);


                break;
        }
    }

    public static Optional<MainActivity> getInstance() {
        return Optional.ofNullable(MainActivity.instance);
    }
}