package gvsu.vinsko.bluetoothscannerx;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Jon on 6/19/2017.
 */

public class BluetoothScannerXApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
