package hifian.hintahaukka.Service;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Sends a HTTP post request to the server.
 * The URL and the parameter names must be defined in the onPreExecute method of the overriding task.
 */
public abstract class HttpPostTask extends AsyncTask<String, String, String> {

    private String urlString;
    private String[] paramNames;
    private boolean isMocked = false;

    /**
     * Sets the URL of the server
     * @param urlString The URL
     */
    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    /**
     * Sets the parameter names of the post request.
     * Parameter names must be in the same order than the parameters given when the task is executed.
     * @param paramNames An array containing the parameter names.
     */
    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    /**
     * Setting the HttpPostTask mocked, if the task is needed in tests.
     * In that case no data is sent to the server and the task returns only mock response.
     */
    public void setMocked() {
        this.isMocked = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // To be implemented in GUI
        // Set urlString and paramNames here
    }

    @Override
    protected String doInBackground(String... params) {

        String response = "";

        if (isMocked) {
            return createMockResponse(params);
        }

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
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        // To be implemented in GUI
        // What to do with response
    }

    private String createMockResponse(String... params) {
        if (paramNames.length == 1 && paramNames[0] == "ean" && params[0].equals("scanResultWithProductName")) {
            return "{\"ean\":\"1\",\"name\": \"Omena\",\"prices\":[{\"cents\":110,\"storeId\":\"1\",\"timestamp\":\"2019-10-07 19:48:56.9918\"},{\"cents\":120,\"storeId\":\"2\",\"timestamp\":\"2019-10-07 19:48:57.356073\"}]}";
        } else if (paramNames.length == 1 && paramNames[0] == "ean" && params[0].equals("scanResultWithoutProductName")) {
            return "{\"ean\":\"1\",\"name\": \"\",\"prices\":[{\"cents\":110,\"storeId\":\"1\",\"timestamp\":\"2019-10-07 19:48:56.9918\"},{\"cents\":120,\"storeId\":\"2\",\"timestamp\":\"2019-10-07 19:48:57.356073\"}]}";
        } else if(paramNames.length == 1 && paramNames[0] == "ean"){
            return "{\"ean\":\"1\",\"name\": \"Omena\",\"prices\":[{\"cents\":110,\"storeId\":\"1\",\"timestamp\":\"2019-10-07 19:48:56.9918\"},{\"cents\":120,\"storeId\":\"2\",\"timestamp\":\"2019-10-07 19:48:57.356073\"}]}";
        } else if (paramNames.length == 4 && paramNames[0] == "ean" && paramNames[1] == "cents" && paramNames[2] == "storeId" && paramNames[3] == "id") {
            return "30:10";
        } else {
            return "";
        }
    }
}
