package hifian.hintahaukka.GUI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.InputStream;

import hifian.hintahaukka.Service.HttpPostTask;
import hifian.hintahaukka.Domain.PriceListItem;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.EnterPriceUtils;
import hifian.hintahaukka.Service.StoreManager;
import hifian.hintahaukka.Domain.Store;
import hifian.hintahaukka.Service.UserManager;

public class EnterPriceFragment extends Fragment {

    private String selectedStore;
    private String scanResult;
    private boolean test;
    private String cents;

    private String productName;
    private TextView nameTextView;
    private Button sendPriceButton;
    private TextView enterEuros;
    private TextView enterCents;
    private PriceListItem[] prices;
    private StoreManager storeManager;
    private String[] parameters;
    private TextView enterProductNameField;
    private Button sendProductNameButton;

    private boolean isRunningInTestEnvironment;

    public EnterPriceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        EnterPriceFragmentArgs args = EnterPriceFragmentArgs.fromBundle(getArguments());

        selectedStore = args.getSelectedStore();
        scanResult = args.getScanResult();
        test = args.getTest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_price, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.checkIfIsRunningInTestEnvironment();

        nameTextView = (TextView) getView().findViewById(R.id.nameField);
        nameTextView.setText("Haetaan tuotenimi...\n");
        enterProductNameField = (TextView) getView().findViewById(R.id.enterProductNameField);
        enterProductNameField.setVisibility(View.INVISIBLE);
        sendProductNameButton = getView().findViewById(R.id.sendProdNameBtn);
        sendProductNameButton.setVisibility(View.INVISIBLE);
        enterEuros = (TextView) getView().findViewById(R.id.enterEuros);
        enterCents = (TextView) getView().findViewById(R.id.enterCents);

        // Send button is disabled before all necessary data has been received
        sendPriceButton = getView().findViewById(R.id.sendPriceBtn);
        sendPriceButton.setEnabled(false);

        // Get product info from scan result
        new ProductInfoTask().execute(new String[]{scanResult});

        // Show store and ean
        this.createStoreManager();
        TextView storeField = (TextView) getView().findViewById(R.id.storeField);
        Store store = storeManager.getStore(selectedStore);
        if (store != null && store.getName() != null) {
            storeField.setText("Kauppa: " + store.getName());
        } else {
            storeField.setText("Tuntematon kauppa");
        }
        TextView eanField = (TextView) getView().findViewById(R.id.productField);
        eanField.setText("Viivakoodi: " + scanResult);

        sendPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disable send button so it won't be pressed twice if there is delay
                sendPriceButton.setEnabled(false);
                // Send price
                cents = EnterPriceUtils.turnEnteredPriceToCents(
                        enterEuros.getText().toString(),
                        enterCents.getText().toString());
                String userId = getUserId();
                parameters = new String[]{scanResult, cents, selectedStore, userId};
                new SendPriceTask().execute(parameters);
            }
        });

        // Moves the focus from euros to cents if pressing '.' ',' or enter keys
        enterEuros.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable e) {
                String input = e.toString();
                if(input.length() > 0) {
                    char c = input.charAt(input.length() - 1);
                    if (c == '.' || c == ',' || c == '\n') {
                        String newInput = input.substring(0, input.indexOf(c));
                        enterEuros.setText(newInput);
                        enterCents.requestFocus();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing needed here...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing needed here...
            }
        });

    }

    /**
     * Parses the product name and an array of prices based on the JSON response.
     * @param response The JSON containing the product and price info
     */
    public void parseProductInfo(String response) {
        if (response == null || response == "") {
            nameTextView.setText("Tuotetietojen hakeminen epäonnistui\n");
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                productName = jsonObject.getString("name");

                JSONArray jsonArray = jsonObject.getJSONArray("prices");
                int l = jsonArray.length();
                prices = new PriceListItem[l];

                for (int i = 0; i < l; i++) {
                    JSONObject j = jsonArray.getJSONObject(i);
                    prices[i] = new PriceListItem(j.getInt("cents"), j.getString("storeId"),
                            j.getString("timestamp"), scanResult);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        // If the response contains no product name, the field and button for product name input are shown.
        if (productName == null || productName.equals("")) {
            productName = "";

            enterProductNameField.setVisibility(View.VISIBLE);
            sendProductNameButton.setVisibility(View.VISIBLE);

            sendProductNameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userInputProductName = enterProductNameField.getText().toString();
                    if(2 <= userInputProductName.length() && userInputProductName.length() <= 150) {
                        new SendProductNameTask().execute(scanResult, getUserId(), userInputProductName);
                        sendProductNameButton.setEnabled(false);
                        enterProductNameField.setEnabled(false);
                        enterProductNameField.setText("Kiitos!");
                        productName = userInputProductName;
                    }
                }
            });
        }
        // If the response contains no price list, create a fake, so that argument won't be null
        // Id must be that of selectedStore, so that it won't be listed in the next fragment
        if (prices == null) {
            prices = new PriceListItem[1];
            prices[0] = new PriceListItem(0, selectedStore, "timestamp", scanResult);
        }
        // Showing of the product name
        nameTextView.setText(productName);

        // we may now proceed to next fragment when ready
        sendPriceButton.setEnabled(true);
    }



    /**
     * Fragment uses the StoreManager of the Activity.
     * In tests the fragment creates its own StoreManager.
     */
    private void createStoreManager() {
        if (isRunningInTestEnvironment) {
            this.storeManager = new StoreManager();
            try {
                InputStream istream = this.getActivity().getAssets().open("stores.osm");
                storeManager.fetchStores(istream);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.storeManager = ((MainActivity)getActivity()).getStoreManager();
        }
    }

    /**
     * Sets the isRunningInTestEnvironment variable true if this fragment has been launched in an android test.
     * Method calls the Main Activity, which causes a ClassCastException in test environment.
     */
    private void checkIfIsRunningInTestEnvironment() {
        try {
            this.isRunningInTestEnvironment = ((MainActivity)getActivity()).isDisabled();
        } catch (ClassCastException e) {
            this.isRunningInTestEnvironment = true;
        }
    }

    public PriceListItem[] getPrices() {
        return this.prices;
    }

    /**
     * Sends the ean code to the server and receives product info.
     */
    private class ProductInfoTask extends HttpPostTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.setUrlString("https://hintahaukka.herokuapp.com/getInfoAndPrices");
            if (test) {
                this.setUrlString("https://hintahaukka.herokuapp.com/test/getInfoAndPrices");
            }
            this.setParamNames(new String[]{"ean"});
            if (isRunningInTestEnvironment) {
                this.setMocked();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            parseProductInfo(response);
        }
    }

    /**
     * Sends the new price to the server and receives points. Then moves to the next fragment.
     */
    private class SendPriceTask extends HttpPostTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.setUrlString("https://hintahaukka.herokuapp.com/addPrice");
            if (test) {
                this.setUrlString("https://hintahaukka.herokuapp.com/test/addPrice");
            }
            this.setParamNames(new String[]{"ean", "cents", "storeId", "id"});
            if (isRunningInTestEnvironment) {
                this.setMocked();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null && response != "") {
                try {
                    String[] points = response.split(":");
                    int pointsTotal = Integer.parseInt(points[0]);
                    int pointsUnused = Integer.parseInt(points[1]);
                    updatePoints(pointsTotal, pointsUnused);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            moveToNextFragment();
        }
    }
  
    /**
     * Sends the product name to the server.
     */
    private class SendProductNameTask extends HttpPostTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.setUrlString("https://hintahaukka.herokuapp.com/updateProductName");
            if (test) {
                this.setUrlString("https://hintahaukka.herokuapp.com/test/updateProductName");
            }
            this.setParamNames(new String[]{"ean", "id", "productName"});
            if (isRunningInTestEnvironment) {
                this.setMocked();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.equals("success")) {
                addPoints(5);
            }
        }
    }

    /**
     * Fetches the user id from memory.
     * @return user id
     */
    private String getUserId() {
        if (isRunningInTestEnvironment) {
            return "1234567890123456789012345678901";
        }
        UserManager userManager = new UserManager(this.getActivity());
        return userManager.getUserId();
    }

    /**
     * Updates points to memory.
     * @param pointsTotal total points
     * @param pointsUnused unused points
     */
    private void updatePoints(int pointsTotal, int pointsUnused) {
        UserManager userManager = new UserManager(this.getActivity());
        userManager.updatePoints(pointsTotal, pointsUnused);
    }

    /**
     * Adds points and updates them to memory
     * @param pointsToAdd number of points to be added to total and unused points
     */
    private void addPoints(int pointsToAdd) {
        UserManager userManager = new UserManager(this.getActivity());
        int total = userManager.getPointsTotal();
        int unused = userManager.getPointsUnused();
        updatePoints(total + pointsToAdd, unused + pointsToAdd);
    }

    /**
     * Navigates to list prices fragment.
     */
    private void moveToNextFragment() {
        Navigation.findNavController(getView()).navigate(
                EnterPriceFragmentDirections.actionEnterPriceFragmentToListPricesFragment(
                        selectedStore, scanResult, cents, productName, prices, test ));
    }
}

