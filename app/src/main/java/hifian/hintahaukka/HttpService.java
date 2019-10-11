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
    private String getResponse;

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

    public String getGetResponse() {
        return this.getResponse;
    }

    //TODO: Consider using a HashMap instead?
    /**
     * Sends a post request to the server, appending the query with given parameters.
     * Both arrays must be of the same length. The parameter names are matched with
     * values in corresponding index.
     * @param parameterNames A String array containing the query parameter names.
     * @param parameters A String array containing the query parameter values.
     */
    public void sendPostRequest(String[] parameterNames, String[] parameters) {

        if (parameterNames.length == parameters.length) {
            this.paramNames = parameterNames;
            postResponse = null;
            new PostTask().execute(parameters);
        }
    }

    /**
     * Sends a get request to the base url.
     */
    public void sendGetRequest() {
        new GetTask().execute("");
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

    /**
     * Executes the HTTP GET request in a separate thread.
     */
    public class GetTask extends AsyncTask<String, String, String> {


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

                int responseCode=urlConnection.getResponseCode();
                System.out.println(responseCode);

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
            getResponse = response;
        }

    }
}
