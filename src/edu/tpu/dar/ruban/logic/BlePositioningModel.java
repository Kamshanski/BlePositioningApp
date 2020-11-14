package edu.tpu.dar.ruban.logic;


import edu.tpu.dar.ruban.appcontrol.ModelInterface;
import edu.tpu.dar.ruban.utils.U;

import java.util.HashMap;

import static edu.tpu.dar.ruban.logic.Beacon.*;
import static edu.tpu.dar.ruban.comport.ComPortConstants.PAYLOAD;

public class BlePositioningModel implements ModelInterface {
    private int slavesNum = 0;
    private int targetsNum = 0;
    private int targetsCapacity = 0;
    private MacAddress[] targetsMacs;
    private Storage storage;
    public ExperimentStorageOfAll expStorage;

    private BlePositioningModel() {
        storage = new Storage();
        expStorage = new ExperimentStorageOfAll(BEACONS[TARGET].mac.macString, BEACONS[SOURCE].id);
    }

    @Override
    public void putNewMeasurements(long timeStart, long timeEnd, String payload) {
        MeasurementMoment.MeasurementMomentBuilder builder = new MeasurementMoment.MeasurementMomentBuilder(timeStart, timeEnd);
        HashMap<String, MeasurementMoment.MeasurementMomentBuilder> tempHm = new HashMap<>();
        String addrs = payload.substring(PAYLOAD.length);
        int startIndex = 0;

        for (int i = 0; i < 50; i++) {
            String macStr = addrs.substring(startIndex, startIndex+=12);
            String rssiStr = addrs.substring(startIndex, startIndex+=3);
            String idStr = addrs.substring(startIndex, startIndex+=2);

            MeasurementMoment.MeasurementMomentBuilder momentBuilder = tempHm.computeIfAbsent(macStr, k -> new MeasurementMoment.MeasurementMomentBuilder(timeStart, timeEnd) );

            int rssi = Integer.parseInt(rssiStr, 16);
            int id = Integer.parseInt(idStr, 16);

            if (expStorage.inExperiment(macStr, id) && expStorage.isOn()) {
                expStorage.add(rssi);
            }

            momentBuilder.put(id, rssi);
        }

        tempHm.forEach( (mac, mmb) -> {
            storage.add(mac, mmb.build());
        });

    }
    @Override
    public boolean setSlavesNum(int slavesNum) {
        if (slavesNum >= 0) {
            this.slavesNum = slavesNum;
            return true;
        }
        return false;
    }

    @Override
    public boolean setTargets(String sizeStr, String capacityStr, String macStr) {
        try {
            targetsNum = Integer.parseInt(sizeStr);
            targetsCapacity = Integer.parseInt(capacityStr);
            parseTargetsMacs(macStr);
            return true;
        } catch (NumberFormatException e) {
            U.nout("ERROR !!!!!!!!!!!!! " + e.getMessage());
            return false;
        }
    }

    public void parseTargetsMacs(String buf) {
        int bufSize = buf.length() / 17;
        assert bufSize == targetsNum;
        targetsMacs = new MacAddress[targetsNum];
        for (int i = 0; i < bufSize; i++) {
            targetsMacs[i] = new MacAddress(buf.substring(17*i, 17*(i+1)));
        }
    }

    @Override
    public String getTargetsString() {
        StringBuilder builder = new StringBuilder();
        if (targetsNum > 0) {
            builder.append("Targets{")
                    .append("\n   Size: ").append(targetsNum)
                    .append("\n   Capacity: ").append(targetsCapacity)
                    .append("\n   Devices: ");
            for (int i = 0; i < targetsMacs.length; i++) {
                builder.append("\n      ").append(targetsMacs[i].macString);
            }
            builder.append("\n}TargetsSet");
        }
        else {
            builder.append("Targets{ 0 }");
        }

        return builder.toString();
    }

    @Override
    public String getTargetsSetString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < targetsNum; i++) {
            builder.append(i).append(") ").append(targetsMacs[i].macString);
        }
        return builder.toString();
    }

    @Override
    public int getTargetsNum() {
        return targetsNum;
    }

    // Singleton
    private boolean inUse = true;

    @Override
    public ExperimentStorageOfAll getExperiment() {
        return expStorage;
    }

    private static BlePositioningModel instance;
    public static BlePositioningModel getClearInstance(boolean clear) {
        if (instance == null || clear) {
            synchronized (BlePositioningModel.class) {
                if (instance == null) {
                    instance = new BlePositioningModel();
                } else if (clear) {
                    instance.inUse = false;
                    instance = new BlePositioningModel();
                }
            }
        }
        return instance;
    }
}
