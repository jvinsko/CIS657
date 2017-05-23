package gvsu.vinsko.geocalculatorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;

public class MainActivity extends AppCompatActivity {

    public static final int SETTINGS_SELECTION = 1;

    private String distUnits = "Kilometers";
    private String bearUnits = "Degrees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button calcBtn = (Button) findViewById(R.id.calcBtn);
        Button clearBtn = (Button) findViewById(R.id.clearBtn);
        TextView distResult = (TextView) findViewById(R.id.distResult);
        TextView bearResult = (TextView) findViewById(R.id.bearResult);
        EditText latP1 = (EditText) findViewById(R.id.latP1);
        EditText latP2 = (EditText) findViewById(R.id.longP1);
        EditText longP1 = (EditText) findViewById(R.id.latP2);
        EditText longP2 = (EditText) findViewById(R.id.longP2);

        calcBtn.setOnClickListener(v -> {
            if(latP1.getText().toString().matches("") || latP2.getText().toString().matches("") || longP1.getText().toString().matches("") || longP2.getText().toString().matches("")) {
                distResult.setText("Fill all four boxes.");
                bearResult.setText("Fill all four boxes.");
            } else {
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
                String distString = String.format("%.2f", distanceInKm);
                distResult.setText("" + distString + " kilometers");

                float bearingInDeg = (p1.bearingTo(p2));
                String bearString = String.format("%.2f", bearingInDeg);
                bearResult.setText("" + bearString + " degrees");
            }
        });

        clearBtn.setOnClickListener(w -> {
            latP1.setText("");
            latP2.setText("");
            longP1.setText("");
            longP2.setText("");
            distResult.setText("");
            bearResult.setText("");
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == SETTINGS_SELECTION) {
            distUnits = data.getStringExtra("dist");
            bearUnits = data.getStringExtra("bear");
        }
    }
}
