package kr.ac.kookmin.cs.homework01;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by user on 2016-10-26.
 */

public class GPSListener implements LocationListener {
    SQLiteDatabase db;
    GoogleMap mMap;
    ArrayList<LatLng> save;
    PolylineOptions polylineOption;

    GPSListener(SQLiteDatabase db, GoogleMap mMap, ArrayList<LatLng> save){
        this.db = db;
        this.mMap = mMap;
        this.save = save;
    }

    public void drawPolyline(){
        polylineOption = new PolylineOptions();
        polylineOption.color(Color.RED);
        polylineOption.width(5);
        polylineOption.addAll(save);
        mMap.addPolyline(polylineOption);
    }

    public void onLocationChanged(Location location) {
        String s = "INSERT INTO Location (X, Y) VALUES(?,?);";
        double x = location.getLatitude(), y = location.getLongitude();
        db.execSQL(s, new Object[]{x, y});
        save.add(new LatLng(x, y));
        drawPolyline();
        mMap.addCircle(new CircleOptions().center(new LatLng(x, y)).radius(10).strokeColor(Color.RED).fillColor(Color.BLUE));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(x, y)));
    }
    public void onProviderDisabled(String provider) {}
    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
