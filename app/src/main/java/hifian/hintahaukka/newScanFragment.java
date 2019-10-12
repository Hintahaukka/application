package hifian.hintahaukka;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class newScanFragment extends Fragment {
    private boolean test;
    private String selectedStore;
    public newScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newScanFragmentArgs args = newScanFragmentArgs.fromBundle(getArguments());
        selectedStore = args.getSelectedStore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_scan_fragment, container, false);
        getActivity().setTitle(selectedStore);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.new_scan_fragment, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView showStore = getView().findViewById(R.id.showStore);
        showStore.setText("Valittu kauppa: " + selectedStore);
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
                        newScanFragmentDirections.actionNewScanFragmentToBarcodeScannerFragment(selectedStore, test));


                }
        });
    }

}
