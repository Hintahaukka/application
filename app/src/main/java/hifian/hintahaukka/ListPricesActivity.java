package hifian.hintahaukka;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class ListPricesActivity extends AppCompatActivity {

    String ean;
    String cents;
    String selectedStore;
    private TextView pricesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_prices);

        if (getIntent().hasExtra("scanResult") && getIntent().hasExtra("cents") && getIntent().hasExtra("selectedStore")) {
            ean = getIntent().getExtras().getString("scanResult").toString();
            cents = getIntent().getExtras().getString("cents").toString();
            selectedStore = getIntent().getExtras().getString("selectedStore");
            pricesTextView = (TextView) findViewById(R.id.pricesTextView);
            //pricesTextView.setText("Haetaan hintoja...");
            pricesTextView.setText("Haetaan hintoja...");
            new HerokuPostTask().execute(ean, cents, selectedStore);
        }
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
                    pricesTextView.append("\nKauppa: " + priceObject.getString("storeId"));

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
