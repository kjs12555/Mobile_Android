package kr.ac.kookmin.cs.homework02;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    DBHelper test;
    SQLiteDatabase db;
    ArrayList<LatLng> save = new ArrayList<LatLng>();
    GoogleMap mMap;
    LocationManager currentLocation;
    TextView textLatitude;
    TextView textLongitude;
    Button checkLocation;
    Button submit;
    EditText log;
    Context context;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> data;
    String choice;
    GPSListener gps;
    ArrayList<saveClass> saveData;
    TabHost tabHost;
    Button statistics;
    TextView textStatistics;
    class saveClass{
        double x;
        double y;
        String division;
        String logConText;
        saveClass(double inX, double inY, String inDivision, String inLogConText){
            x=inX;
            y=inY;
            division=inDivision;
            logConText=inLogConText;
        }
    }

    public void onClickedStatistics(View v) {
        Cursor rs = db.rawQuery("select * from Logger order by Division;", null);
        textStatistics.setText("");
        String preDivision="";
        String currentDivision="current";
        String text = "";
        int count_num=0;
        while(rs.moveToNext()){
            preDivision = currentDivision;
            currentDivision = rs.getString(2);
            count_num++;
            if(!preDivision.equals(currentDivision)){
                if(preDivision.equals("current")){
                    text = text.concat(currentDivision+"\n");
                    text = text.concat(rs.getString(0)+" "+rs.getString(1)+rs.getString(2)+" "+" "+rs.getString(3)+"\n");
                    count_num=0;
                    continue;
                }
                text = text.concat(preDivision+"에 대한 갯수 : "+Integer.toString(count_num));
                count_num=0;
                text = text.concat("\n\n"+currentDivision+"\n");
            }
            text = text.concat(rs.getString(0)+" "+rs.getString(1)+" "+rs.getString(2)+" "+" "+rs.getString(3)+"\n");
        }
        rs.moveToLast();
        text = text.concat(preDivision+"에 대한 갯수 : "+Integer.toString(count_num+1));
        text = text.concat("\n\n총 데이터 갯수 : "+Integer.toString(saveData.size()));
        textStatistics.setText(text);
        Toast.makeText(context, "통계가 작성되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void drawPolyline(){
        PolylineOptions polylineOption = new PolylineOptions();
        polylineOption.color(Color.RED);
        polylineOption.width(5);
        polylineOption.addAll(save);
        mMap.addPolyline(polylineOption);
    }

    public void initialize(){
        saveData = new ArrayList<saveClass>();
        Cursor rs = db.rawQuery("select * from Logger;", null);
        while(rs.moveToNext()){
            saveClass tmpData = new saveClass(rs.getDouble(0), rs.getDouble(1), rs.getString(2), rs.getString(3));
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(tmpData.x,tmpData.y)).title(tmpData.division).snippet(tmpData.logConText);
            mMap.addMarker(markerOptions).showInfoWindow();
            saveData.add(tmpData);
        }
        drawPolyline();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.610215, 126.997202)));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        mMap.animateCamera(zoom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("Tab1").setContent(R.id.tab1).setIndicator("지도"));
        tabHost.addTab(tabHost.newTabSpec("Tab2").setContent(R.id.tab2).setIndicator("통계"));


        textLatitude = (TextView)findViewById(R.id.latitude);
        textLongitude = (TextView)findViewById(R.id.longitude);
        checkLocation = (Button)findViewById(R.id.location);
        submit = (Button)findViewById(R.id.submit);
        log = (EditText)findViewById(R.id.logEditText);
        context = this;
        data = new ArrayList<String>();
        data.add("휴식");
        data.add("문화");
        data.add("공부");
        data.add("과제");
        data.add("음식");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        textStatistics = (TextView)findViewById(R.id.textStatistics);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choice = (String) spinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(test==null)test = new DBHelper(MapsActivity.this, "EveryDayLog_db", null, 1);
        db = test.getWritableDatabase();
        gps = new GPSListener();
        initialize();

        currentLocation = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try{
            currentLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gps);
            currentLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, gps);
        }catch(SecurityException ex){}

        checkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude=0;
                double longitude=0;
                boolean flag=true;
                try{
                    Location lastLocationNetwork = currentLocation.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Location lastLocationGPS = currentLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(lastLocationNetwork!=null){
                        latitude = lastLocationNetwork.getLatitude();
                        longitude = lastLocationNetwork.getLongitude();
                    }else if(lastLocationGPS!=null){
                        latitude = lastLocationGPS.getLatitude();
                        longitude = lastLocationGPS.getLongitude();
                    }
                    else{
                        flag=false;
                        Toast.makeText(context, "GPS연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    if(flag){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                        textLatitude.setText(Double.toString(latitude));
                        textLongitude.setText(Double.toString(longitude));
                    }
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                    mMap.animateCamera(zoom);
                }catch(Exception e){
                    Toast.makeText(context, "오류발생!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClass tmpData = new saveClass(Double.parseDouble(textLatitude.getText().toString()),Double.parseDouble(textLongitude.getText().toString()),choice,log.getText().toString());
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(textLatitude.getText().toString()), Double.parseDouble(textLongitude.getText().toString()))).title(choice).snippet(log.getText().toString());
                mMap.addMarker(markerOptions).showInfoWindow();
                String a = "insert into Logger(X, Y, Division, LogContext) values(?, ?, ?, ?);";
                db.execSQL(a, new Object[]{tmpData.x,tmpData.y,tmpData.division,tmpData.logConText});
                saveData.add(tmpData);
            }
        });
    }
}
