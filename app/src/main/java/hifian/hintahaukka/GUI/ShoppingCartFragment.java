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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hifian.hintahaukka.Database.Product;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.HttpPostTask;
import hifian.hintahaukka.Service.PriceListItem;
import hifian.hintahaukka.Service.RecyclerViewClickListener;


public class ShoppingCartFragment extends Fragment {

    private ShoppingCartViewModel viewModel;
    private boolean test;
    private String productName;
    private PriceListItem[] prices;
    private String productEan;
    private String storeId;
    private boolean isRunningInTestEnvironment;

    public ShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        test = true;
        super.onViewCreated(view, savedInstanceState);
        RecyclerView shoppingCartList = getActivity().findViewById(R.id.shopping_cart_list);
        ShoppingCartListAdapter adapter = new ShoppingCartListAdapter(getContext());
        shoppingCartList.setAdapter(adapter);
        shoppingCartList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnProductClickListener(new RecyclerViewClickListener() {
            @Override
            public void onProductClick(Product product) {
                //Toast.makeText(getContext(), product.getName() + "", Toast.LENGTH_LONG).show();
                /*Navigation.findNavController(getView()).navigate(
                        ShoppingCartFragmentDirections.actionShoppingCartFragmentToListPricesFragment
                                (null, product.getEan(), null, product.getName(), new PriceListItem[1], true)
                );*/

                productEan = product.getEan();
                productName = product.getName();
                new ProductInfoTask().execute(product.getEan());

            }
        });

        viewModel = new ViewModelProvider(getActivity()).get(ShoppingCartViewModel.class);
        viewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProducts(products);
            }
        });
    }


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
            handleResponse(response);
            Navigation.findNavController(getView()).navigate(
                    ShoppingCartFragmentDirections.actionShoppingCartFragmentToListPricesFragment22(productName, productEan, prices)
            );
        }
    }

    public void handleResponse(String response) {

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

        // If the responce contains no price list, create a fake, so that argument won't be null
        // Id must be that of selectedStore, so that it won't be listed in the next fragment
        if (prices == null) {
            prices = new PriceListItem[1];
            prices[0] = new PriceListItem(0, "", "timestamp");
        }

    }

}