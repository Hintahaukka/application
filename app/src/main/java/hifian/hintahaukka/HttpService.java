package hifian.hintahaukka;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class HttpService {

    private String urlString;
    private String[] paramNames;
    private String postResponse;

    /**
     * Creates a HttpService that connects to the given url
     * @param urlString The base url as string
     */
    public HttpService(String urlString) {
        this.urlString = urlString;
    }

    /**
     * Returns the response of the last HTTP post query.
     * @return The response from the server or null if the post task is not yet completed.
     */
    public String getPostResponse() {
        return this.postResponse;
    }

    //TODO: Consider using a HashMap instead?
    /**
     * Sends a post request to the server, appending the query with given parameters.
     * Both arrays must be of the same length. The parameter names are matched with
     * values in corresponding index.
     * @param parameterNames A String array containing the query parameter names.
     * @param parameters A String array containing the query parameter values.
     * @return The response from the server.
     */
    public void sendPostRequest(String[] parameterNames, String[] parameters) {

        if (parameterNames.length == parameters.length) {
            this.paramNames = parameterNames;
            postResponse = null;
            new PostTask().execute(parameters);
        }
    }

    /**
     * Executes the HTTP post request in a separate thread.
     */
    public class PostTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String response = "";

            try {
                URL url = new URL(urlString);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);

                Uri.Builder builder = new Uri.Builder();

                for (int i = 0; i < paramNames.length; i ++) {
                    builder.appendQueryParameter(paramNames[i], params[i]);
                }
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
                postResponse = response;
                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                postResponse = "";
                return "";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            postResponse = response;
        }

    }

    public static String getProductNameFromApi(String barcode) throws MalformedURLException, IOException {
        String productName;
        String urlString = "https://api.barcodelookup.com/v2/products?barcode=" + barcode + "&formatted=y&key=kcz6mpkh3x2rblgh46b2cpcda9p2xy";
        URL url = new URL (urlString);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        if (request.getResponseCode() == 404) {
            return null;
        }
        
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream()));

        String inputLine;

        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        } reader.close();

        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(response.toString());
        JsonObject jObject = element.getAsJsonObject();
        JsonArray jArray = jObject.getAsJsonArray("products");
        jObject = jArray.get(0).getAsJsonObject();
        productName = jObject.get("product_name").toString();

        return productName;
    }
}
