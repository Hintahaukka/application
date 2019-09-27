package hifian.hintahaukka;


import android.graphics.Color;

import android.location.Location;
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
        getLocation();
        createSpinner();
        Button scanBarcodeButton = getView().findViewById(R.id.button_scan_barcode);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(
                        HomeFragmentDirections.actionHomeFragmentToBarcodeScannerFragment(selectedStore));
            }
        });
    }

    private void getLocation() {
        this.gpsActivity = ((MainActivity)getActivity()).getGpsActivity();
        Location l = gpsActivity.getLocation();
        if( l == null){
            Toast.makeText(this.getContext(),"GPS unable to get Value",Toast.LENGTH_LONG).show();
        }else {
            this.lat = l.getLatitude();
            this.lon = l.getLongitude();
            Toast.makeText(this.getContext(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_LONG).show();
        }
    }

    private void createSpinner() {
        final Spinner spinner = (Spinner) getView().findViewById(R.id.storeSpinner);
        this.storeManager = ((MainActivity)getActivity()).getStoreManager();

        // TODO: Dropdown menu should show store names, but send the store id as value
       // List<String> storeList = storeManager.listNearestStores(this.lat, this.lon);
        final List<Store> storeList2 = storeManager.listNearestStores2(this.lat, this.lon);
        List<String> storeNames = new ArrayList<>();
        for (Store s : storeList2) {
            storeNames.add(s.getName());
        }

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.getContext(),android.R.layout.simple_spinner_dropdown_item,storeNames){

            public boolean isEnabled(int position){
                return true;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                Store selected = storeList2.get(position);
                selectedStore = selected.getStoreId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
