package hifian.hintahaukka.GUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.HttpGetTask;
import hifian.hintahaukka.Service.HttpPostTask;
import hifian.hintahaukka.Service.UserManager;

public class CreateUsernameActivity extends AppCompatActivity {

    private TextView usernameField;
    private Button sendUsernameButton;
    private String nickname;
    private String userId;
    private String[] parameters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_username);

        usernameField = (TextView) findViewById(R.id.usernameField);
        sendUsernameButton = (Button) findViewById(R.id.sendUsernameButton);
        new GetNewIdTask().execute();

        sendUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfUserNameIsLongEnough(usernameField.getText().toString()) == false) {
                    //Hide keyboard to show error message
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(findViewById(android.R.id.content), R.string.text_username_too_short, Snackbar.LENGTH_LONG).show();

                } else if (checkIfUserNameIsTooLong(usernameField.getText().toString()) == false) {
                    //Hide keyboard to show error message
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(findViewById(android.R.id.content), R.string.text_username_too_long, Snackbar.LENGTH_LONG).show();
                } else {
                    nickname = usernameField.getText().toString();
                    parameters = new String[] {userId, nickname};
                    new PostNewNicknameTask().execute(parameters);
                }
            }
        });
    }

    // Method to check that username is at least 3 characters long
    public boolean checkIfUserNameIsLongEnough(String username) {
        if (username.length() < 3) {
            return false;
        }
        return true;
    }

    // Method to check that username is not longer than 20 characters
    public boolean checkIfUserNameIsTooLong(String username) {
        if (username.length() > 20) {
            return false;
        }
        return true;
    }

    //Get a new id from backend
    private class GetNewIdTask extends HttpGetTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.setUrlString("https://hintahaukka.herokuapp.com/getNewId");
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            userId = response;
        }
    }

    //Post nickname with id to backend
    private class PostNewNicknameTask extends HttpPostTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.setUrlString("https://hintahaukka.herokuapp.com/updateNickname");
            this.setParamNames(new String[]{"id","nickname"});
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            updateUserIdAndNickName();
            continueToApp();
        }
    }

    private void updateUserIdAndNickName() {
        UserManager userManager = new UserManager(this);
        userManager.setUserId(userId);
        userManager.setUserName(nickname);
    }

    private void continueToApp() {
        Intent intent = new Intent(CreateUsernameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
