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
    private TextView seenOnDateLabel;
    private TextView seenAtLatLabel;
    private TextView seenAtLongLabel;

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
        seenOnDateLabel = (TextView) findViewById(R.id.seenOnDateLabel);
        seenAtLatLabel = (TextView) findViewById(R.id.seenAtLatLabel);
        seenAtLongLabel = (TextView) findViewById(R.id.seenAtLongLabel);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("DEVICE")) {
            Parcelable parcel = intent.getParcelableExtra("DEVICE");
            Device d = Parcels.unwrap(parcel);
            setTitle(d.getName());
            nameLabel.setText("Name: " + d.getName());
            addressLabel.setText("Address: " + d.getAddress());
            rssiLabel.setText("RSSI: " + d.getRssi());
            seenOnDateLabel.setText("Seen On: " + d.getFoundDate());
            seenAtLatLabel.setText("Seen At: " + d.getFoundLat() + " Latitude");
            seenAtLongLabel.setText("Seen At: " + d.getFoundLong() + " Longitude");
        }

        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}