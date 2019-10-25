package hifian.hintahaukka.GUI;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import hifian.hintahaukka.GUI.MainActivity;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.StoreManager;
import hifian.hintahaukka.Domain.Store;
import hifian.hintahaukka.GUI.StoreListFragmentDirections;

public class StoreListFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationClient;
    private String selectedStore = "Unknown store";
    private StoreManager storeManager;
    private double lat;
    private double lon;

    private boolean isRunningInTestEnvironment;

    public StoreListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.checkIfIsRunningInTestEnvironment();
        updateLocationAndStoreList();

        getView().findViewById(R.id.button_update_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocationAndStoreList();
            }
        });
    }

    private void createList() {
        this.createStoreManager();
        final List<Store> storeList = storeManager.listNearestStores(lat, lon);
        final ListView listView = (ListView) getView().findViewById(R.id.listView);
        final List<String> storeNames = new ArrayList<>();
        for (Store s : storeList) {
            if (s.getName() != null) {
                storeNames.add(s.getName());
            } else {
                storeNames.add("Tuntematon kauppa");
            }
        }

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,storeNames);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Store selected = storeList.get(i);
                selectedStore = selected.getStoreId();
                Navigation.findNavController(getView()).navigate(
                        StoreListFragmentDirections.actionStoreListFragmentToScanButtonFragment(selectedStore)
                );
            }
        });
    }

    /**
     * Fragment uses the StoreManager of the Activity.
     * In tests the fragment creates its own StoreManager.
     */
    private void createStoreManager() {
        if (isRunningInTestEnvironment) {
            this.storeManager = new StoreManager();
            try {
                InputStream istream = this.getActivity().getAssets().open("stores.osm");
                storeManager.fetchStores(istream);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.storeManager = ((MainActivity)getActivity()).getStoreManager();
        }
    }

    /**
     * Sets the isRunningInTestEnvironment variable true if this fragment has been launched in an android test.
     * Method calls the Main Activity, which causes a ClassCastException in test environment.
     */
    private void checkIfIsRunningInTestEnvironment() {
        try {
            this.isRunningInTestEnvironment = ((MainActivity) getActivity()).isDisabled();
        } catch (ClassCastException e) {
            this.isRunningInTestEnvironment = true;
        }
    }

    /**
     * Finds the location of the user and updates the store list with new location information.
     * In tests location is set to 0,0.
     */
    private void updateLocationAndStoreList() {
        if(isRunningInTestEnvironment) {
            lat = 0;
            lon = 0;
            createList();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Location found
                            lat = location.getLatitude();
                            lon = location.getLongitude();

                            // Update store list
                            createList();
                        } else {
                            // User has probably turned off location, ask user to turn location on
                            Snackbar.make(getView(), R.string.text_ask_to_turn_location_on, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
