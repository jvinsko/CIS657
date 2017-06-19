package gvsu.vinsko.bluetoothscannerx;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;
import gvsu.vinsko.bluetoothscannerx.dummy.DeviceContent;

public class  MainActivity extends AppCompatActivity implements DeviceFragment.OnListFragmentInteractionListener {
    public static int REQUEST_BLUETOOTH = 1;

    private Button logoutBtn;
    private BluetoothAdapter btAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logoutBtn = (Button) findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

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

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for(BluetoothDevice device : pairedDevices) {
                DeviceContent.DeviceItem item = new DeviceContent.DeviceItem(device.getName(), "-50", false);
                DeviceContent.addItem(item);
            }
        }

        btAdapter.startDiscovery();


        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(bReceiver, filter);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(bReceiver, filter2);

    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            Log.d("App", "onReceive");

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DeviceContent.DeviceItem item = new DeviceContent.DeviceItem(device.getName(), "-50", false);
                DeviceContent.addItem(item);
                recreate();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                btAdapter.startDiscovery();
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