package hifian.hintahaukka;

import android.widget.Button;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import hifian.hintahaukka.GUI.CreateUsernameActivity;

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
        TextView firstTimeRegisterFied = activity.findViewById(R.id.firstTimeRegisterField);
        Button sendUsernameButton = activity.findViewById(R.id.sendUsernameButton);

        assertThat(firstTimeRegisterFied, notNullValue());
        assertThat(usernameField, notNullValue());
        assertThat(sendUsernameButton, notNullValue());
    }
}