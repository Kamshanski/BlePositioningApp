package edu.tpu.dar.ruban.logic.measurement;

import edu.tpu.dar.ruban.logic.core.MacAddress;

import java.util.ArrayList;
import java.util.HashMap;

public class Storage {
    HashMap<MacAddress, MacAddress> macs = new HashMap<>(10);
    ArrayList<Moment[]> storage = new ArrayList<>(50);


    HashMap<MacAddress, ArrayList<Moment>> map = new HashMap<>();

    public void add(Moment[] moments) {
        storage.add(moments);
    }

    public Moment[] get(int index) {
        if (index < 0) {
            index += storage.size();
        }
        return storage.get(index);
    }

    // helps to keep only one instance of MacAddress per device
    public MacAddress getMac(String macStr) {
        MacAddress t = new MacAddress(macStr);
        MacAddress output = macs.get(t);
        if (output == null) {
            macs.put(t, t);
            output = t;
        }
        return output;
    }
}
