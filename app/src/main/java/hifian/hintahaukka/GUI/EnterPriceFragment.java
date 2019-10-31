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

import hifian.hintahaukka.GUI.EnterPriceFragmentArgs;
import hifian.hintahaukka.GUI.EnterPriceFragmentDirections;
import hifian.hintahaukka.Service.PriceListItem;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.EnterPriceUtils;
import hifian.hintahaukka.Service.HttpService;
import hifian.hintahaukka.Service.HttpServiceMock;
import hifian.hintahaukka.Service.StoreManager;
import hifian.hintahaukka.Domain.Store;

public class EnterPriceFragment extends Fragment {

    private String selectedStore;
    private String scanResult;
    private boolean test;

    private String storeName;
    private String productName;
    private TextView nameTextView;
    private PriceListItem priceListItem;
    private PriceListItem[] prices;
    private StoreManager storeManager;
    private HttpService httpService;
    private String[] parameterNames;
    private String[] parameters;

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

        // find productName from backend to nameTextView
        String productNameUrlString = "https://hintahaukka.herokuapp.com/getInfoAndPrices";
        if (test) {
            productNameUrlString = "https://hintahaukka.herokuapp.com/test/getInfoAndPrices";
        }
        httpService = this.createHttpService(productNameUrlString);
        parameterNames = new String[]{"ean"};
        parameters = new String[]{scanResult};
        httpService.sendPostRequest(parameterNames, parameters);
        String herokuResponse = null;
        while (herokuResponse == null) {
            // TODO: Do something while the post task is running. While-loop probably not the best way to wait.
            herokuResponse = httpService.getPostResponse();
        }
        this.handleResponse(herokuResponse);

        // Show store and ean
        this.createStoreManager();
        TextView storeField = (TextView) getView().findViewById(R.id.storeField);
        Store store = storeManager.getStore(selectedStore);
        if (store != null && store.getName() != null) {
            storeField.setText("Kauppa: " + store.getName());
        } else {
            storeField.setText("Tuntematon kauppa");
        }
        TextView eanField = (TextView) getView().findViewById(R.id.eanField);
        eanField.setText("Viivakoodi: " + scanResult);

        Button sendPriceButton = getView().findViewById(R.id.sendPriceBtn);
        final TextView enterEuros = (TextView) getView().findViewById(R.id.enterEuros);
        final TextView enterCents = (TextView) getView().findViewById(R.id.enterCents);

        sendPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cents = EnterPriceUtils.turnEnteredPriceToCents(
                        enterEuros.getText().toString(),
                        enterCents.getText().toString());
                parameterNames = new String[]{"ean", "cents", "storeId"};
                parameters = new String[]{scanResult, cents, selectedStore};
                String addPriceUrlString = "https://hintahaukka.herokuapp.com/addPrice";
                if (test) {
                    addPriceUrlString = "https://hintahaukka.herokuapp.com/test/addPrice";
                }
                httpService = createHttpService(addPriceUrlString);

                httpService.sendPostRequest(parameterNames, parameters);
                Navigation.findNavController(getView()).navigate(
                        EnterPriceFragmentDirections.actionEnterPriceFragmentToListPricesFragment(
                                selectedStore, scanResult, cents, productName, prices, test ));

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
    public void handleResponse(String response) {
        if (response == null) {
            nameTextView.setText("Tuotenimen hakeminen epäonnistui\n");
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            productName = jsonObject.getString("name");

            JSONArray jsonArray = jsonObject.getJSONArray("prices");
            int l = jsonArray.length();
            prices = new PriceListItem[l];

            for (int i = 0; i < l; i++) {
                JSONObject j = jsonArray.getJSONObject(i);
                prices[i] = new PriceListItem(j.getInt("cents"), j.getString("storeId"),
                        j.getString("timestamp"));
            }
        } catch (JSONException e1) {
            e1.printStackTrace();

        }
        // If the response contains no product name, put Unknown so that argument won't be null
        if (productName == null || productName.equals("")) {
            productName = "Tuotenimeä ei saatavilla";
        }
        // If the responce contains no price list, create a fake, so that argument won't be null
        // Id must be that of selectedStore, so that it won't be listed in the next fragment
        if (prices == null) {
            prices = new PriceListItem[1];
            prices[0] = new PriceListItem(0, selectedStore, "timestamp");
        }
        // we are now have productname, lets show it
        nameTextView.setText(productName);
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

    private HttpService createHttpService(String url) {
        if (isRunningInTestEnvironment) {
            return new HttpServiceMock(url);
        } else {
            return new HttpService(url);
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

}