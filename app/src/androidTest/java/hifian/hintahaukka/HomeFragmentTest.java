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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    NavController mockNavController;

    // The id of the closest store when coordinates are 0, 0
    String defaultStoreId  = "418006009";

    private void launchHomeFragment() {
        FragmentScenario<HomeFragment> scenario =
                FragmentScenario.launchInContainer(HomeFragment.class);

        mockNavController = Mockito.mock(NavController.class);
        scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), mockNavController);
            }
        });
    }

    @Test
    public void ifUserClicksScanBarcodeButtonThenFragmentNavigatesToTheNextFragmentWithCorrectStoreIdAndTestArguments() {

        // GIVEN - On the home fragment screen
        launchHomeFragment();

        // WHEN - User clicks scan barcode button
        onView(withId(R.id.button_scan_barcode)).perform(click());

        // THEN - Application navigates to barcode scanner with the id of selected store and the test parameter set false
        verify(mockNavController).navigate(
                HomeFragmentDirections.actionHomeFragmentToBarcodeScannerFragment(
                        "418006009", false));
    }

    @Test
    public void ifUserClicksCheckboxOnceThenParameterTestIsTrueWhenFragmentNavigatesToTheNextFragment() {

        // GIVEN - On the home fragment screen
        launchHomeFragment();

        // WHEN - User selects the checkbox and clicks scan barcode button
        onView(withId(R.id.checkbox_test_database)).perform(click());
        onView(withId(R.id.button_scan_barcode)).perform(click());

        // THEN - Application navigates to barcode scanner with parameter test set true
        verify(mockNavController).navigate(
                HomeFragmentDirections.actionHomeFragmentToBarcodeScannerFragment(
                        "418006009", true));
    }

}