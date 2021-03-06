package hifian.hintahaukka.GUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.HttpGetTask;
import hifian.hintahaukka.Service.UserManager;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean isNewUser = false;
    private String userId;
    private int completedTasks = 0;
    private final int TASKS_TO_COMPLETE = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Wake Heroku
        new WakeHerokuTask().execute("");
        requirePermissionToUseLocation(true);
        getUserId();
    }

    private void requirePermissionToUseLocation(boolean firstTimer) {

        // Check if permission has already been given earlier
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Permission has already been granted
            taskCompleted();

        } else {

            // Permission has not been granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // If user has previously denied permission, tell the reason why we are asking the permission to the user
                Snackbar.make(findViewById(R.id.image_splash_screen_logo), R.string.snackbar_request_permission_rationale, Snackbar.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Either user is using the app first time or user has clicked "never ask again" and denied permission
                if (firstTimer) {
                    // If first timer, ask permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // If user has pressed "never ask again", show message and don't let the user use the app
                    Snackbar.make(findViewById(R.id.image_splash_screen_logo), R.string.snackbar_require_permission, Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission was granted
            taskCompleted();
        } else {
            // Permission denied, ask permission again
            requirePermissionToUseLocation(false);
        }
    }

    /**
     * Checks if a user id already exists. If not, get a new one from the server.
     */
    private void getUserId() {

        // Check if there already is an id in the memory
        UserManager userManager = new UserManager(this);
        userId = userManager.getUserId();

        // If not, mark as new user (to be guided to create username in continueToApp())
        if (userId == null) {
           isNewUser = true;
        }

        taskCompleted();
    }

    /**
     * Sends a wake request to the server.
     */
    private class WakeHerokuTask extends HttpGetTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.setUrlString("https://hintahaukka.herokuapp.com/wake");
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String response) {
            taskCompleted();
        }
    }

    /**
     * Counter for completed tasks to ensure that all required asynchronous tasks are completed
     * before continuing to the app.
     */
    private void taskCompleted() {
        completedTasks++;
        if (completedTasks == TASKS_TO_COMPLETE) {
            continueToApp();
        }
    }

    /**
     * Continues to the actual app. Should be called only when all required tasks have
     * finished.
     */
    private void continueToApp() {

        // New users are navigated to create username
        if(isNewUser) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreenActivity.this, CreateUsernameActivity.class);
                startActivity(intent);
                finish();
            }, SPLASH_TIME_OUT);
        } else {

            // Old users navigate straight to the app
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }, SPLASH_TIME_OUT);
        }
    }
}
