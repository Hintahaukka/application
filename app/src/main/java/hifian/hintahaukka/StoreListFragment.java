package hifian.hintahaukka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

    public StoreListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

    private void createStoreManager() {
        try {
            this.storeManager = ((MainActivity)getActivity()).getStoreManager();
        } catch (ClassCastException e) {
            this.storeManager = new StoreManager();
            try {
                InputStream istream = this.getActivity().getAssets().open("stores.osm");
                storeManager.fetchStores(istream);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getCoordinates() {
        try {
            this.lat = ((MainActivity)getActivity()).getLat();
            this.lon = ((MainActivity)getActivity()).getLon();
        } catch (ClassCastException e) {
            this.lat = 0.0;
            this.lon = 0.0;
        }
    }
}
