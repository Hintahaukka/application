package hifian.hintahaukka.GUI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;

import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.StoreManager;
import hifian.hintahaukka.Service.UserManager;


public class MainActivity extends AppCompatActivity {

    private StoreManager storeManager;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure top-level destinations that show a menu icon instead of up button on top left corner
        // and set drawer layout for navigation drawer
        appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.storeListFragment, R.id.shoppingCartFragment, R.id.leaderboardFragment)
                        .setDrawerLayout(findViewById(R.id.drawerLayout))
                        .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationView navView = findViewById(R.id.navView);
        NavigationUI.setupWithNavController(navView, navController);
        new UserManager(this).updatePointsToUIView();


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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration);
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
