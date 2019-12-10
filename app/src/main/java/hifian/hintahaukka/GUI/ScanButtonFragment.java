package hifian.hintahaukka.GUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.InputStream;
import java.util.List;

import hifian.hintahaukka.Domain.User;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.HttpPostTask;
import hifian.hintahaukka.Service.LeaderboardUtils;
import hifian.hintahaukka.Service.StoreManager;
import hifian.hintahaukka.Domain.Store;

public class ScanButtonFragment extends Fragment {
    private boolean test;
    private String selectedStore;
    private StoreManager storeManager;
    private boolean isRunningInTestEnvironment;

    public ScanButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScanButtonFragmentArgs args = ScanButtonFragmentArgs.fromBundle(getArguments());
        selectedStore = args.getSelectedStore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_button, container, false);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_scan_button, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.checkIfIsRunningInTestEnvironment();
        this.createStoreManager();
        TextView showStore = getView().findViewById(R.id.showStore);
        Store store = storeManager.getStore(selectedStore);
        showStore.setText(getString(R.string.store_leaderboard_title) + " " + store.getName());
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
                        ScanButtonFragmentDirections.actionScanButtonFragmentToBarcodeScannerFragment(selectedStore, test));
                }
        });

        CheckBox databaseCheckBox = getView().findViewById(R.id.checkbox_test_database);
        databaseCheckBox.setOnClickListener(checkBoxView -> {
            test = databaseCheckBox.isChecked();
            new GetStoreLeaderboardTask().execute(new String[]{selectedStore});
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CheckBox databaseCheckBox = getView().findViewById(R.id.checkbox_test_database);
        test = databaseCheckBox.isChecked();
        new GetStoreLeaderboardTask().execute(new String[]{selectedStore});
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
            this.isRunningInTestEnvironment = ((MainActivity)getActivity()).isDisabled();
        } catch (ClassCastException e) {
            this.isRunningInTestEnvironment = true;
        }
    }

    private class GetStoreLeaderboardTask extends HttpPostTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(test) {
                this.setUrlString("https://hintahaukka.herokuapp.com/test/getLeaderboardForStore");
            } else {
                this.setUrlString("https://hintahaukka.herokuapp.com/getLeaderboardForStore");
            }

            this.setParamNames(new String[]{"storeId"});
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            List<User> leaderboard = LeaderboardUtils.parseLeaderboardFromJSONRespose(response);
            LeaderboardListAdapter adapter = new LeaderboardListAdapter(getContext(), R.layout.leaderboard_item, leaderboard);
            ListView listView = getView().findViewById(R.id.list_store_leaderboard);
            listView.setAdapter(adapter);
        }
    }

}
