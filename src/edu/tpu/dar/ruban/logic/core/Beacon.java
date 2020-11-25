package edu.tpu.dar.ruban.logic.core;

import java.util.HashMap;
import java.util.Map;

public class Beacon {
    public MacAddress mac;
    public int id;
    public boolean isMain;
    public double x, y, z;

    public static final int MAIN = 37;
    public static final int TARGET = 42;
    public static final int SOURCE = 37;

    public static final Map<Integer, Beacon> BEACONS;
    static {
        BEACONS = new HashMap<>();
        BEACONS.put(37, new Beacon("B4:E6:2D:C1:E5:07".replaceAll(":", "").toLowerCase(), 37, true, 0.0, 0.0, 0.0));
        BEACONS.put(43, new Beacon("a4:cf:12:8d:40:be".replaceAll(":", "").toLowerCase(), 43, false,0.0, 200.0, 0.0));
        BEACONS.put(42, new Beacon("24:6f:28:b2:dc:7a".replaceAll(":", "").toLowerCase(), 42, false,0.0, 0.0, 0.0));
        BEACONS.put(41, new Beacon("a4:cf:12:8d:75:c2".replaceAll(":", "").toLowerCase(), 41, false,220.0, 200.0, 0.0));
        BEACONS.put(44, new Beacon("30:ae:a4:8b:44:2a".replaceAll(":", "").toLowerCase(), 44, false,220.0, 0.0, 0.0));
    }


    public Beacon(String mac, int id,  boolean isMain, double x, double y, double z) {
        this.mac = new MacAddress(mac);
        this.isMain = isMain;
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
