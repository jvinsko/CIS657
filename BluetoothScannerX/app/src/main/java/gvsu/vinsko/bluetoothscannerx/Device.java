package gvsu.vinsko.bluetoothscannerx;

import org.parceler.Parcel;

/**
 * Created by Jon on 6/19/2017.
 */

@Parcel
public class Device {
    String _key;
    String name;
    String address;
    int rssi;
    String lastSeen;
    double lastLat;
    double lastLong;
    Boolean stationary;

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() { return rssi; }

    public void setRssi(int rssi) { this.rssi = rssi; }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public double getLastLat() {
        return lastLat;
    }

    public void setLastLat(double lastLat) { this.lastLat = lastLat; }

    public double getLastLong() {
        return lastLong;
    }

    public void setLastLong(double lastLong) {
        this.lastLong = lastLong;
    }

    public Boolean getStationary() {
        return stationary;
    }

    public void setStationary(Boolean stationary) {
        this.stationary = stationary;
    }
}
