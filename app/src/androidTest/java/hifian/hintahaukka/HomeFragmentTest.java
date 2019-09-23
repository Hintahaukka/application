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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Test
    public void navigateToScanBarcode() {

        // GIVEN - On the home screen

        // Launch HomeFragment in a container to test it in isolation
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class);

        // Set up mocked NavController for the launched fragment
        final NavController mockNavController = Mockito.mock(NavController.class);
        scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), mockNavController);
            }
        });

        // WHEN - User clicks the scan barcode button
        onView(withId(R.id.button_scan_barcode))
                .perform(click());

        // THEN - App navigates to scan a barcode

        // Hard-coded selectedStore argument will cause failure soon - should be changed when
        // HomeFragment code is changed and problems arise
        verify(mockNavController)
                .navigate(HomeFragmentDirections.actionHomeFragmentToBarcodeScannerFragment("26197451"));
    }

}
