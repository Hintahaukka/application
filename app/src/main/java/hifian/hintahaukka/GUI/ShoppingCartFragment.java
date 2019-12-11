package hifian.hintahaukka.GUI;


import android.os.Bundle;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import hifian.hintahaukka.Domain.PriceListItem;
import hifian.hintahaukka.Domain.PricesInStore;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Database.Product;
import hifian.hintahaukka.Service.HttpPostTask;
import hifian.hintahaukka.Service.UserManager;
import hifian.hintahaukka.Service.RecyclerViewClickListener;

public class ShoppingCartFragment extends Fragment {

    private ShoppingCartViewModel viewModel;
    private List<Product> productList;
    private int cartSize;
    private Button compareShoppingCartsButton;
    private PricesInStore[] shoppingCartPrices;
    private boolean isRunningInTestEnvironment;
    private int testMessage;
    private boolean test;
    private String productName;
    private PriceListItem[] priceList;
    private String productEan;

    public ShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        checkIfIsRunningInTestEnvironment();
        test = true;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView shoppingCartList = getActivity().findViewById(R.id.shopping_cart_list);
        ShoppingCartListAdapter adapter = new ShoppingCartListAdapter(getContext());
        shoppingCartList.setAdapter(adapter);
        shoppingCartList.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(shoppingCartList);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        shoppingCartList.addItemDecoration(divider);

        adapter.setOnProductClickListener(new RecyclerViewClickListener() {
            @Override
            public void onProductClick(Product product) {
                productEan = product.getEan();
                productName = product.getName();
                //Toast.makeText(getContext(), "ean: " + productEan, Toast.LENGTH_LONG).show();
                new ProductInfoTask().execute(productEan);
            }
        });


        viewModel = new ViewModelProvider(getActivity()).get(ShoppingCartViewModel.class);
        viewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProducts(products);
                cartSize = products.size();
                productList = products;
                if (cartSize == 0 && !isRunningInTestEnvironment) {
                    compareShoppingCartsButton.setVisibility(View.INVISIBLE);
                } else {
                    compareShoppingCartsButton.setVisibility(View.VISIBLE);
                }
            }
        });

        compareShoppingCartsButton = view.findViewById(R.id.button_compare_shopping_cart_prices);

        compareShoppingCartsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPointsUnused() < cartSize) {
                    showMessage(R.string.text_not_enough_points);
                } else {
                    compareShoppingCartsButton.setEnabled(false);
                    String userId = getUserId();
                    String[] parameters = new String[cartSize + 1];
                    parameters[0] = userId;
                    for (int i = 0; i < cartSize; i ++) {
                        parameters[i + 1] = productList.get(i).getEan();
                    }
                    new ShoppingCartPricesTask().execute(parameters);
                }
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
            parseShoppingCartPricesInfo(response);
            // If the responce contains no price info, show a message, otherwise continue to next fragment
            if (shoppingCartPrices == null) {
                showMessage(R.string.text_no_shopping_cart_prices_found);
            } else {
                moveToCompareShoppingCartsFragment();
            }
        }
    }

    public void moveToCompareShoppingCartsFragment() {
        Navigation.findNavController(getView()).navigate(
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToCompareShoppingCartsFragment(shoppingCartPrices));
    }

    /**
     * Parses the JSON response to create the shoppingCartPrices list and to update user's points.
     * @param response JSON response from the server
     */
    public void parseShoppingCartPricesInfo(String response) {
        if (response != null && !response.equals("")) {
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
                new UserManager(getContext()).updatePoints(pointsTotal, pointsUnused);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Fetches user's unused points from memory
     * @return user's unused points
     */
    public int getPointsUnused() {
        if (isRunningInTestEnvironment) {
            return 2;
        }
        return new UserManager(getActivity()).getPointsUnused();
    }

    /**
     * Fetches the user id from memory
     * @return user id
     */
    public String getUserId() {
        if (isRunningInTestEnvironment) {
            return "123567890123456789012345678901";
        }
        return new UserManager(getActivity()).getUserId();
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

    /**
     * This method is used in tests
     * @param productList Product list representing the list in database
     */
    public void setProductList(List<Product> productList) {
        this.productList = productList;
        this.cartSize = productList.size();
    }

    public PricesInStore[] getShoppingCartPrices() {
        return this.shoppingCartPrices;
    }

    /**
     * Shows the message in the application. In tests updates the testMessage variable instead.
     * @param message The R.string integer to be shown
     */
    public void showMessage(int message) {
        if (isRunningInTestEnvironment) {
            this.testMessage = message;
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public int getTestMessage() {
        return testMessage;
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
                    ShoppingCartFragmentDirections.actionShoppingCartFragmentToListPricesFragment22(productName, productEan, priceList)
            );
        }
    }

    public void handleResponse(String response) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                productName = jsonObject.getString("name");

                JSONArray jsonArray = jsonObject.getJSONArray("prices");
                int l = jsonArray.length();
                priceList = new PriceListItem[l];

                for (int i = 0; i < l; i++) {
                    JSONObject j = jsonArray.getJSONObject(i);
                    priceList[i] = new PriceListItem(j.getInt("cents"), j.getString("storeId"),
                            j.getString("timestamp"), productEan);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        // If the responce contains no price list, create a fake, so that argument won't be null
        // Id must be that of selectedStore, so that it won't be listed in the next fragment
        if (priceList == null) {
            priceList = new PriceListItem[1];
            priceList[0] = new PriceListItem(0, "", "timestamp", "");
        }

    }


    /**
     * Deletes the product from te database. Shows a snackbar that allows to cancel the deletion.
     * @param product Product to delete
     */
    public void deleteProduct(Product product) {
        ShoppingCartViewModel viewModel = new ViewModelProvider(getActivity()).get(ShoppingCartViewModel.class);
        viewModel.delete(product);
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.product_deleted_from_shopping_cart, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo_delete, v -> viewModel.insert(product));
        snackbar.show();
    }

    /**
     * Handles what happens when user swipes a product in the shopping cart product list
     */
    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        private ShoppingCartListAdapter mAdapter;

        public SwipeToDeleteCallback(ShoppingCartListAdapter adapter) {
            // Handle only swipes to left
            super(0,ItemTouchHelper.LEFT);
            mAdapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // This method is not needed
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // Delete the product that was swiped
            int position = viewHolder.getAdapterPosition();
            Product product = mAdapter.getProduct(position);
            deleteProduct(product);
        }

    }

}
