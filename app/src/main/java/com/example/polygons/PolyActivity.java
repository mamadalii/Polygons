package com.example.polygons;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.PolyUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.example.polygons.R.id.Number_OF_Polygons_Text_View;
import static com.example.polygons.R.id.map;


/**
 * An activity that displays a Google map with polygons to represent areas.
 */

public class PolyActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,

        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static int n = 0;


    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location userLocation;
    ArrayList<polygon> polygonsObjectsArray=new ArrayList<>();
    int buttonCount ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            Log.v("kheili","hast");
            buttonCount=savedInstanceState.getInt("buttonCount");
        }
        else{
            buttonCount=0;}
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);




        textView = findViewById(R.id.textView);



        handler = new Handler();

//        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

//        adapter = new ArrayAdapter<String>(PolyActivity.this,
//                android.R.layout.simple_list_item_1,
//                ListElementsArrayList
//        );

//        listView.setAdapter(adapter);
        //Start Button Click Listener
        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the activity to statistics activity
                Intent intent = new Intent(PolyActivity.this,Stastistics.class);
                startActivity(intent);
            }
        });




//run the check runnable
        checkHandler = new Handler();
        checkHandler.postDelayed(checkRunnable, 1000);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);


            }
        }
    }

    /**

     */
    ArrayList<LatLng> optionsLat = new ArrayList<>();
    ArrayList<LatLng> optionsLatReserve = new ArrayList<>();
    Polygon polygon1;
    List<List<LatLng>> pointsArr = new ArrayList<>();
    ArrayList<Polygon> polyList = new ArrayList<Polygon>();
    //$
    ArrayList<Marker> markerList = new ArrayList<>();



    boolean buttonB = true;
    String s = "";




    public void buttonFunction(View view) {
++buttonCount;
        //removing the markers
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(markerList.size() - i - 1).remove();
        }

        polyList.add(polygon1);
        pointsArr.add(optionsLat);
        optionsLatReserve.addAll(optionsLat);
        EditText e = findViewById(R.id.place_tag_edit_text);
        s = e.getText().toString();
polygon p = new polygon(optionsLat,s);
polygonsObjectsArray.add(p);
        polygon1.setTag(s);
        TextView number_of_polygons_text_view = findViewById(R.id.Number_OF_Polygons_Text_View);
String number_of_polygons= String.valueOf(buttonCount);
        number_of_polygons_text_view.setText(number_of_polygons);
        optionsLat.clear();
        e.getText().clear();
        buttonB = false;


    }


    @Override
    public void onMapReady(GoogleMap gooogleMap) {
        final GoogleMap googleMap = gooogleMap;

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            Marker m;

            @Override
            public void onLocationChanged(Location location) {
                if (m != null) {
                    m.remove();
                }


                m = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude())).title("You Are Here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

                userLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
//        public void clickShapeButton(){
//            createShape();
//        }


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(LatLng location) {


                Marker m;
                m = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.latitude, location.longitude))
                        .draggable(true));
                markerList.add(m);
                optionsLat.add(new LatLng(location.latitude, location.longitude));

                Geocoder geocoder = new Geocoder(PolyActivity.this);
                try {
                    geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    ++n;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PolygonOptions options = new PolygonOptions();
                if (buttonB == false) {
                    for (int j = 0; j < pointsArr.size(); j++) {
                        for (int k = 0; k < pointsArr.get(j).size(); k++) {

                            options.add(new LatLng(pointsArr.get(j).get(k).latitude, pointsArr.get(j).get(k).latitude));
Log.v("injast",k+"");
                            polygon1 = googleMap.addPolygon(options.clickable(true));


                        }
                    }
                    buttonB = true;
                }
                if (n > 2) {


                    if (polygon1 != null) {
                        polygon1.remove();

                    }

                    for (int i = 0; i < optionsLat.size(); i++) {
                        options.add(new LatLng(optionsLat.get(i).latitude, optionsLat.get(i).longitude));
                    }

//

                    polygon1 = googleMap.addPolygon(options.fillColor(COLOR_BLUE_ARGB).visible(true).clickable(true));


                    //
////        // Store a data object with the polygon, used here to indicate an arbitrary type.

////        // Style the polygon.
//                    stylePolygon(polygon1);

                }
            }


        });


        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);


    }


    /**
     * Styles the polygon, based on type.
     *
     * @param polygon The polygon object that needs styling.
     */
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }

    /**
     * Listens for clicks on a polyline.
     *
     * @param polyline The polyline object that the user has clicked.
     */
    @Override
    public void onPolylineClick(Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern.
//        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
//            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
//        } else {
//            // The default pattern is a solid stroke.
//            polyline.setPattern(null);
//        }

        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
//         Flip the values of the red, green, and blue components of the polygon's color.
        int color = polygon.getStrokeColor() ^ 0x00ffffff;
        polygon.setStrokeColor(color);
        color = polygon.getFillColor() ^ 0x00ffffff;
        polygon.setFillColor(COLOR_BLUE_ARGB);

        if (polygon.getTag() != null) {
            Toast.makeText(this, "Area type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getPoint(Location location) {
        //Convert location to LatLng
        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());

        ArrayList<LatLng> nearestOfEachPolygon=new ArrayList<>();
        ArrayList<Float>Distances=new ArrayList<>();
//pointsArr.size = number of Polygons
        //return the closet point of each polygon to user location
        for (int k=0;k<pointsArr.size();k++){
           nearestOfEachPolygon.add(NearestPoint(l,pointsArr.get(k)));
        }

        //build an arrayList of Distances of each polygon to user
        for (int k=0;k<nearestOfEachPolygon.size();k++){
            //Convert LatLng to Location
            Location loc1 = new Location(LocationManager.GPS_PROVIDER);
            loc1.setLatitude(nearestOfEachPolygon.get(k).latitude);
            loc1.setLongitude((nearestOfEachPolygon.get(k).longitude));
//add Distances of each point of neeaestOfEachPolygon to users location
          Distances.add((float)(distanceBetweenToPoint(loc1,location)));
        }


        ArrayList<LatLng> tempOptionsLat=new ArrayList<>();
        tempOptionsLat.addAll(pointsArr.get(Distances.indexOf(Collections.min(Distances))));

        TextView textView = findViewById(R.id.result_text_view);

//        LatLng l = new LatLng(-38,14);
        boolean result = (PolyUtil.containsLocation(l, tempOptionsLat, true));
        textView.setText(String.valueOf(result));
        optionsLatReserve.clear();
        nearestOfEachPolygon.clear();
        Distances.clear();
        tempOptionsLat.clear();
        return result;
    }

    public float distanceBetweenToPoint(Location loc1,Location loc2){
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;

    }


//#fields
    TextView textView;

    Button start;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;

    Handler handler;

    int Seconds, Minutes,hours, MilliSeconds;

    ListView listView;

    String[] ListElements = new String[]{};

    List<String> ListElementsArrayList;

    ArrayAdapter<String> adapter;

//runnable that runs timer
    public Runnable startRunnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.elapsedRealtime() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            hours = Minutes/60;
            Minutes=Minutes%60;
            Seconds = Seconds % 60;



            textView.setText(""+String.format("%02d",hours)+":"+ String.format("%02d", Minutes)+ ":"
                    + String.format("%02d", Seconds));


            handler.postDelayed(this, 0);
        }

    };




//Check the location and Start the timer if user is in a polygon
    Handler checkHandler;
    public Runnable checkRunnable = new Runnable() {


        public void run() {

            checkHandler.postDelayed(checkRunnable, 0);
            if (polyList.size() > 0) {

                if (getPoint(userLocation)) {
                    StartTime = SystemClock.elapsedRealtime();
                    handler.postDelayed(startRunnable, 0);

                }

            }
        }
    };


    private LatLng NearestPoint(LatLng test, List<LatLng> target) {
        double distance = -1;
        LatLng minimumDistancePoint = test;

        if (test == null || target == null) {
            return minimumDistancePoint;
        }

        for (int i = 0; i < target.size(); i++) {
            LatLng point = target.get(i);

            int segmentPoint = i + 1;
            if (segmentPoint >= target.size()) {
                segmentPoint = 0;
            }

            double currentDistance = PolyUtil.distanceToLine(test, point, target.get(segmentPoint));
            if (distance == -1 || currentDistance < distance) {
                distance = currentDistance;
                minimumDistancePoint = findNearestPoint(test, point, target.get(segmentPoint));
            }
        }

        return minimumDistancePoint;
    }

    /**
     * Based on `distanceToLine` method from
     * https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
     */
 //Belongs to Impelementation of finding closest point of list to a point(NearestPoint Method)
    private LatLng findNearestPoint(final LatLng p, final LatLng start, final LatLng end) {
        if (start.equals(end)) {
            return start;
        }

        final double s0lat = Math.toRadians(p.latitude);
        final double s0lng = Math.toRadians(p.longitude);
        final double s1lat = Math.toRadians(start.latitude);
        final double s1lng = Math.toRadians(start.longitude);
        final double s2lat = Math.toRadians(end.latitude);
        final double s2lng = Math.toRadians(end.longitude);

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0) {
            return start;
        }
        if (u >= 1) {
            return end;
        }

        return new LatLng(start.latitude + (u * (end.latitude - start.latitude)),
                start.longitude + (u * (end.longitude - start.longitude)));


    }
@Override
    public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        outState.putInt("buttonCunt",  buttonCount);
        outState.putString("hey","hety");
        Log.v("buttonCount is",buttonCount+"");

        // etc.
    }


//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        Log.v("hereIS","onRestore");
//
//    }





}

