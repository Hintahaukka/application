package hifian.hintahaukka;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class EnterPriceFragment extends Fragment {

    private String selectedStore;
    private String scanResult;

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

        TextView eanField = (TextView) getView().findViewById(R.id.eanField);
        eanField.setText("Viivakoodi: " + scanResult);

        Button sendPriceButton = getView().findViewById(R.id.sendPriceBtn);
        sendPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView enterEuros = (TextView) getView().findViewById(R.id.enterEuros);
                TextView enterCents = (TextView) getView().findViewById(R.id.enterCents);
                String cents = turnEnteredPriceToCents(enterEuros.getText().toString(),
                        enterCents.getText().toString());

                Navigation.findNavController(getView()).navigate(
                        EnterPriceFragmentDirections.actionEnterPriceFragmentToListPricesFragment(selectedStore, scanResult, cents));

            }
        });
    }

    private String turnEnteredPriceToCents(String euros, String cents) {
        int eurosAsInt = 0;
        if(!euros.isEmpty()) {
            eurosAsInt = Integer.parseInt(euros);
        }

        int centsasInt = 0;
        if(!cents.isEmpty()) {
            centsasInt = Integer.parseInt(cents);
        }

        int result = eurosAsInt * 100 + centsasInt;
        return String.valueOf(result);
    }
}
