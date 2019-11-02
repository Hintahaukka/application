package hifian.hintahaukka.Service;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public abstract class HttpGetTask extends AsyncTask<String, String, String> {
    private String urlString;
    private boolean isMocked = false;

    /**
     * Sets the URL of the server
     * @param urlString The URL
     */
    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public void setMocked() {
        this.isMocked = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // To be implemented in GUI
        // Set urlString here
    }

    @Override
    protected String doInBackground(String... params) {

        String response = "";
        if (isMocked) {
            return "";
        }
        try {
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

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
        // To be implemented in GUI
        // What to do with response
    }

}