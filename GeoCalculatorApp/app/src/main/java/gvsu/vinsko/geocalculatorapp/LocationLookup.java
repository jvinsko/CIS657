package gvsu.vinsko.geocalculatorapp;

import org.joda.time.DateTime;
import org.parceler.Parcel;

/**
 * Created by Jon on 6/8/2017.
 */
@Parcel
public class LocationLookup {
    public double getOrigLat() {
        return origLat;
    }

    public void setOrigLat(double origLat) {
        this.origLat = origLat;
    }

    public double getOrigLong() {
        return origLong;
    }

    public void setOrigLong(double origLong) {
        this.origLong = origLong;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLong() {
        return endLong;
    }

    public void setEndLong(double endLong) {
        this.endLong = endLong;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    double origLat;
    double origLong;
    double endLat;
    double endLong;
    String _key;
    String timestamp;
}
