package hifian.hintahaukka;


import android.graphics.Color;

import android.location.Location;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private String selectedStore = "Unknown store";
    private StoreManager storeManager;
    private GpsActivity gpsActivity;
    private double lat;
    private double lon;
    private boolean test;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createSpinner();
/*
        CheckBox databaseCheckBox = (CheckBox) getView().findViewById(R.id.checkbox_test_database);
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    test = true;
                } else {
                    test = false;
                }
            }
        };
        databaseCheckBox.setOnCheckedChangeListener(listener);
*/
        Button scanBarcodeButton = getView().findViewById(R.id.button_scan_barcode);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox databaseCheckBox = (CheckBox) getView().findViewById(R.id.checkbox_test_database);
                if (databaseCheckBox.isChecked()) {
                    test = true;
                } else {
                    test = false;
                }
                Navigation.findNavController(getView()).navigate(
                        HomeFragmentDirections.actionHomeFragmentToBarcodeScannerFragment(selectedStore, test));
            }
        });
    }


    /**
     * Creates the dropdown menu.
     */
    private void createSpinner() {
        final Spinner spinner = (Spinner) getView().findViewById(R.id.storeSpinner);
        this.storeManager = ((MainActivity)getActivity()).getStoreManager();
        this.lat = ((MainActivity)getActivity()).getLat();
        this.lon = ((MainActivity)getActivity()).getLon();
        //final List<Store> storeList = storeManager.listNearestStores(this.lat, this.lon);
        final List<Store> storeList = storeManager.listNearestStores(lat, lon);
        List<String> storeNames = new ArrayList<>();
        for (Store s : storeList) {
            if (s.getName() != null) {
                storeNames.add(s.getName());
            } else {
                storeNames.add("Tuntematon kauppa");
            }

        }

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.getContext(),android.R.layout.simple_spinner_dropdown_item,storeNames){

            /**
             * Defines which items in the dropdown menu are selectable.
             * If first item is used as hint, make this method return false for position 0.
             * @param position The position of the item (Starting from 0).
             * @return True if the item is enabled, false otherwise.
             */
            public boolean isEnabled(int position){
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                // Defines the color of the item in this position.
                // If first item is used as hint, set color gray for position 0.
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        // What to do when an item in dropdown is selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Store selected = storeList.get(position);
                selectedStore = selected.getStoreId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
