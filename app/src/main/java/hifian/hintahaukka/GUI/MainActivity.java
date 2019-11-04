package hifian.hintahaukka.GUI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.io.InputStream;

import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.StoreManager;


public class MainActivity extends AppCompatActivity {

    private StoreManager storeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    /**
     * This is a dumb method that is called by fragments for testing purposes.
     * @return
     */
    public boolean isDisabled() {
        return false;
    }

}
