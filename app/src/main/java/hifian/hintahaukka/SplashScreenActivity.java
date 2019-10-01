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
    public Location l;
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
            l = gpsActivity.getLocation();
            if( l == null){
                Toast.makeText(this,"GPS unable to get Value",Toast.LENGTH_LONG).show();
            }else {
                double lat = l.getLatitude();
                double lon = l.getLongitude();
                Toast.makeText(this,"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_LONG).show();
                return l;
            }
            return null;
        }

        public Intent getIntentWithLocationArguments() {
            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            if (l != null) {
                i.putExtra("lat", l.getLatitude());
                i.putExtra("lon", l.getLongitude());
            } else {
                Double def= 0.0;
                i.putExtra("lat", def);
                i.putExtra("lon", def);
            }
            return i;
        }
}
