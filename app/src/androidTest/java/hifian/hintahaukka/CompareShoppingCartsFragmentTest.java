package hifian.hintahaukka;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import hifian.hintahaukka.Domain.PricesInStore;
import hifian.hintahaukka.GUI.CompareShoppingCartsFragment;
import hifian.hintahaukka.Domain.PriceListItem;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class CompareShoppingCartsFragmentTest {

    // mock NavController
    NavController mockNavController;

    private void launchCompareShoppingCartsFragment(Bundle bundle) {

        FragmentScenario<CompareShoppingCartsFragment> scenario =
                FragmentScenario.launchInContainer(CompareShoppingCartsFragment.class, bundle);

        mockNavController = Mockito.mock(NavController.class);
        scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), mockNavController);
            }
        });
    }

    @Test
    public void totalPricesAreListedCorrectly() {

        // GIVEN - There are price info from two stores
        PricesInStore[] pricesInStores = new PricesInStore[2];
        PricesInStore item1 = new PricesInStore("418006009", 250, new PriceListItem[]{new PriceListItem(100, "418006009" , "2019-10-17 19:48:56.9918", "12345678"), new PriceListItem(150, "418006009", "2019-10-09 16:55:20.6143", "87654321")});
        PricesInStore item2 = new PricesInStore("26197451", 520, new PriceListItem[]{new PriceListItem(200, "26197451" , "2019-10-17 19:48:56.9918", "12345678"), new PriceListItem(320, "26197451", "", "87654321")});
        pricesInStores[0] = item1;
        pricesInStores[1] = item2;

        Bundle bundle = new Bundle();
        bundle.putParcelableArray("shoppingCartPrices", pricesInStores);

        // WHEN - On the compare shopping carts fragment screen
        launchCompareShoppingCartsFragment(bundle);

        // THEN - Both total prices are listed with store names
        final DataInteraction firstPrice = onData(anything())
                .inAdapterView(withId(R.id.shoppingCartPriceListView))
                .atPosition(0);

        firstPrice.onChildView(withText(String.format("%.02f", 250/100.0) + "€"))
                .check(matches(isDisplayed()));
        firstPrice.onChildView(withText("Punkten"))
                .check(matches(isDisplayed()));

        final DataInteraction secondPrice = onData(anything())
                .inAdapterView(withId(R.id.shoppingCartPriceListView))
                .atPosition(1);

        secondPrice.onChildView(withText(String.format("%.02f", 520/100.0) + "€"))
                .check(matches(isDisplayed()));
        secondPrice.onChildView(withText("Pallon Teboil"))
                .check(matches(isDisplayed()));
    }

}
