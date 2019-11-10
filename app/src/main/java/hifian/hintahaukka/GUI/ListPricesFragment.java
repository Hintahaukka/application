package hifian.hintahaukka.GUI;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sccomponents.widgets.ScArc;
import com.sccomponents.widgets.ScGauge;
import com.sccomponents.widgets.ScSeekBar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import hifian.hintahaukka.Service.ListPricesUtils;
import hifian.hintahaukka.Service.PriceListItem;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.StoreManager;
import hifian.hintahaukka.Domain.Store;


public class ListPricesFragment extends Fragment {

    private String ean;
    private String productName;
    private String cents;
    private String selectedStore;
    private PriceListItem[] priceList;
    private TextView averagePriceField;
    private TextView myPriceField;
    private TextView productField;
    private TextView otherPricesText;
    private StoreManager storeManager;
    private static final int NUMBER_OF_PRICES_TO_RETURN = 10;
    private boolean test;

    private boolean isRunningInTestEnvironment;
    private double averagePrice;
    private int differencePercentage;
    public ListPricesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListPricesFragmentArgs args = ListPricesFragmentArgs.fromBundle(getArguments());
        ean = args.getScanResult();
        productName = args.getProductName();
        selectedStore = args.getSelectedStore();
        cents = args.getCents();
        priceList = args.getPriceList();
        test = args.getTest();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.checkIfIsRunningInTestEnvironment();
        createStoreManager();

        //Showing the product info
        productField = (TextView) getView().findViewById(R.id.productField);
        productField.setText(productName);

        //Showing the store
        TextView storeField = (TextView) getView().findViewById(R.id.storeField);
        Store s = storeManager.getStore(selectedStore);
        if (s!= null && s.getName() != null) {
            storeField.setText(s.getName());
        } else {
            storeField.setText("Tuntematon kauppa");
        }
        double myPrice = Integer.parseInt(this.cents) / 100.0;
        String formattedPrice = String.format("%.02f", myPrice);
        myPriceField.append("\nHinta: " + formattedPrice + "€\n");

        //Showing the product info
        productField = (TextView) getView().findViewById(R.id.productField);
        productField.setText(productName);

        //Showing other prices
        otherPricesText = (TextView) getView().findViewById(R.id.otherPricesText);

        //Showing average price and difference to average price
        averagePriceField = (TextView) getView().findViewById(R.id.averagePriceField);
        this.handlePricelist();
        averagePriceField.setText("Keskihinta: " + averagePrice + "€");

        // Create the price gauge
        final ScSeekBar priceGauge = (ScSeekBar) getView().findViewById(R.id.priceGauge);
        assert priceGauge != null;

        priceGauge.setStrokesCap(Paint.Cap.SQUARE);
        priceGauge.setValue(differencePercentage, -50, 50);

        priceGauge.getBaseArc().setFillingColors(ScArc.FillingColors.GRADIENT);
        priceGauge.getBaseArc().setStrokeColors(
                Color.parseColor("#55B20C"),
                Color.parseColor("#FDE401"),
                Color.parseColor("#EA3A3C")
        );

        TextView percentageText = (TextView) getView().findViewById(R.id.percentageTextField);
        assert percentageText != null;

        // Normally, the user may move the pointer by touch.
        // So we need to disable that by resetting the value immediately if user tries to change it.
        priceGauge.setOnEventListener(new ScGauge.OnEventListener() {
            @Override
            public void onValueChange(float degrees) {
                priceGauge.setValue(differencePercentage, -50, 50);
            }
        });
        if (differencePercentage >= 0) {
            percentageText.setText(differencePercentage + "%\nkeskihintaa kalliimpi");
        } else {
            percentageText.setText((0 - differencePercentage) + "%\nkeskihintaa halvempi");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_prices, container, false);
    }


    public void handlePricelist() {
        otherPricesText.setText("Muut hinnat:\n");
        Store selected = storeManager.getStore(selectedStore);
        Arrays.sort(priceList, new ListPricesFragment.PriceListItemDistanceComparator(selected.getLat(), selected.getLon()));
        if(priceList.length > NUMBER_OF_PRICES_TO_RETURN) priceList = Arrays.copyOf(priceList, NUMBER_OF_PRICES_TO_RETURN);

        // Create the list view
        final ListView listView;
        try {
            listView = (ListView) getView().findViewById(R.id.priceListView);
        } catch (NullPointerException e) {
            return;
        }
        final List<String> priceStrings = new ArrayList<>();

        // Add price items to the list view
        for (PriceListItem item : priceList) {

            Store s = storeManager.getStore(item.getStoreId());

            if (item.getStoreId().equals(selectedStore)) {
                continue;
            }
            String priceToString = "";
            if (s!= null && s.getName() != null) {
                priceToString += s.getName();
            } else {
                priceToString += "Tuntematon kauppa";
            }
            String date = item.getTimestamp();
            priceToString += ("\n"+ date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4));
            double cents = item.getCents() / 100.0;
            String formattedPrice = String.format("%.02f", cents);
            priceToString += ("\nHinta: " + formattedPrice + "€");
            priceStrings.add(priceToString);
        }
        if (priceStrings.size() == 0) {
            priceStrings.add("Ei muita hintoja");
        }
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this.getContext(),R.layout.textview_list_prices,priceStrings);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // What happens if user clicks a price item
                // Currently nothing
            }
        });

        double myPrice = Integer.parseInt(this.cents) / 100.0;
        averagePrice = ListPricesUtils.getAveragePrice(priceList ,myPrice);
        differencePercentage = ListPricesUtils.getDifferenceToAveragePriceInPercentages(myPrice, averagePrice);
    }

    /**
     * Sorts PriceListItems into ascending order according to their distances to the given reference location.
     */
    class PriceListItemDistanceComparator implements Comparator<PriceListItem> {
        private double lat;
        private double lon;
        /**
         * @param lat The latitude of the reference location.
         * @param lon The longitude of the reference location.
         */
        public PriceListItemDistanceComparator(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
        public int compare(PriceListItem p1, PriceListItem p2) {
            // Extract stores from PriceListItems.
            Store s1 = storeManager.getStore(p1.getStoreId());
            Store s2 = storeManager.getStore(p2.getStoreId());

            if(s1 != null && s2 == null) {
                return -1;
            } else if(s1 == null && s2 != null) {
                return 1;
            } else if(s1 == null && s2 == null) {
                return 0;
            }

            //Store s1 distance to reference location.
            double distanceS1 = Math.hypot(s1.getLat() - lat, s1.getLon() - lon);
            //Store s2 distance to reference location.
            double distanceS2 = Math.hypot(s2.getLat() - lat, s2.getLon() - lon);

            if(distanceS1 < distanceS2) {
                return -1;
            } else if(distanceS1 > distanceS2) {
                return 1;
            } else {
                return 0;
            }
        }
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
