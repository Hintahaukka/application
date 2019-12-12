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

import hifian.hintahaukka.Domain.ParcelableHashMap;
import hifian.hintahaukka.Domain.PriceListItem;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.StoreManager;

public class ProductListAdapter extends ArrayAdapter<PriceListItem> {
    Context context;
    int resource;
    List<PriceListItem> priceList;
    StoreManager storeManager;
    ParcelableHashMap<String, String> eanWithNames;

    public ProductListAdapter(Context context, int resource, List<PriceListItem> priceList, StoreManager storeManager, ParcelableHashMap<String, String> eanWithNames) {
        super(context, resource, priceList);
        this.context = context;
        this.resource = resource;
        this.priceList = priceList;
        this.storeManager = storeManager;
        this.eanWithNames = eanWithNames;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.list_products_in_store, null);
        TextView price = view.findViewById(R.id.text_price);
        TextView text_product_name = view.findViewById(R.id.text_product_name);
        TextView timestamp = view.findViewById(R.id.text_timestamp);

        PriceListItem item = priceList.get(position);

        price.setText(parsePrice(item));
        text_product_name.setText(eanWithNames.get(item.getEan()));
        timestamp.setText(parseTimestamp(item));

        return view;
    }

    private String parsePrice(PriceListItem item) {
        double cents = item.getCents() / 100.0;
        return String.format("%.02f", cents) + "€";
    }

    private String parseTimestamp(PriceListItem item) {
        String date = item.getTimestamp();
        if (date.length() != 0) {
            return (date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4));
        }
        return getContext().getString(R.string.text_price_is_average);

    }
}
