package hifian.hintahaukka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EnterPriceActivity extends AppCompatActivity {
    String ean;
    String selectedStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_price);

        if (getIntent().hasExtra("scanResult") && getIntent().hasExtra("selectedStore")) {
            TextView eanField = (TextView) findViewById(R.id.eanField);
            ean = getIntent().getExtras().getString("scanResult");
            selectedStore = getIntent().getExtras().getString("selectedStore");
            eanField.setText("Viivakoodi: " + ean);
        }

        Button sendPriceBtn = findViewById(R.id.sendPriceBtn);
        sendPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView priceField = (TextView) findViewById(R.id.priceField);
                Intent intent = new Intent(getApplicationContext(), ListPricesActivity.class);
                intent.putExtra("scanResult", ean);
                intent.putExtra("cents", priceField.getText().toString());
                intent.putExtra("selectedStore", selectedStore);
                startActivity(intent);
            }
        });



    }
}