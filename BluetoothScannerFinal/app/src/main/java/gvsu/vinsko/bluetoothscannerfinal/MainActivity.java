package gvsu.vinsko.bluetoothscannerfinal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Set;
import gvsu.vinsko.bluetoothscannerfinal.dummy.DeviceContent;
import static android.R.attr.filter;

public class MainActivity extends AppCompatActivity implements DeviceFragment.OnListFragmentInteractionListener {
    public static int REQUEST_BLUETOOTH = 1;

    private Button scanBtn;
    private BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button) findViewById(R.id.scanBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*scanBtn.setOnClickListener(v -> {
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    DeviceContent.DeviceItem item = new DeviceContent.DeviceItem(device.getName(),device.getAddress(),false);
                    DeviceContent.addItem(item);
                }
            }
            btAdapter.startDiscovery();
            recreate();
        });*/

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (btAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        if (!btAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        btAdapter.startDiscovery();

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bReceiver, filter);
    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DeviceContent.DeviceItem item = new DeviceContent.DeviceItem(device.getName(), device.getAddress(),false);
                DeviceContent.addItem(item);
                recreate();
            }
        }
    };

    @Override
    public void onListFragmentInteraction(DeviceContent.DeviceItem item) {
        Intent deviceDetails = new Intent(this, DeviceDetailsActivity.class);
        deviceDetails.putExtra("DEVICE", item.name);
        startActivity(deviceDetails);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bReceiver);
        super.onDestroy();
    }
}