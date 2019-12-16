package hifian.hintahaukka.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import hifian.hintahaukka.Domain.PricesInStore;
import hifian.hintahaukka.Domain.Store;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.StoreManager;

public class ShoppingCartPriceListAdapter extends ArrayAdapter<PricesInStore> {

    Context context;
    int resource;
    List<PricesInStore> priceList;
    StoreManager storeManager;

    public ShoppingCartPriceListAdapter(Context context, int resource, List<PricesInStore> priceList, StoreManager storeManager) {
        super(context, resource, priceList);
        this.context = context;
        this.resource = resource;
        this.priceList = priceList;
        this.storeManager = storeManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.compare_shopping_carts_item, null);
        TextView price = view.findViewById(R.id.text_cart_price);
        TextView shop = view.findViewById(R.id.text_cart_shop);

        PricesInStore item = priceList.get(position);

        if (!item.getStoreId().equals("")) {
            price.setText(parsePrice(item));
            shop.setText(parseStore(item));
        }

        return view;
    }

    private String parsePrice(PricesInStore item) {
        double cents = item.getCentsTotal() / 100.0;
        return String.format("%.02f", cents) + "€";
    }

    private String parseStore(PricesInStore item) {
        Store s = storeManager.getStore(item.getStoreId());

        if (s != null && s.getName() != null) {
            return s.getName();
        } else {
            return "Tuntematon kauppa";
        }
    }
}
