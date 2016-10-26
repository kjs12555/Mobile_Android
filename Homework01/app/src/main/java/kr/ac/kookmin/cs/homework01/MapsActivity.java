package kr.ac.kookmin.cs.homework01;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    DBHelper test;
    SQLiteDatabase db;
    ArrayList<LatLng> save = new ArrayList<LatLng>();
    PolylineOptions polylineOptions;
    GoogleMap mMap;
    ZoomControls zoom;


    public void initialize(){
        Cursor rs = db.rawQuery("select * from Location;", null);
        while(rs.moveToNext()){
            mMap.addCircle(new CircleOptions().center(new LatLng(rs.getDouble(0),rs.getDouble(1))).radius(10).strokeColor(Color.RED).fillColor(Color.BLUE));
            save.add(new LatLng(rs.getDouble(0), rs.getDouble(1)));
        }
        this.drawPolyline();
    }

    public void drawPolyline(){
        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        polylineOptions.addAll(save);
        mMap.addPolyline(polylineOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(test==null)test = new DBHelper(MapsActivity.this, "test_db", null, 1);
        db = test.getWritableDatabase();
        this.initialize();
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                String s = "INSERT INTO Location (X, Y) VALUES(?,?);";
                double x=location.getLatitude(),y=location.getLongitude();
                db.execSQL(s,new Object[]{x,y});
                save.add(new LatLng(x,y));
                drawPolyline();
                mMap.addCircle(new CircleOptions().center(new LatLng(x,y)).radius(10).strokeColor(Color.RED).fillColor(Color.BLUE));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(x,y)));
            }
            public void onProviderDisabled(String provider) {}
            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };
        try{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,10,mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000,10,mLocationListener);   //네트워크 제공자는 데이터가 안되서 사용을 못했습니다. 데이터를 사용해서 정확도는 떨어지지만 실내에서도 사용 가능하다는 점이 장점.
        }catch(SecurityException ex){}

        if(save.size()!=0){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(save.get(0)));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.610215, 126.997202)));
        }
        Toast.makeText(this, "DB에 있는 정보의 수 : "+Integer.toString(save.size()), Toast.LENGTH_SHORT).show();
        //mMap.setMinZoomPreference(13);
    }
}
