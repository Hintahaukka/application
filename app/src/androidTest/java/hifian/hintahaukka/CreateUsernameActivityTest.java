package hifian.hintahaukka;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.snackbar.Snackbar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import hifian.hintahaukka.GUI.CreateUsernameActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.util.regex.Pattern.matches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;



@MediumTest
    @RunWith(AndroidJUnit4.class)
    public class CreateUsernameActivityTest {

    @Rule
    public ActivityTestRule<CreateUsernameActivity> rule = new ActivityTestRule<>(CreateUsernameActivity.class);


    @Test
    public void ensureViewIsPresented() {
        CreateUsernameActivity activity = rule.getActivity();
        TextView usernameField = activity.findViewById(R.id.usernameField);
        TextView firstTimeRegisterFied = activity.findViewById(R.id.firstTimeRegisterFied);
        Button sendUsernameButton = activity.findViewById(R.id.sendUsernameButton);

        assertThat(firstTimeRegisterFied, notNullValue());
        assertThat(usernameField, notNullValue());
        assertThat(sendUsernameButton, notNullValue());
    }

    /**
     
    @Test
    public void ensureUsernameTooShortTextIsDisplayed() {
        String shortUsername = "a";

        CreateUsernameActivity activity = rule.getActivity();
        TextView usernameField = activity.findViewById(R.id.usernameField);
        Button sendUsernameButton = activity.findViewById(R.id.sendUsernameButton);

        usernameField.setText(shortUsername);
        sendUsernameButton.performClick();

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.text_username_too_short)));



    }
    */
}