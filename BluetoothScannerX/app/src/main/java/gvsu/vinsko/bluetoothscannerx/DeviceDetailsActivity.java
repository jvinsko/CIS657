package gvsu.vinsko.bluetoothscannerx;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.parceler.Parcels;

public class DeviceDetailsActivity extends AppCompatActivity {

    private TextView nameLabel;
    private TextView addressLabel;
    private TextView rssiLabel;
    private TextView stationaryLabel;
    private TextView lastSeenDateLabel;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_device_details);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        nameLabel = (TextView) findViewById(R.id.nameLabel);
        addressLabel = (TextView) findViewById(R.id.addressLabel);
        rssiLabel = (TextView) findViewById(R.id.rssiLabel);
        stationaryLabel = (TextView) findViewById(R.id.stationaryLabel);
        lastSeenDateLabel = (TextView) findViewById(R.id.lastSeenDateLabel);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("DEVICE")) {
            Parcelable parcel = intent.getParcelableExtra("DEVICE");
            Device d = Parcels.unwrap(parcel);
            setTitle(d.getName());
            nameLabel.setText("Name: " + d.getName());
            addressLabel.setText("Address: " + d.getAddress());
            rssiLabel.setText("RSSI: " + d.getRssi());
            if(d.getStationary() == null) {
                stationaryLabel.setText("Stationary: First Time Seen");
            } else if(d.getStationary() == true) {
                stationaryLabel.setText("Stationary: Unmoved Since Last Sighting");
            } else if(d.getStationary() == false) {
                stationaryLabel.setText("Stationary: Moved Since Last Sighting");
            } else {
                stationaryLabel.setText("Stationary: Not Enough Information");
            }
            lastSeenDateLabel.setText("Last Seen On: " + d.getLastSeen());
        }

        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}