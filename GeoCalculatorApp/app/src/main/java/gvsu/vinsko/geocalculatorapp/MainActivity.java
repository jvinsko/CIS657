package gvsu.vinsko.geocalculatorapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Location;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import gvsu.vinsko.geocalculatorapp.webservice.WeatherService;

import static gvsu.vinsko.geocalculatorapp.webservice.WeatherService.BROADCAST_WEATHER;

public class MainActivity extends AppCompatActivity {

    public static final int SETTINGS_SELECTION = 1;
    public static final int HISTORY_RESULT = 2;
    public static final int SEARCH_RESULT = 3;

    public static List<LocationLookup> allHistory;

    private String distUnits = "Kilometers";
    private String bearUnits = "Degrees";

    private Button calcBtn;
    private Button clearBtn;
    private Button searchBtn;
    private TextView distResult;
    private TextView bearResult;
    private EditText latP1;
    private EditText latP2;
    private EditText longP1;
    private EditText longP2;

    private TextView currSum;
    private TextView currTemp;
    private ImageView currImage;
    private TextView destSum;
    private TextView destTemp;
    private ImageView destImage;

    private DatabaseReference topRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        allHistory = new ArrayList<LocationLookup>();

        calcBtn = (Button) findViewById(R.id.calcBtn);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        distResult = (TextView) findViewById(R.id.distResult);
        bearResult = (TextView) findViewById(R.id.bearResult);
        latP1 = (EditText) findViewById(R.id.latP1);
        latP2 = (EditText) findViewById(R.id.longP1);
        longP1 = (EditText) findViewById(R.id.latP2);
        longP2 = (EditText) findViewById(R.id.longP2);

        currSum = (TextView) findViewById(R.id.currSum);
        currTemp = (TextView) findViewById(R.id.currTemp);
        currImage = (ImageView) findViewById(R.id.currImage);
        destSum = (TextView) findViewById(R.id.destSum);
        destTemp = (TextView) findViewById(R.id.destTemp);
        destImage = (ImageView) findViewById(R.id.destImage);

        calcBtn.setOnClickListener(v -> {
            if(latP1.getText().toString().matches("") || latP2.getText().toString().matches("") || longP1.getText().toString().matches("") || longP2.getText().toString().matches("")) {
                distResult.setText("Distance: Fill all four boxes.");
                bearResult.setText("Bearing: Fill all four boxes.");
            } else {
                updateScreen();
            }
        });

        clearBtn.setOnClickListener(w -> {
            latP1.setText("");
            latP2.setText("");
            longP1.setText("");
            longP2.setText("");
            distResult.setText("Distance: ");
            bearResult.setText("Bearing: ");
            setWeatherViews(View.INVISIBLE);
        });

        searchBtn.setOnClickListener(v -> {
           Intent intent = new Intent(MainActivity.this, LocationSearchActivity.class);
           startActivityForResult(intent, SEARCH_RESULT);
        });
    }

    private void setWeatherViews(int visible) {
        currImage.setVisibility(visible);
        currSum.setVisibility(visible);
        currTemp.setVisibility(visible);
        destImage.setVisibility(visible);
        destSum.setVisibility(visible);
        destTemp.setVisibility(visible);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        allHistory.clear();
        topRef = FirebaseDatabase.getInstance().getReference("history");
        topRef.addChildEventListener (chEvListener);
        IntentFilter weatherFilter = new IntentFilter(BROADCAST_WEATHER);
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, weatherFilter);
        setWeatherViews(View.INVISIBLE);
    }

    @Override
    public void onPause(){
        super.onPause();
        topRef.removeEventListener(chEvListener);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent,SETTINGS_SELECTION);
                return true;
            case R.id.action_history:
                Intent intent2 = new Intent(MainActivity.this, HistoryActivity.class);
                startActivityForResult(intent2, HISTORY_RESULT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            double temp = bundle.getDouble("TEMPERATURE");
            String summary = bundle.getString("SUMMARY");
            String icon = bundle.getString("ICON").replaceAll("-", "_");
            String key = bundle.getString("KEY");
            int resID = getResources().getIdentifier(icon , "drawable", getPackageName());
            setWeatherViews(View.VISIBLE);
            if (key.equals("p1")) {
                currSum.setText(summary);
                currTemp.setText(Double.toString(temp));
                currImage.setImageResource(resID);
            } else {
                destSum.setText(summary);
                destTemp.setText(Double.toString(temp));
                destImage.setImageResource(resID);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == SETTINGS_SELECTION) {
            distUnits = data.getStringExtra("dist");
            bearUnits = data.getStringExtra("bear");
            updateScreen();
        } else if (resultCode == HISTORY_RESULT) {
            if(data != null && data.hasExtra("LL")) {
                Parcelable parcel = data.getParcelableExtra("LL");
                LocationLookup ll = Parcels.unwrap(parcel);
                this.latP1.setText(String.valueOf(ll.origLat));
                this.longP1.setText(String.valueOf(ll.origLong));
                this.latP2.setText(String.valueOf(ll.endLat));
                this.longP2.setText(String.valueOf(ll.endLong));
                this.updateScreen(); // code that updates the calcs.
            }
        } else if (resultCode == SEARCH_RESULT) {
            if(data != null && data.hasExtra("LL")) {
                Parcelable parcel = data.getParcelableExtra("LL");
                LocationLookup ll = Parcels.unwrap(parcel);
                this.latP1.setText(String.valueOf(ll.origLat));
                this.longP1.setText(String.valueOf(ll.origLong));
                this.latP2.setText(String.valueOf(ll.endLat));
                this.longP2.setText(String.valueOf(ll.endLong));
                this.updateScreen(); // code that updates the calcs.
            }
        }
    }

    private void updateScreen() {
        double latP1Double = Double.parseDouble(latP1.getText().toString());
        double latP2Double = Double.parseDouble(latP2.getText().toString());
        double longP1Double = Double.parseDouble(longP1.getText().toString());
        double longP2Double = Double.parseDouble(longP2.getText().toString());

        Location p1 = new Location("");
        p1.setLatitude(latP1Double);
        p1.setLongitude(longP1Double);
        Location p2 = new Location("");
        p2.setLatitude(latP2Double);
        p2.setLongitude(longP2Double);

        float distanceInKm = (p1.distanceTo(p2))/1000;
        if(distUnits.matches("Kilometers")) {
            String distString = String.format("%.2f", distanceInKm);
            distResult.setText("Distance: " + distString + " kilometers");
        } else {
            double distanceInMiles = distanceInKm * 0.621371;
            String distString = String.format("%.2f", distanceInMiles);
            distResult.setText("Distance: " + distString + " miles");
        }


        float bearingInDeg = (p1.bearingTo(p2));
        if(bearUnits.matches("Degrees")) {
            String bearString = String.format("%.2f", bearingInDeg);
            bearResult.setText("Bearing: " + bearString + " degrees");
        } else {
            double bearingInMil = bearingInDeg * 17.777777777778;
            String bearString = String.format("%.2f", bearingInMil);
            bearResult.setText("Bearing: " + bearString + " mils");
        }

        // remember the calculation.
        LocationLookup entry = new LocationLookup();
        entry.setOrigLat(latP1Double);
        entry.setOrigLong(longP1Double);
        entry.setEndLat(latP2Double);
        entry.setEndLong(longP2Double);
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        entry.setTimestamp(fmt.print(DateTime.now()));
        topRef.push().setValue(entry);

        WeatherService.startGetWeather(this, Double.toString(latP1Double), Double.toString(longP1Double), "p1");
        WeatherService.startGetWeather(this, Double.toString(latP2Double), Double.toString(longP2Double), "p2");
    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LocationLookup entry = (LocationLookup)
                    dataSnapshot.getValue(LocationLookup.class);
            entry._key = dataSnapshot.getKey();
            allHistory.add(entry);
        }
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            LocationLookup entry = (LocationLookup)
                    dataSnapshot.getValue(LocationLookup.class);
            List<LocationLookup> newHistory = new ArrayList<LocationLookup>();
            for (LocationLookup t : allHistory) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }
            allHistory = newHistory;
        }
        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}
