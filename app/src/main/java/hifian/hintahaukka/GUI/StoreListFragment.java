package hifian.hintahaukka.GUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

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

        //The main tool for location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        // Set up requesting location
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                //Stop tracking location
                fusedLocationClient.removeLocationUpdates(locationCallback);

                // Update location
                Location location = locationResult.getLocations().get(0);
                lat = location.getLatitude();
                lon = location.getLongitude();

                // Update store list
                createList();
            }
        };

        // Settings of location request
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        showPoints();

        getView().findViewById(R.id.button_update_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationSettings();
            }
        });
    }

    private void createList() {
        this.createStoreManager();
        final List<Store> storeList = storeManager.listNearestStores(lat, lon);

        // Prevent a bug that chrases the program: if user has asked to update the location
        // but navigates to next screen before finding location is finished, don't update the list
        final ListView listView;
        try {
            listView = (ListView) getView().findViewById(R.id.listView);
        } catch (NullPointerException e) {
            return;
        }

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
     * Finds the location of the user and updates the store list with the information.
     * In tests location is set to a particular shop in Åland islands, beloved by the
     * developing team.
     */
    private void updateLocationAndStoreList() {
        if(isRunningInTestEnvironment) {
            lat = 60.083087;
            lon = 19.942171;
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
                            // User has probably turned off location, check location settings
                            checkLocationSettings();
                        }
                    }
                });
    }

    /**
     * Checks if the user's location is on. If on, location is found and updated, if not,
     * user is asked to turn the location on.
     */
    public void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this.getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        // If location is on, request location
        task.addOnSuccessListener(this.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Snackbar.make(getView(), R.string.text_finding_stores, Snackbar.LENGTH_LONG).show();
                requestLocation();
            }
        });

        // If location is turned off, ask user to turn location on
        task.addOnFailureListener(this.getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    Snackbar.make(getView(), R.string.text_ask_to_turn_location_on, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Finds the current location if last known location is not available
     */
    private void requestLocation() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    public void showPoints() {
        if (isRunningInTestEnvironment) {
            return;
        }
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int pointsTotal = sharedPreferences.getInt(getString(R.string.key_points_total), 0);
        int pointsUnused = sharedPreferences.getInt(getString(R.string.key_points_unused), 0);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navView);
        View headerView = navigationView.getHeaderView(0);
        TextView pointsField = (TextView) headerView.findViewById(R.id.pointsField);
        SpannableString spanString = new SpannableString("Pisteet: "+ pointsTotal + "\nKäytettävissä: " + pointsUnused + "\n");
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        pointsField.setText(spanString);
    }
    
}
