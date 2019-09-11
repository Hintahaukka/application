package hifian.hintahaukka;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
;
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

    Map<String, List<String>> prices = new HashMap<>();
    String ean;
    String price;

    private TextView pricesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_prices);

        if (getIntent().hasExtra("scanResult") && getIntent().hasExtra("price")) {
            ean = getIntent().getExtras().getString("scanResult").toString();
            price = getIntent().getExtras().getString("price").toString();
            pricesTextView = (TextView) findViewById(R.id.pricesTextView);
            pricesTextView.setText("Viivakoodi: " + ean);
            new HerokuPostTask().execute(ean, price, "101");
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
            pricesTextView.setText(response);
        }

    }

}
