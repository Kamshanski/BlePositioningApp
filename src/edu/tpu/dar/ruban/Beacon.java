package edu.tpu.dar.ruban;

public class Beacon {
    MacAddress mac;
    int id;
    double x, y, z;

    public static Beacon BEACONS[] = {
            new Beacon("", 37, 0.0, 0.0, 0.0),
            new Beacon("", 41, 0.0, 0.0, 0.0),
            new Beacon("", 42, 0.0, 0.0, 0.0),
            new Beacon("", 43, 0.0, 0.0, 0.0),
            new Beacon("", 44, 0.0, 0.0, 0.0),

    };

    public Beacon(String mac, int id, double x, double y, double z) {
        this.mac = new MacAddress(mac);
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
