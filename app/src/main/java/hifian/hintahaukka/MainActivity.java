package hifian.hintahaukka;

import android.Manifest;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.gpsActivity = new GpsActivity(getApplicationContext());
        try {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } catch (Exception ex) {
            ex.printStackTrace();
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
