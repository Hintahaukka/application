package hifian.hintahaukka;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StoreListFragment extends Fragment {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.checkIfIsRunningInTestEnvironment();
        createList();
    }


    private void createList() {
        this.createStoreManager();
        this.getCoordinates();
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
                //selectedStore = storeList.get(i).getName();
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
     * Fragment gets the coordinates from the MainActivity.
     * In tests zero coordinates are used instead.
     */
    private void getCoordinates() {
        if (isRunningInTestEnvironment) {
            this.lat = 0.0;
            this.lon = 0.0;
        } else {
            this.lat = ((MainActivity)getActivity()).getLat();
            this.lon = ((MainActivity)getActivity()).getLon();
        }
    }

    /**
     * Sets the isRunningInTestEnvironment variable true if this fragment has been launched in an android test.
     * Method calls the Main Activity, which causes a ClassCastException in test environment.
     */
    private void checkIfIsRunningInTestEnvironment() {
        try {
            this.isRunningInTestEnvironment = ((MainActivity)getActivity()).isDisabled();
        } catch (ClassCastException e) {
            this.isRunningInTestEnvironment = true;
        }
    }
}
