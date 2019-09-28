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
    Double lat;
    Double lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             lat = extras.getDouble("lat");
             lon = extras.getDouble("lon");
        }
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

    public GpsActivity getGpsActivity() {
        return this.gpsActivity;
    }

}
