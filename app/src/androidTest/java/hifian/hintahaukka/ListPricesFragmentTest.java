package hifian.hintahaukka;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ListPricesFragmentTest {

    // default values
    String selectedStore = "418006009";
    String ean = "ean";
    boolean test = false;
    String productName = "Omena";
    String cents = "100";
    PriceListItem[] prices = {new PriceListItem(80, "30326364" , "2019-10-17 19:48:56.9918")};

    // mock NavController
    NavController mockNavController;

    private void launchListPricesFragment(Bundle bundle) {

        FragmentScenario<ListPricesFragment> scenario =
                FragmentScenario.launchInContainer(ListPricesFragment.class, bundle);

        mockNavController = Mockito.mock(NavController.class);
        scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), mockNavController);
            }
        });
    }

    @Test
    public void otherPricesAreListedCorrectly() {

        // GIVEN - There are two prices added to the product previously
        PriceListItem[] myPrices = new PriceListItem[2];
        myPrices[0] = new PriceListItem(100, "26197451" , "2019-10-17 19:48:56.9918");
        myPrices[1] = new PriceListItem(250, "30288487", "2019-10-09 16:55:20.6143");

        Bundle bundle = new Bundle();
        bundle.putParcelableArray("priceList", myPrices);
        bundle.putString("selectedStore", selectedStore);
        bundle.putString("scanResult", ean);
        bundle.putString("productName", productName);
        bundle.putBoolean("test", test);
        bundle.putString("cents", cents);

        // WHEN - On the list prices screen
        launchListPricesFragment(bundle);

        // THEN - Both prices are listed with correct dates and store names
        onView(withId(R.id.pricesTextView)).check(matches(withText(containsString("Pallon Teboil\n17.10.2019\nHinta: "
                + String.format("%.02f", 1.0)))));

        onView(withId(R.id.pricesTextView)).check(matches(withText(containsString("Stockmann Turku\n09.10.2019\nHinta: "
                + String.format("%.02f", 2.5)))));
    }

    @Test
    public void insertedPriceAndStoreAreShownCorrectly() {

        // GIVEN - selected store's id is 1839427464 and inserted price is 150 cents
        Bundle bundle = new Bundle();
        bundle.putString("selectedStore", "1839427464");
        bundle.putString("productName", productName);
        bundle.putString("cents", "150");
        bundle.putString("scanResult", ean);
        bundle.putBoolean("test", test);
        bundle.putParcelableArray("priceList", prices);

        // WHEN - On the list prices screen
        launchListPricesFragment(bundle);

        // THEN - Store name Alepa Sturenkatu 40 and price 1,50€ are shown in price field
        onView(withId(R.id.myPriceField)).check(matches(withText(containsString("Alepa Sturenkatu 40\nHinta: "
                + String.format("%.02f", 1.5)))));
    }

    @Test
    public void productNameIsShownCorrectly() {

        // GIVEN - product name is Coca Cola
        Bundle bundle = new Bundle();
        bundle.putString("productName", "Coca Cola");
        bundle.putString("selectedStore", selectedStore);
        bundle.putString("cents", cents);
        bundle.putString("scanResult", ean);
        bundle.putBoolean("test", test);
        bundle.putParcelableArray("priceList", prices);

        // WHEN - On the list prices screen
        launchListPricesFragment(bundle);

        // THEN - Text Coca Cola is shown in product field
        onView(withId(R.id.productField)).check(matches(withText(containsString("Coca Cola"))));
    }

}
