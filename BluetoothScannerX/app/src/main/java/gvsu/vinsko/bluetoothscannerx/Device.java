package gvsu.vinsko.bluetoothscannerx;

import org.parceler.Parcel;

/**
 * Created by Jon on 6/19/2017.
 */

@Parcel
public class Device {
    String name;
    String address;
    int rssi;
    String foundDate;
    double foundLat;
    double foundLong;

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

    public String getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(String foundDate) {
        this.foundDate = foundDate;
    }

    public double getFoundLat() {
        return foundLat;
    }

    public void setFoundLat(double foundLat) { this.foundLat = foundLat; }

    public double getFoundLong() {
        return foundLong;
    }

    public void setFoundLong(double foundLong) {
        this.foundLong = foundLong;
    }
}
