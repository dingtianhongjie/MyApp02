package com.example.soltmember.myapp02;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;


public class LocationActivity extends AppCompatActivity implements LocationListener{

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    1000);
        }else {
            locationStart();
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 50, this);
        }
    }

    private void locationStart(){
        Log.d("debug","locationStart()");

        mLocationManager =
                (LocationManager)getSystemService(LOCATION_SERVICE);

        if(mLocationManager != null && mLocationManager.isProviderEnabled(mLocationManager.GPS_PROVIDER)){
            Log.d("debug", "location manager Enabled");
        }else {
            Intent settingsIntent =
                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not gpsEnable, startActivity");
        }

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            Log.d("debug", "checkSelfPermission false");
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 50, this);


    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug", "checkSelfPermission true");
                locationStart();
            }else{
                Toast toast = Toast.makeText(this,"何もできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        // 開発
        Toast.makeText(this, "Latitude:" + location.getLatitude() + ", " +
                "Longtude:" + location.getLongitude(), Toast.LENGTH_LONG).show();
        // ここまで

        final TextView textIdo = (TextView)findViewById(R.id.textIdo);
        final TextView textKeido = (TextView)findViewById(R.id.textKeido);
        Button buttonGet = (Button)findViewById(R.id.buttonGet);

        double ido = location.getLatitude();
        double keido = location.getLongitude();
        
        int intIdo = (int)(ido);
        int intKeido = (int)(keido);

        final int paramIdo = (int)((ido - intIdo) * 10000);
        final int paramKeido = (int)((keido - intKeido) * 10000);

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stringで動作確認
                String stIdo = String.valueOf(paramIdo);
                String stKeido = String.valueOf(paramKeido);

                textIdo.setText(stIdo);
                textKeido.setText(stKeido);
                // ここまで

            }
        });



//          テキストビューに入れるだけのコード
//        TextView textIdo = (TextView)findViewById(R.id.textIdo);
//        String str1 = "Latitude:" + location.getLatitude();
//        textIdo.setText(str1);
//
//        TextView textKeido = (TextView)findViewById(R.id.textKeido);
//        String str2 = "Longtude:" + location.getLongitude();
//        textKeido.setText(str2);
//          ここまで
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status){
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                            break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                        break;
        }

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
