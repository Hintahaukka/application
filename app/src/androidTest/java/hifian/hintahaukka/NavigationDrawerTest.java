package hifian.hintahaukka;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import hifian.hintahaukka.GUI.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NavigationDrawerTest {

    @Test
    public void tappingItemInNavigationDrawerNavigatesToRightPlace() {

        // GIVEN - User is past the splash screen
        ActivityScenario.launch(MainActivity.class);

        // WHEN - Navigation drawer is opened and shopping cart option tapped
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open());
        onView(withId(R.id.navView))
                .perform(NavigationViewActions.navigateTo(R.id.shoppingCartFragment));

        // THEN - Application navigates to the shopping cart view
        onView(withId(R.id.shoppingCartLayout)).check(matches(isDisplayed()));
    }
}
