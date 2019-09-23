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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ListPricesFragment extends Fragment {

    private String ean;
    private String cents;
    private String selectedStore;
    private TextView pricesTextView;
    private StoreManager storeManager;


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

        pricesTextView = (TextView) getView().findViewById(R.id.pricesTextView);
        new ListPricesFragment.HerokuPostTask().execute(ean, cents, selectedStore);

        this.storeManager = new StoreManager();
        try {
            InputStream istream = getContext().getAssets().open("stores.osm");
            storeManager.fetchStores(istream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            pricesTextView.setText("Tuotteen "+ ean + " hinnat:\n");
            try {
                JSONArray json = new JSONArray(response);

                for (int i = 0; i < json.length(); i++) {
                    JSONObject priceObject = json.getJSONObject(i);
                    Store s = storeManager.getStore(priceObject.getString("storeId"));
                    if (s!= null && s.getName() != null) {
                        pricesTextView.append("\n" + s.getName());
                    } else {
                        pricesTextView.append("\nTuntematon kauppa");
                    }
                    double cents = priceObject.getInt("cents") / 100.0;
                    String formattedPrice = String.format("%.02f", cents);
                    pricesTextView.append("\nHinta: " + formattedPrice + "€\n");
                }
                pricesTextView.setMovementMethod(new ScrollingMovementMethod());
            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
