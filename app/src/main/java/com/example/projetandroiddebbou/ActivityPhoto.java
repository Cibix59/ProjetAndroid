package com.example.projetandroiddebbou;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ActivityPhoto extends AppCompatActivity implements SensorEventListener {
    private LocationManager mSensorManager;

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private static double latitude;
    private static double longitude;

    private StringBuilder nomPhoto = new StringBuilder("");
    private StringBuilder nomGroupe = new StringBuilder("");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        this.imageView = (ImageView) this.findViewById(R.id.imageView1);

        //DEBUT GEOLOC

        //recupere le manager
        mSensorManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //recupere un provider pour obtenir la position
        LocationProvider provider = mSensorManager.getProvider(LocationManager.GPS_PROVIDER);

        //on crée le listener
        final LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("lifecycle", "onLocationChanged");
                // A new location update is received.  Do something useful with it.
                setLatitude(location.getLatitude());
                setLongitude(location.getLongitude());


                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("lifecycle", "run");
                        setLatitude(location.getLatitude());
                        setLongitude(location.getLongitude());
                    }
                });
            }
        };


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

        //FIN GEOLOC


        Button buttonGallerie = (Button) this.findViewById(R.id.buttonGallerie);
        buttonGallerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*                    Prendre une photo de la gallerie */
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
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

        popup(nomPhoto,getResources().getString(R.string.popupNom));
        popup(nomGroupe,getResources().getString(R.string.popupGroupe));
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "c'est bon", Toast.LENGTH_LONG).show();

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, nomPhoto.toString(), nomGroupe.toString());

            sqlLiteHelper db = new sqlLiteHelper(this);
            db.addPhoto(new Photo(path,latitude,longitude,nomGroupe.toString(),nomPhoto.toString()));
            db.close();
        }

    }

    private void setLatitude(double latitude) {
        this.latitude=latitude;
    }

    private void setLongitude(double longitude) {
        this.longitude=longitude;
    }

    private void popup(StringBuilder cible, String texte){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(texte);

        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(getResources().getString(R.string.valider), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cible.append(input.getText().toString()) ;
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.refuser), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}