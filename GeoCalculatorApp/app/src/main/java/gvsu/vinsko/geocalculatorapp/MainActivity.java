package gvsu.vinsko.geocalculatorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;

import org.joda.time.DateTime;

import gvsu.vinsko.geocalculatorapp.dummy.HistoryContent;

public class MainActivity extends AppCompatActivity {

    public static final int SETTINGS_SELECTION = 1;
    public static final int HISTORY_RESULT = 2;

    private String distUnits = "Kilometers";
    private String bearUnits = "Degrees";

    private Button calcBtn;
    private Button clearBtn;
    private TextView distResult;
    private TextView bearResult;
    private EditText latP1;
    private EditText latP2;
    private EditText longP1;
    private EditText longP2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        calcBtn = (Button) findViewById(R.id.calcBtn);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        distResult = (TextView) findViewById(R.id.distResult);
        bearResult = (TextView) findViewById(R.id.bearResult);
        latP1 = (EditText) findViewById(R.id.latP1);
        latP2 = (EditText) findViewById(R.id.longP1);
        longP1 = (EditText) findViewById(R.id.latP2);
        longP2 = (EditText) findViewById(R.id.longP2);

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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == SETTINGS_SELECTION) {
            distUnits = data.getStringExtra("dist");
            bearUnits = data.getStringExtra("bear");
            updateScreen();
        } else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");
            this.latP1.setText(vals[0]);
            this.longP1.setText(vals[1]);
            this.latP2.setText(vals[2]);
            this.longP2.setText(vals[3]);
            this.updateScreen(); // code that updates the calcs.
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
        HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(Double.toString(latP1Double),
                Double.toString(longP1Double), Double.toString(latP2Double), Double.toString(longP2Double), DateTime.now());
        HistoryContent.addItem(item);
    }
}
