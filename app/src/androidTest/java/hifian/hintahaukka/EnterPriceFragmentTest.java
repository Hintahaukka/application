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

import hifian.hintahaukka.GUI.EnterPriceFragment;
import hifian.hintahaukka.GUI.EnterPriceFragmentDirections;
import hifian.hintahaukka.Domain.PriceListItem;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class EnterPriceFragmentTest {

    String selectedStore = "selectedStore";
    boolean test = false;

    //default productName returned by mocked HttpPostTask
    String productName = "Omena";

    PriceListItem[] prices;

    // mock NavController
    NavController mockNavController;

    // this running Fragment
    EnterPriceFragment thisFragment;

    private void launchEnterPriceFragment(String scanResult) {
        Bundle bundle = new Bundle();
        bundle.putString("selectedStore", selectedStore);
        bundle.putString("scanResult", scanResult);
        bundle.putString("productName", productName);
        bundle.putBoolean("test", test);

        FragmentScenario<EnterPriceFragment> scenario =
                FragmentScenario.launchInContainer(EnterPriceFragment.class, bundle);

        mockNavController = Mockito.mock(NavController.class);
        scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), mockNavController);
                thisFragment = (EnterPriceFragment) fragment;
            }
        });
    }

    @Test
    public void typedPriceIsPassedToTheNextFragment() {

        // GIVEN - On the enter price screen
        launchEnterPriceFragment("scanResult");

        // WHEN - User types price 2,50€ and clicks send
        onView(withId(R.id.enterEuros)).perform(typeText("2"));
        onView(withId(R.id.enterCents)).perform(typeText("50"));
        closeSoftKeyboard();

        onView(withId(R.id.sendPriceBtn)).perform(click());

        // THEN - Application navigates to list prices with correct price as argument
        prices = thisFragment.getPrices();
        verify(mockNavController).navigate(
                EnterPriceFragmentDirections.actionEnterPriceFragmentToListPricesFragment(
                        selectedStore, "scanResult", "250", productName, prices, test));


    }

    @Test
    public void ifPriceFieldsAreEmptyThePricePassedToTheNextFragmentIsZero() {

        // GIVEN - On the enter price screen
        launchEnterPriceFragment("scanResult");

        // WHEN - User clicks send without typing a price
        onView(withId(R.id.sendPriceBtn)).perform(click());

        // THEN - Application navigates to list prices with 0,00€ as price
        prices = thisFragment.getPrices();
        verify(mockNavController).navigate(
                EnterPriceFragmentDirections.actionEnterPriceFragmentToListPricesFragment(
                        selectedStore, "scanResult", "0", productName, prices, test));
    }

    @Test
    public void typingCommaMovesFocusToEnterCents() {

        // GIVEN - On the enter price screen
        launchEnterPriceFragment("scanResult");

        // WHEN - User types price with comma
        onView(withId(R.id.enterEuros)).perform(typeText("2,50"));

        // THEN - Comma is removed and the price is divided to right fields
        onView(withId(R.id.enterEuros)).check(matches((withText("2"))));
        onView(withId(R.id.enterCents)).check(matches((withText("50"))));
    }

    @Test
    public void typingDotMovesFocusToEnterCents() {

        // GIVEN - On the enter price screen
        launchEnterPriceFragment("scanResult");

        // WHEN - User types price with dot
        onView(withId(R.id.enterEuros)).perform(typeText("2.50"));

        // THEN - Comma is removed and the price is divided to right fields
        onView(withId(R.id.enterEuros)).check(matches((withText("2"))));
        onView(withId(R.id.enterCents)).check(matches((withText("50"))));
    }

    @Test
    public void fieldAndButtonForProductNameEntryAreShownAndFunctionalIfThereIsNoProductName() {

        // GIVEN - On the enter price screen, product does not have a name
        launchEnterPriceFragment("scanResultWithoutProductName");

        // WHEN - User types in the name of the product and pushes the send button
        onView(withId(R.id.enterProductNameField)).perform(typeText("Fazer suklaapatukka"));
        onView(withId(R.id.sendProdNameBtn)).perform(click());

        // THEN - Thank you is displayed and field and button are disabled.
        onView(withId(R.id.enterProductNameField)).check(matches(withText("Kiitos!")));
        onView(withId(R.id.enterProductNameField)).check(matches(not(isEnabled())));
        onView(withId(R.id.sendProdNameBtn)).check(matches(not(isEnabled())));

    }

    @Test
    public void fieldAndButtonForProductNameEntryAreNotShownIfThereIsProductName() {

        // GIVEN - On the enter price screen, product does have a name
        launchEnterPriceFragment("scanResultWithProductName");

        // THEN - Field and button are not shown.
        onView(withId(R.id.enterProductNameField)).check(matches(not(isDisplayed())));
        onView(withId(R.id.sendProdNameBtn)).check(matches(not(isDisplayed())));

    }
}
