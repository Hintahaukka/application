package hifian.hintahaukka;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class SplashScreenActivity extends AppCompatActivity {
    public Location location;
    public GpsActivity gpsActivity;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.gpsActivity = new GpsActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        requestPermission();

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
    
    public void requestPermission() {
        if (this.gpsActivity != null) {
            try {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(this,"NULL",Toast.LENGTH_LONG).show();
        }
    }

    public Location getLocation() {
        location = gpsActivity.getLocation();
        if(location == null) {
            Toast.makeText(this,"GPS unable to get Value",Toast.LENGTH_LONG).show();
        } else {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            Toast.makeText(this,"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_LONG).show();

            return location;
        }
        return null;
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
