package hifian.hintahaukka.GUI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Arrays;

import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.PriceListItem;
import hifian.hintahaukka.Service.StoreManager;


public class ListPricesFragment2 extends Fragment {

    private String ean;
    private String productName;
    private PriceListItem[] priceList;
    private TextView productNameField;
    private StoreManager storeManager;
    private static final int NUMBER_OF_PRICES_TO_RETURN = 10;
    private boolean test;
    private boolean isRunningInTestEnvironment;



    public ListPricesFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListPricesFragment2Args args = ListPricesFragment2Args.fromBundle(getArguments());
        ean = args.getScanResult();
        productName = args.getProductName();
        priceList = args.getPriceList();
        test = true;

        createStoreManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_prices_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Showing the product name
        productNameField = (TextView) getView().findViewById(R.id.averagePriceField);
        productNameField.setText(productName);

        createPriceList();

    }

    public void createPriceList() {
        // TODO: Handle empty list!


        //Arrays.sort(priceList, new ListPricesFragment2.PriceListItemDistanceComparator(selected.getLat(), selected.getLon()));
        if(priceList.length > NUMBER_OF_PRICES_TO_RETURN) priceList = Arrays.copyOf(priceList, NUMBER_OF_PRICES_TO_RETURN);

        PriceListAdapter adapter = new PriceListAdapter(
                this.getContext(), R.layout.list_prices_item, Arrays.asList(priceList), storeManager);

        final ListView listView = getView().findViewById(R.id.priceListView);
        listView.setAdapter(adapter);
    }

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
}
