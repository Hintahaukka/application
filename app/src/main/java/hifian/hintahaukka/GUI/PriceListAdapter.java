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

import hifian.hintahaukka.Domain.Store;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.PriceListItem;
import hifian.hintahaukka.Service.StoreManager;

public class PriceListAdapter extends ArrayAdapter<PriceListItem> {

    Context context;
    int resource;
    List<PriceListItem> priceList;
    StoreManager storeManager;

    public PriceListAdapter(Context context, int resource, List<PriceListItem> priceList, StoreManager storeManager) {
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

        View view = inflater.inflate(R.layout.list_prices_item, null);
        TextView price = view.findViewById(R.id.text_price);
        TextView shop = view.findViewById(R.id.text_shop);
        TextView timestamp = view.findViewById(R.id.text_timestamp);

        PriceListItem item = priceList.get(position);

        price.setText(parsePrice(item));
        shop.setText(parseStore(item));
        timestamp.setText(parseTimestamp(item));

        return view;
    }

    private String parsePrice(PriceListItem item) {
        double cents = item.getCents() / 100.0;
        return String.format("%.02f", cents) + "€";
    }

    private String parseStore(PriceListItem item) {
        Store s = storeManager.getStore(item.getStoreId());

        if (s != null && s.getName() != null) {
            return s.getName();
        } else {
            return "Tuntematon kauppa";
        }
    }

    private String parseTimestamp(PriceListItem item) {
        String date = item.getTimestamp();
        return (date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4));
    }
}
