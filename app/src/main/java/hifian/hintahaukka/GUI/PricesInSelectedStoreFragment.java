package hifian.hintahaukka.GUI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.InputStream;
import java.util.Arrays;

import hifian.hintahaukka.Domain.ParcelableHashMap;
import hifian.hintahaukka.Domain.PriceListItem;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.StoreManager;

public class PricesInSelectedStoreFragment extends Fragment {

    private PriceListItem[] pricesInSelectedStore;
    private StoreManager storeManager;
    private boolean isRunningInTestEnvironment;
    public ParcelableHashMap<String, String> eanWithNames;

    public PricesInSelectedStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.checkIfIsRunningInTestEnvironment();
        createStoreManager();
        PricesInSelectedStoreFragmentArgs args = PricesInSelectedStoreFragmentArgs.fromBundle(getArguments());
        pricesInSelectedStore = args.getPricesInSelectedStore();
        eanWithNames = args.getEanWithNames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compare_shopping_carts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createPriceList();
    }

    public void createPriceList() {
        // TODO: Handle empty list!

        ProductListAdapter adapter = new ProductListAdapter(
                this.getContext(), R.layout.fragment_compare_shopping_carts, Arrays.asList(pricesInSelectedStore), storeManager, eanWithNames);

        final ListView listView = getView().findViewById(R.id.storePriceListView);
        listView.setAdapter(adapter);
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
}
