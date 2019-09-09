package hifian.hintahaukka;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ListPricesActivity extends AppCompatActivity {

    Map<String, List<String>> prices = new HashMap<>();
    String ean;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_prices);

        if (getIntent().hasExtra("scanResult") && getIntent().hasExtra("price")) {
            ean = getIntent().getExtras().getString("scanResult");
            price = getIntent().getExtras().getString("price");
            TextView pricesTextView = (TextView) findViewById(R.id.pricesTextView);
            pricesTextView.setText("Viivakoodi: " + ean);

        }
    }
}
