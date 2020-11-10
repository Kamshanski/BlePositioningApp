package edu.tpu.dar.ruban;


import java.util.HashMap;

import static edu.tpu.dar.ruban.ComPortConstants.PAYLOAD;

public class BlePositioningModel {
    private int slavesNum = 0;
    private int targetsNum = 0;
    private int targetsCapacity = 0;
    private MacAddress[] targetsMacs;
    private Storage storage;

    public BlePositioningModel() {
        storage = new Storage();
    }

    public void putNewMeasurements(long timeStart, long timeEnd, String payload) {
        MeasurementMoment.MeasurementMomentBuilder builder = new MeasurementMoment.MeasurementMomentBuilder(timeStart, timeEnd);
        HashMap<String, MeasurementMoment.MeasurementMomentBuilder> tempHm = new HashMap<>();
        String addrs = payload.substring(PAYLOAD.length);
        int startIndex = 0;

        for (int i = 0; i < 50; i++) {
            String macStr = addrs.substring(startIndex, startIndex+=12);
            String rssiStr = addrs.substring(startIndex, startIndex+=3);
            String idStr = addrs.substring(startIndex, startIndex+=2);

            MeasurementMoment.MeasurementMomentBuilder b = tempHm.computeIfAbsent(macStr, k -> new MeasurementMoment.MeasurementMomentBuilder(timeStart, timeEnd) );

            int rssi = Integer.parseInt(rssiStr, 16);
            int id = Integer.parseInt(idStr, 16);
            b.put(id, rssi);
        }

        tempHm.forEach( (mac, mmb) -> {
            storage.add(mac, mmb.build());
        });

    }

    public void setSlavesNum(int slavesNum) {
        this.slavesNum = slavesNum;
    }

    public void update(StringBuilder sizeBuf, StringBuilder capacityBuff, StringBuilder addrBuf) {
        try {
            targetsNum = Integer.parseInt(sizeBuf.toString());
            targetsCapacity = Integer.parseInt(capacityBuff.toString());
            parseTargetsMacs(addrBuf);
        } catch (NumberFormatException e) {
            U.nout("ERROR !!!!!!!!!!!!! " + e.getMessage());
        }
    }

    public void parseTargetsMacs(StringBuilder buf) {
        int bufSize = buf.length() / 17;
        assert bufSize == targetsNum;
        targetsMacs = new MacAddress[targetsNum];
        for (int i = 0; i < bufSize; i++) {
            targetsMacs[i] = new MacAddress(buf.substring(17*i, 17*(i+1)));
        }
    }

    public String targetsSetToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Targets{")
                .append("\n   Size: ").append(targetsNum)
                .append("\n   Capacity: ").append(targetsCapacity)
                .append("\n   Devices: ");
        for (int i = 0; i <targetsMacs.length; i++) {
            builder.append("\n      ").append(targetsMacs[i].macString);
        }
        builder.append("\n}TargetsSet");

        return builder.toString();
    }
}
