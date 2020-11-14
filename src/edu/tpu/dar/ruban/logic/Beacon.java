package edu.tpu.dar.ruban.logic;

public class Beacon {
    public MacAddress mac;
    public int id;
    public boolean isMain;
    public double x, y, z;

    public static final int MAIN = 0;
    public static final int TARGET = 2;
    public static final int SOURCE = 3;

    public static final Beacon BEACONS[] = {
            new Beacon("B4:E6:2D:C1:E5:07".toLowerCase().replaceAll(":", ""), 37, true, 0.0, 0.0, 0.0),
            new Beacon("a4:cf:12:8d:40:be".toLowerCase().replaceAll(":", ""), 43, false,0.0, 0.0, 0.0),
            new Beacon("24:6f:28:b2:dc:7a".toLowerCase().replaceAll(":", ""), 42, false,0.0, 0.0, 0.0), //
            new Beacon("a4:cf:12:8d:75:c2".toLowerCase().replaceAll(":", ""), 41, false,0.0, 0.0, 0.0),
            new Beacon("30:ae:a4:8b:44:2a".toLowerCase().replaceAll(":", ""), 44, false,0.0, 0.0, 0.0),

    };

    public Beacon(String mac, int id,  boolean isMain, double x, double y, double z) {
        this.mac = new MacAddress(mac);
        this.isMain = isMain;
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
