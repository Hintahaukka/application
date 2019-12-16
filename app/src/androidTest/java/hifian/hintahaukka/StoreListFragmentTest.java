package hifian.hintahaukka;

import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import hifian.hintahaukka.GUI.StoreListFragment;
import hifian.hintahaukka.GUI.StoreListFragmentDirections;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.verify;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class StoreListFragmentTest {

    // mock NavController
    NavController mockNavController;

    private void launchStoreListFragment() {

        FragmentScenario<StoreListFragment> scenario =
                FragmentScenario.launchInContainer(StoreListFragment.class);

        mockNavController = Mockito.mock(NavController.class);
        scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                Navigation.setViewNavController(fragment.requireView(), mockNavController);
            }
        });
    }

    @Test
    public void correctStoreIdIsPassedToTheNextFragment() {

        // GIVEN - On the store list screen
        launchStoreListFragment();

        // WHEN - User chooses the first store when coordinates are 0, 0
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());

        // THEN - Application navigates to scan button fragment with correct store id
        verify(mockNavController).navigate(
               StoreListFragmentDirections.actionStoreListFragmentToScanButtonFragment(
                        "418006009"));
    }
    
}

class Matchers {
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }
}