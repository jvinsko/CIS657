package gvsu.vinsko.bluetoothscannerfinal.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DeviceContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DeviceItem> ITEMS = new ArrayList<DeviceItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DeviceItem> ITEM_MAP = new HashMap<String, DeviceItem>();

    private static final int COUNT = 25;

    public static void addItem(DeviceItem item) {
        ITEMS.add(item);
    }

    static {
        DeviceItem di = new DeviceItem("Jon's Phone", "-59", false);
        ITEMS.add(di);
        di = new DeviceItem("BLE Beacon 1", "-22", true);
        ITEMS.add(di);
        di = new DeviceItem("BLE Beacon 2", "-33", true);
        ITEMS.add(di);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DeviceItem {
        public final String name;
        public final String rssi;
        public final Boolean stationary;

        public DeviceItem(String name, String rssi, Boolean stationary) {
            this.name = name;
            this.rssi = rssi;
            this.stationary = stationary;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}