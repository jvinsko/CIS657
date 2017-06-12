package gvsu.vinsko.bluetoothscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import gvsu.vinsko.bluetoothscanner.dummy.DeviceContent;

public class MainActivity extends AppCompatActivity implements DeviceFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onListFragmentInteraction(DeviceContent.DeviceItem item) {
        Intent deviceDetails = new Intent(this, DeviceDetailsActivity.class);
        deviceDetails.putExtra("DEVICE", item.name);
        startActivity(deviceDetails);
    }
}
