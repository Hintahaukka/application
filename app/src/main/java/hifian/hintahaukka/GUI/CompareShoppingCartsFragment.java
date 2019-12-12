
package hifian.hintahaukka.GUI;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;


import hifian.hintahaukka.Domain.ParcelableHashMap;
import hifian.hintahaukka.Domain.PriceListItem;
import hifian.hintahaukka.Domain.PricesInStore;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.StoreManager;


public class CompareShoppingCartsFragment extends Fragment {

    private PricesInStore[] pricesInStores;
    private PriceListItem[] pricesInSelectedStore;
    private StoreManager storeManager;
    private boolean isRunningInTestEnvironment;
    private static final int NUMBER_OF_PRICES_TO_RETURN = 10;
    private PricesInStore pricesInStore;
    public ParcelableHashMap<String, String> eanWithNames;

    public CompareShoppingCartsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CompareShoppingCartsFragmentArgs args = CompareShoppingCartsFragmentArgs.fromBundle(getArguments());
        pricesInStores = args.getShoppingCartPrices();
        eanWithNames = args.getEanWithNames();
        this.checkIfIsRunningInTestEnvironment();
        createStoreManager();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createCartPriceList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compare_shopping_carts, container, false);
    }

    public void createCartPriceList() {
        if(pricesInStores.length > NUMBER_OF_PRICES_TO_RETURN) pricesInStores = Arrays.copyOf(pricesInStores, NUMBER_OF_PRICES_TO_RETURN);
        ShoppingCartPriceListAdapter adapter = new ShoppingCartPriceListAdapter(
                this.getContext(), R.layout.list_prices_item, Arrays.asList(pricesInStores), storeManager);

        final ListView listView = getView().findViewById(R.id.storePriceListView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pricesInStore = (PricesInStore) adapterView.getItemAtPosition(i);
                moveToTheNextFragment(pricesInStore);

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
            this.isRunningInTestEnvironment = ((MainActivity)getActivity()).isDisabled();
        } catch (ClassCastException e) {
            this.isRunningInTestEnvironment = true;
        }
    }

    private void moveToTheNextFragment(PricesInStore pricesInStore) {
        pricesInSelectedStore = pricesInStore.getPrices();
        Navigation.findNavController(getView()).navigate(
                CompareShoppingCartsFragmentDirections.actionCompareShoppingCartsFragmentToPricesInSelectedStoreFragment(pricesInSelectedStore, eanWithNames)
        );
    }
}
