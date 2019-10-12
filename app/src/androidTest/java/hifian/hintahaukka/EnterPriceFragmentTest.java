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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class EnterPriceFragmentTest {

    String selectedStore = "selectedStore";
    String scanResult = "scanResult";
    String productName = "Omena";
    NavController mockNavController;

    private void launchEnterPriceFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(selectedStore, selectedStore);
        bundle.putString(scanResult, scanResult);
        bundle.putString(productName, productName);
        bundle.putBoolean("test", false);
        FragmentScenario<EnterPriceFragment> scenario =
                FragmentScenario.launchInContainer(EnterPriceFragment.class, bundle);

        mockNavController = Mockito.mock(NavController.class);
        scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), mockNavController);
            }
        });
    }


/**
    @Test
    public void testSendingPrice() {

        // GIVEN - On the enter price screen
        launchEnterPriceFragment();

        // WHEN - User types price 2,50€ and clicks send
        onView(withId(R.id.enterEuros)).perform(typeText("2"));
        onView(withId(R.id.enterCents)).perform(typeText("50"));
        onView(withId(R.id.nameField)).perform(replaceText("Omena"));
        PriceListItem[] prices = new PriceListItem[]{new PriceListItem(250,"23","2019-10-01 19:48:57.356073")};
        closeSoftKeyboard();

        onView(withId(R.id.sendPriceBtn)).perform(click());

        // THEN - Application navigates to list prices with correct price as argument
        verify(mockNavController).navigate(
                EnterPriceFragmentDirections.actionEnterPriceFragmentToListPricesFragment(
                        selectedStore, scanResult, "250", productName, prices, false));
    }

    @Test
    public void testSendingEmptyPrice() {

        // GIVEN - On the enter price screen
        launchEnterPriceFragment();

        // WHEN - User clicks send without typing a price
        onView(withId(R.id.sendPriceBtn)).perform(click());

        // THEN - Application navigates to list prices with 0,00€ as price
        verify(mockNavController).navigate(
                EnterPriceFragmentDirections.actionEnterPriceFragmentToListPricesFragment(
                        selectedStore, scanResult, "0", productName, new PriceListItem[]{}, false));
    }
**/
    @Test
    public void typingCommaMovesFocusToEnterCents() {

        // GIVEN - On the enter price screen
        launchEnterPriceFragment();

        // WHEN - User types price with comma
        onView(withId(R.id.enterEuros)).perform(typeText("2,50"));

        // THEN - Comma is removed and the price is divided to right fields
        onView(withId(R.id.enterEuros)).check(matches((withText("2"))));
        onView(withId(R.id.enterCents)).check(matches((withText("50"))));
    }

    @Test
    public void typingDotMovesFocusToEnterCents() {

        // GIVEN - On the enter price screen
        launchEnterPriceFragment();

        // WHEN - User types price with dot
        onView(withId(R.id.enterEuros)).perform(typeText("2.50"));

        // THEN - Comma is removed and the price is divided to right fields
        onView(withId(R.id.enterEuros)).check(matches((withText("2"))));
        onView(withId(R.id.enterCents)).check(matches((withText("50"))));
    }
}
