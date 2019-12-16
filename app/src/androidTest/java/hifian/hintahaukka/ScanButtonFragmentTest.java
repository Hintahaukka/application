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

import hifian.hintahaukka.GUI.ScanButtonFragment;
import hifian.hintahaukka.GUI.ScanButtonFragmentDirections;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ScanButtonFragmentTest {

    NavController mockNavController;

    // The id of the closest store when coordinates are 0, 0
    String defaultStoreId  = "418006009";

    private void launchScanButtonFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("selectedStore", defaultStoreId);

        FragmentScenario<ScanButtonFragment> scenario =
                FragmentScenario.launchInContainer(ScanButtonFragment.class, bundle);

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

        // GIVEN - On the scan button fragment screen
        launchScanButtonFragment();

        // WHEN - User clicks scan barcode button
        onView(withId(R.id.button_scan_barcode)).perform(click());

        // THEN - Application navigates to barcode scanner with the id of selected store and the test parameter set false
        verify(mockNavController).navigate(
                ScanButtonFragmentDirections.actionScanButtonFragmentToBarcodeScannerFragment(
                        defaultStoreId, true));
    }

}