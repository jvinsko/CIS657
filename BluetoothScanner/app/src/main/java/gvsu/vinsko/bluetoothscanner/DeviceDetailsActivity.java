package gvsu.vinsko.bluetoothscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DeviceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        Intent i = this.getIntent();
        String name = i.getStringExtra("DEVICE");
        setTitle(name);
    }
}
