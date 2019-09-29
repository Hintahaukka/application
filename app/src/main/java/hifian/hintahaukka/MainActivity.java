package hifian.hintahaukka;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private StoreManager storeManager;
    private GpsActivity gpsActivity;
    double lat;
    double lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             this.lat = extras.getDouble("lat");
             this.lon = extras.getDouble("lon");
        }

        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lon", lon);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);

        Toast.makeText(this,"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_LONG).show();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);


        this.storeManager = new StoreManager();
        try {
            InputStream istream = this.getAssets().open("stores.osm");
            storeManager.fetchStores(istream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    public StoreManager getStoreManager() {
        return this.storeManager;
    }

    public void sendLocationDataToHomefragment() {
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lon", lon);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

}
