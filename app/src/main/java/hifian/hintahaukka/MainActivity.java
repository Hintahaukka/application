package hifian.hintahaukka;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spinner = (Spinner) findViewById(R.id.storeSpinner);

        StoreManager storeManager = new StoreManager();
        storeManager.setStores(this.handleStores());

        // TODO: Dropdown menu should show store names, but send the store id as value
        List<String> storeList = storeManager.listNearestStores(0, 0);

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,storeList){

            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Button scanButton = findViewById(R.id.button_scan_barcode);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BarcodeScannerActivity.class);
                intent.putExtra("selectedStore", String.valueOf(spinner.getSelectedItem()));
                startActivity(intent);
            }
        });
    }

    /**
     * Calls the StoreHandler to access the list of Store objects
     * @return
     */
    private List<Store> handleStores() {

        List<Store> stores = new ArrayList<>();
        try {
            InputStream istream = getAssets().open("stores.osm");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            StoreHandler storeHandler = new StoreHandler();
            saxParser.parse(istream, storeHandler);

            stores = storeHandler.getStores();

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return stores;
    }

}
