package hifian.hintahaukka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;


public class SplashScreenActivity extends AppCompatActivity {
    public Location location;
    public GpsActivity gpsActivity;
    private static int SPLASH_TIME_OUT = 3000;
    private int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.gpsActivity = new GpsActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        requirePermissionToUseLocation(true);
    }

    private void requirePermissionToUseLocation(boolean firstTimer) {

        // Check if permission has already been given earlier
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Permission has already been granted, we can go on to the app
            permissionGranted();

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
            // Permission was granted, we can go on to the app
            permissionGranted();

        } else {
            // Permission denied, ask permission again
            requirePermissionToUseLocation(false);
        }
    }

    /**
     * Continues to the actual app. Should be called only when permission to find location is
     * granted by the user.
     */
    private void permissionGranted() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocation();
                Intent intent = getIntentWithLocationArguments();
                startActivity(intent);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public Location getLocation() {
        location = gpsActivity.getLocation();
        if (location == null) {
            Log.e("SplashScreenActivity", "GPS unable to get value");
            return null;
        } else {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            Log.i("SplashScreenActivity", "GPS lat = " + lat + " lon = " + lon);

            return location;
        }
    }

    public Intent getIntentWithLocationArguments() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);

        if (location != null) {
            intent.putExtra("lat", location.getLatitude());
            intent.putExtra("lon", location.getLongitude());
        } else {
            Double def = 0.0;
            intent.putExtra("lat", def);
            intent.putExtra("lon", def);
        }

        return intent;
    }
}
