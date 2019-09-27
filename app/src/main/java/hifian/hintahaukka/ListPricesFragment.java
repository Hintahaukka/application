package hifian.hintahaukka;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;


public class ListPricesFragment extends Fragment {

    private String ean;
    private String cents;
    private String selectedStore;
    private TextView pricesTextView;
    private StoreManager storeManager;
    private String herokuResponse;
    private static final int NUMBER_OF_PRICES_TO_RETURN = 10;


    public ListPricesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListPricesFragmentArgs args = ListPricesFragmentArgs.fromBundle(getArguments());
        ean = args.getScanResult();
        selectedStore = args.getSelectedStore();
        cents = args.getCents();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.storeManager = ((MainActivity)getActivity()).getStoreManager();

        pricesTextView = (TextView) getView().findViewById(R.id.pricesTextView);
        new ListPricesFragment.HerokuPostTask().execute(ean, cents, selectedStore);

        while( herokuResponse == null) {

        }
        this.handleResponse(herokuResponse);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_prices, container, false);
    }

    public class HerokuPostTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String urlString = "https://hintahaukka.herokuapp.com/";
            String response = "";

            try {
                URL url = new URL(urlString);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("ean", params[0])
                        .appendQueryParameter("cents", params[1])
                        .appendQueryParameter("storeId", params[2]);
                String query = builder.build().getEncodedQuery();

                urlConnection.connect();

                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());

                out.writeBytes(query);
                out.flush();
                out.close();


                int responseCode=urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                urlConnection.disconnect();
                herokuResponse = response;
                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                herokuResponse = "";
                return "";
            }
        }

        @Override
        protected void onPostExecute(String response) {
        }

    }

    public void handleResponse(String response) {
        pricesTextView.setText("Tuotteen "+ ean + " hinnat:\n");

        Store selected = storeManager.getStore(selectedStore);

        PriceListItem[] priceList = new Gson().fromJson(response, PriceListItem[].class);
        Arrays.sort(priceList, new PriceListItemDistanceComparator(selected.getLat(), selected.getLon()));
        if(priceList.length > NUMBER_OF_PRICES_TO_RETURN) priceList = Arrays.copyOf(priceList, NUMBER_OF_PRICES_TO_RETURN);

        for (PriceListItem item : priceList) {
            Store s = storeManager.getStore(item.getStoreId());
            if (s!= null && s.getName() != null) {
                pricesTextView.append("\n" + s.getName());
            } else {
                pricesTextView.append("\nTuntematon kauppa");
            }
            String date = item.getTimestamp();
            pricesTextView.append("\n"+ date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4));
            double cents = item.getCents() / 100.0;
            String formattedPrice = String.format("%.02f", cents);
            pricesTextView.append("\nHinta: " + formattedPrice + "€\n");
        }
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
