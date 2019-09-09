package hifian.hintahaukka;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

public class ListPricesActivity extends AppCompatActivity {

    Map<String, List<String>> prices = new HashMap<>();
    String ean;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("scanResult") && getIntent().hasExtra("price")) {
            ean = getIntent().getExtras().getString("scanResult");
            price = getIntent().getExtras().getString("price");

        }
    }
}
