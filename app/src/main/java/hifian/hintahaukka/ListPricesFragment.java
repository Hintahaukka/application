package hifian.hintahaukka;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;


public class ListPricesFragment extends Fragment {

    private String ean;
    private String productName;
    private String cents;
    private String selectedStore;
    private PriceListItem[] priceList;
    private TextView myPriceField;
    private TextView productField;
    private TextView pricesTextView;
    private TextView otherPricesText;
    private StoreManager storeManager;
    private static final int NUMBER_OF_PRICES_TO_RETURN = 10;
    private boolean test;




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
        // array of PriceListItems from database via enterPriceFragment
        priceList = args.getPriceList();
        test = args.getTest();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.storeManager = ((MainActivity)getActivity()).getStoreManager();

        //Showing the store and price added by user
        myPriceField = (TextView) getView().findViewById(R.id.myPriceField);
        Store s = storeManager.getStore(selectedStore);
        if (s!= null && s.getName() != null) {
            myPriceField.append(s.getName());
        } else {
            myPriceField.append("Tuntematon kauppa");
        }
        double myPrice = Integer.parseInt(this.cents) / 100.0;
        String formattedPrice = String.format("%.02f", myPrice);
        myPriceField.append("\nHinta: " + formattedPrice + "€\n");

        //Showing the product info
        productField = (TextView) getView().findViewById(R.id.productField);
        productField.setText(productName);

        //Showing other prices
        otherPricesText = (TextView) getView().findViewById(R.id.otherPricesText);
        pricesTextView = (TextView) getView().findViewById(R.id.pricesTextView);
        // added productName
        this.handlePricelist();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_prices, container, false);
    }


    public void handlePricelist() {
        // changed to handle array from enterPriceFragment
        pricesTextView.setText(productName + " " + ean + " hinnat:\n");



        otherPricesText.setText("Muut hinnat:\n");

        Store selected = storeManager.getStore(selectedStore);

        Arrays.sort(priceList, new ListPricesFragment.PriceListItemDistanceComparator(selected.getLat(), selected.getLon()));
        if(priceList.length > NUMBER_OF_PRICES_TO_RETURN) priceList = Arrays.copyOf(priceList, NUMBER_OF_PRICES_TO_RETURN);

        // current Store and price there
        Store s = storeManager.getStore(selectedStore);
        if (s!= null && s.getName() != null) {
            pricesTextView.append("\n" + s.getName());
        } else {
            pricesTextView.append("\nTuntematon kauppa");
        }
        double price = Integer.parseInt(cents) / 100.0;
        String formattedPrice = String.format("%.02f", price);
        pricesTextView.append("\nHinta: " + formattedPrice + "€\n");

        // strores and prices from array
        for (PriceListItem item : priceList) {

            s = storeManager.getStore(item.getStoreId());

            if (item.getStoreId().equals(selectedStore)) {
                continue;
            }

            if (s!= null && s.getName() != null) {
                pricesTextView.append("\n" + s.getName());
            } else {
                pricesTextView.append("\nTuntematon kauppa");
            }
            String date = item.getTimestamp();
            pricesTextView.append("\n"+ date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4));
            double cents = item.getCents() / 100.0;
            formattedPrice = String.format("%.02f", cents);
            pricesTextView.append("\nHinta: " + formattedPrice + "€\n");
        }
        // can first one be locked ??
        pricesTextView.setMovementMethod(new ScrollingMovementMethod());
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

}
