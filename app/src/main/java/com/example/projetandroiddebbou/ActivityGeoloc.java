package com.example.projetandroiddebbou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityGeoloc extends AppCompatActivity implements SensorEventListener {
    private LocationManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_geoloc);
        //recupere le manager
        mSensorManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //recupere un provider pour obtenir la position
        LocationProvider provider = mSensorManager.getProvider(LocationManager.GPS_PROVIDER);

        Log.d("lifecycle", "ok");

        //on crée le listener
        final LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("lifecycle", "onLocationChanged");
                // A new location update is received.  Do something useful with it.
                setText("" + location.getLatitude(), "" + location.getLongitude());

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("lifecycle", "run");
                        setText("" + location.getLatitude(), "" + location.getLongitude());
                    }
                });
            }

        };

        Log.d("lifecycle", "ok1");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("lifecycle", "pas OK !");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 44);

            /*return;*/
        }
        mSensorManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,          // 10-second interval.
                10,             // 10 meters.
                listener);

    }

    private void setText(String posX, String posY) {
        Log.d("lifecycle", "setText");
        TextView txtPositionGPS = (TextView) findViewById(R.id.txtPositionGPS);
        txtPositionGPS.setText(posX + "   " + posY);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Toast.makeText(this, "onSensorChanged", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Toast.makeText(this, "onAccuracyChanged", Toast.LENGTH_LONG).show();
    }


    //Verifie si le provider existe
    @Override
    protected void onStart() {
        super.onStart();

        // This verification should be done during onStart() because the system calls
        // this method when the user returns to the activity, which ensures the desired
        // location provider is enabled each time the activity resumes from the stopped state.
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        Toast.makeText(this, "Verification gps ...", Toast.LENGTH_SHORT).show();
        if (!gpsEnabled) {
            Toast.makeText(this, "GPS pas activé", Toast.LENGTH_LONG).show();
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
}