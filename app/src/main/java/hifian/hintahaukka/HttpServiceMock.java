package hifian.hintahaukka;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpServiceMock extends HttpService {

    private String urlString;
    private String[] paramNames;
    private String postResponse;
    private String getResponse;

    /**
     * Creates a mock HttpService to be used in tests
     * @param urlString The base url as string
     */
    public HttpServiceMock(String urlString) {
        super(urlString);
        this.urlString = urlString;
    }

    @Override
    public String getPostResponse() {
        return this.postResponse;
    }

    @Override
    public String getGetResponse() {
        return this.getResponse;
    }

    @Override
    public void sendPostRequest(String[] parameterNames, String[] parameters) {
        // Example response for product info
        if (parameterNames.length == 1 && parameterNames[0] == "ean") {
            this.postResponse = "{\"ean\":\"1\",\"name\": \"Omena\",\"prices\":[{\"cents\":110,\"storeId\":\"1\",\"timestamp\":\"2019-10-07 19:48:56.9918\"},{\"cents\":120,\"storeId\":\"2\",\"timestamp\":\"2019-10-07 19:48:57.356073\"}]}";
        } else {
            this.postResponse = "";
        }
    }

    @Override
    public void sendGetRequest() {
        // Do nothing here?
        this.getResponse = "";
    }

}
