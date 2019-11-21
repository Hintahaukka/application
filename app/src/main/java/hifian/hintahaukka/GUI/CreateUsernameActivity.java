package hifian.hintahaukka.GUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class CreateUsernameActivity extends AppCompatActivity {

    private TextView usernameField;
    private Button sendUsernameButton;
    private String nickname;
    private TextView firstTimeRegisterFied;
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
        firstTimeRegisterFied = (TextView) findViewById(R.id.firstTimeRegisterFied);
        firstTimeRegisterFied.setText(R.string.first_time_register_text);
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


    //Method to check if username is taken
    /*public boolean checkIfUserNameIsTaken(String username) {
        //Will be implemented later
    }*/


    //Method to check username is atleast 2 characters long
    public boolean checkIfUserNameIsLongEnough(String username) {
        if (username.length() < 3) {
            return false;
        }
        return true;
    }

    //Method to check username length is less than 20
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
            this.setUrlString("https://hintahaukka.herokuapp.com/test/getNewId");
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            userId = response;
            /**Snackbar.make(findViewById(android.R.id.content), "Response: " + userId, Snackbar.LENGTH_LONG).show();
             * See if there was a response
             */

        }
    }



    //Post nickname with id to backend
    private class PostNewNicknameTask extends HttpPostTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.setUrlString("https://hintahaukka.herokuapp.com/test/updateNickname");

            this.setParamNames(new String[]{"id","nickname"});

            // Will be implemented later
            /*if (isRunningInTestEnvironment) {
                this.setMocked();
            }*/
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.key_user_id), userId);
            editor.apply();

            continueToApp();

        }
    }



    private void continueToApp() {
        Intent intent = new Intent(CreateUsernameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
