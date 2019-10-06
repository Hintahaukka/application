package hifian.hintahaukka;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.Arrays;


public class EnterPriceFragment extends Fragment {

    private String selectedStore;
    private String scanResult;
    private String storeName;
    private String productName;
    private TextView nameTextView;
    private PriceListItem[] priceList;
    private StoreManager storeManager;

    public EnterPriceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EnterPriceFragmentArgs  args = EnterPriceFragmentArgs.fromBundle(getArguments());

        selectedStore = args.getSelectedStore();
        scanResult = args.getScanResult();
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
        nameTextView = (TextView) getView().findViewById(R.id.nameField);
        this.storeManager = ((MainActivity)getActivity()).getStoreManager();
        TextView storeField = (TextView) getView().findViewById(R.id.storeField);

        HttpService httpService = new HttpService("https://hintahaukka.herokuapp.com/");
        // removed cents from parameters as we haven't got it yet
        String[] parameterNames = {"ean",  "storeId"};
        String[] parameters = {scanResult,  selectedStore};
        httpService.sendPostRequest(parameterNames, parameters);
        String herokuResponse = null;
        while (herokuResponse == null) {
            // TODO: Do something while the post task is running. While-loop probably not the best way to wait.
            herokuResponse = httpService.getPostResponse();
        }
        this.handleResponse(herokuResponse);
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

                Navigation.findNavController(getView()).navigate(
                        EnterPriceFragmentDirections.actionEnterPriceFragmentToListPricesFragment(
                                selectedStore, scanResult, cents, productName, priceList ));

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

    public void handleResponse(String response) {
        nameTextView.setText("Haetaan tuotenimi...\n");

        Store selected = storeManager.getStore(selectedStore);

        priceList = new Gson().fromJson(response, PriceListItem[].class);

        // we are now have productname, lets show it
        productName = priceList[0].getProductName();
        nameTextView.setText(productName);



    }

}
