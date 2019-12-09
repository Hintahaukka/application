package hifian.hintahaukka;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import hifian.hintahaukka.Database.Product;
import hifian.hintahaukka.Domain.PricesInStore;
import hifian.hintahaukka.GUI.ShoppingCartFragment;
import hifian.hintahaukka.GUI.ShoppingCartFragmentDirections;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ShoppingCartFragmentTest {

    private ShoppingCartFragment thisFragment;
    // mock NavController
    NavController mockNavController;

    private void launchShoppingCartFragment() {

        FragmentScenario<ShoppingCartFragment> scenario =
                FragmentScenario.launchInContainer(ShoppingCartFragment.class);

        mockNavController = Mockito.mock(NavController.class);
        scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), mockNavController);
                thisFragment = (ShoppingCartFragment) fragment;
            }
        });
    }

    @Test
    public void compareShoppingCartPricesButtonSendsPriceDataToNextFragment() {

        // GIVEN - On the shopping cart fragment screen
        launchShoppingCartFragment();

        // WHEN - There are two products in the shopping cart
        List<Product> products = new ArrayList<>();
        products.add(new Product("6414893083202", "Rainbow banaanirahka"));
        products.add(new Product("6411402106407", "Oululainen ruisleipä"));
        thisFragment.setProductList(products);

        // AND - User clicks Compare shopping cart prices button
        onView(withId(R.id.button_compare_shopping_cart_prices)).perform(click());

        // THEN - Received price data is sent to next fragment
        PricesInStore[] prices = thisFragment.getShoppingCartPrices();

        verify(mockNavController).navigate(
                ShoppingCartFragmentDirections.actionShoppingCartFragmentToCompareShoppingCartsFragment(
                        prices));
    }

    @Test
    public void ifThereAreNotEnoughPointsToComparePricesThenApplicationWillShowAMessage() {

        // GIVEN - On the shopping cart fragment screen
        launchShoppingCartFragment();

        // WHEN - There are three products in the shopping cart but not enough points
        List<Product> products = new ArrayList<>();
        products.add(new Product("6414893083202", "Rainbow banaanirahka"));
        products.add(new Product("6411402106407", "Oululainen ruisleipä"));
        products.add(new Product("470062700013", "Värska originaal mineraalivesi"));
        thisFragment.setProductList(products);

        // AND - User clicks Compare shopping cart prices button
        onView(withId(R.id.button_compare_shopping_cart_prices)).perform(click());

        // THEN - Received price data is sent to next fragment
        assertTrue(thisFragment.getTestMessage() == R.string.text_not_enough_points);

    }
}
