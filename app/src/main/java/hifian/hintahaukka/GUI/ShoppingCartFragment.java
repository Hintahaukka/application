package hifian.hintahaukka.GUI;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hifian.hintahaukka.Database.Product;
import hifian.hintahaukka.Domain.PriceListItem;
import hifian.hintahaukka.Domain.PricesInStore;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.HttpPostTask;
import hifian.hintahaukka.Service.UserManager;


public class ShoppingCartFragment extends Fragment {

    private ShoppingCartViewModel viewModel;
    private List<Product> productList;
    private int cartSize;
    private Button compareShoppingCartsButton;
    private PricesInStore[] shoppingCartPrices;


    public ShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView shoppingCartList = getActivity().findViewById(R.id.shopping_cart_list);
        ShoppingCartListAdapter adapter = new ShoppingCartListAdapter(getContext());
        shoppingCartList.setAdapter(adapter);
        shoppingCartList.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(getActivity()).get(ShoppingCartViewModel.class);

        viewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProducts(products);
                cartSize = products.size();
                productList = products;
                if (cartSize == 0) {
                    compareShoppingCartsButton.setVisibility(View.INVISIBLE);
                } else {
                    compareShoppingCartsButton.setVisibility(View.VISIBLE);
                }
            }
        });

        UserManager userManager = new UserManager(getActivity());

        compareShoppingCartsButton = getView().findViewById(R.id.button_compare_shopping_cart_prices);

        compareShoppingCartsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compareShoppingCartsButton.setEnabled(false);
                String userId = userManager.getUserId();
                String[] parameters = new String[cartSize + 1];
                parameters[0] = userId;
                for (int i = 0; i < cartSize; i ++) {
                    parameters[i + 1] = productList.get(i).getEan();
                }
                new ShoppingCartPricesTask().execute(parameters);
            }
        });
    }

    /**
     * Sends shopping cart eans to the server and receives prices in stores.
     */
    private class ShoppingCartPricesTask extends HttpPostTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.setUrlString("https://hintahaukka.herokuapp.com/test/getPricesForManyProducts");
            String[] paramNames = new String[cartSize + 1];
            paramNames[0] = "id";
            for (int i = 1; i < paramNames.length; i ++) {
                paramNames[i] = "ean" + i;
            }
            this.setParamNames(paramNames);
            /*
            if (isRunningInTestEnvironment) {
                this.setMocked();
            }
            */
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            parseShoppingCartPricesInfo(response);
            moveToCompareShoppingCartsFragment();
        }
    }

    public void moveToCompareShoppingCartsFragment() {
        Navigation.findNavController(getView()).navigate(
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToCompareShoppingCartsFragment(shoppingCartPrices));
    }

    public void parseShoppingCartPricesInfo(String response) {
        if (response == null || response == "") {
            // Do something
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("pricesOfStores");
                int l = jsonArray.length();
                PricesInStore[] pricesInStores = new PricesInStore[l];

                for (int i = 0; i < l; i++) {
                    JSONObject storeObject = jsonArray.getJSONObject(i);
                    String storeId = storeObject.getString("googleStoreId");
                    int centsTotal = storeObject.getInt("storeCentsTotal");

                    JSONArray pricesJsonArray = storeObject.getJSONArray("pricesInStore");
                    PriceListItem[] prices = new PriceListItem[pricesJsonArray.length()];
                    for (int j = 0; j < pricesJsonArray.length(); j ++) {
                        JSONObject priceObject = pricesJsonArray.getJSONObject(j);
                        String ean = priceObject.getString("ean");
                        int cents = priceObject.getInt("cents");
                        String timestamp = priceObject.getString("timestamp");
                        PriceListItem priceItem = new PriceListItem(cents, storeId, timestamp, ean);
                        prices[j] = priceItem;
                    }

                    pricesInStores[i] = new PricesInStore(storeId, centsTotal, prices);
                }
                shoppingCartPrices = pricesInStores;

                int pointsTotal = jsonObject.getInt("pointsTotal");
                int pointsUnused = jsonObject.getInt("pointsUnused");
                // TODO: Update points (not yet implemented because backend sends wrong points)
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        // If the responce contains no price info, create a fake, so that argument won't be null
        if (shoppingCartPrices == null) {
            shoppingCartPrices = new PricesInStore[1];
            shoppingCartPrices[0] = new PricesInStore("", 0, new PriceListItem[1]);
        }
    }
}
