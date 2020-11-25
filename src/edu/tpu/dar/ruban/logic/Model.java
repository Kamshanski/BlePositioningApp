package edu.tpu.dar.ruban.logic;


import edu.tpu.dar.ruban.appcontrol.Controller;
import edu.tpu.dar.ruban.appcontrol.ModelInterface;
import edu.tpu.dar.ruban.logic.core.MacAddress;
import edu.tpu.dar.ruban.logic.experiment.Experiment;
import edu.tpu.dar.ruban.logic.experiment.ExperimentOfPositioning;
import edu.tpu.dar.ruban.logic.measurement.Moment;
import edu.tpu.dar.ruban.logic.measurement.Storage;
import edu.tpu.dar.ruban.utils.U;

import java.util.ArrayList;
import java.util.HashMap;

import static edu.tpu.dar.ruban.logic.core.Beacon.*;
import static edu.tpu.dar.ruban.comport.ComPortConstants.PAYLOAD;

public class Model implements ModelInterface {
    private Controller c;

    private int slavesNum = 0;
    private int targetsNum = 0;
    private int targetsCapacity = 0;
    private MacAddress[] targetsMacs;
    private Storage storage;
    private Positioning positioning;

    public ExperimentOfPositioning expStorage;


    private Model(Controller controller) {
        c = controller;
        storage = new Storage();
        positioning = new Positioning();

        //expStorage = new ExperimentStorageOfAll(BEACONS.get(TARGET).mac.macString, BEACONS.get(SOURCE).id);
        expStorage = new ExperimentOfPositioning(BEACONS.get(TARGET).mac, BEACONS.get(SOURCE).id);
    }

    @Override
    public void putNewMeasurements(long duration, long arrivalTime, String payload) {
        String addrs = payload.substring(PAYLOAD.length);   // to cut off all noisy data

        ArrayList<Double> newPlotData = new ArrayList<>(15);

        HashMap<MacAddress, Moment> moments = new HashMap<>(10);

        for (int i = 0, startIndex = 0; i < 50; i++) {
            // dynamically go to next record in payload
            String macStr = addrs.substring(startIndex, startIndex+=12);
            String rssiStr = addrs.substring(startIndex, startIndex+=3);
            String beaconIdStr = addrs.substring(startIndex, startIndex+=2);

            // get moment for selected mac or create new
            MacAddress mac = storage.getMac(macStr);
            Moment moment = moments.computeIfAbsent(mac,
                    key -> new Moment(arrivalTime, arrivalTime+duration, key));

            // Parse measurement and save it
            int rssi = Integer.parseInt(rssiStr, 16);
            Integer beaconId = Integer.valueOf(beaconIdStr, 16);
            moment.addMeasurement(rssi,
                                  BEACONS.get(beaconId));
                                                //
            if (beaconId == expStorage.sourceId
                    //&& mac.equals(new MacAddress("5cea6a1d4842"))
                    && mac.equals(expStorage.targetMac)
            ) {
                newPlotData.add((double) rssi);
            }

            // experiment (measure rssi(pho) function
//            if (expStorage.inExperiment(macStr, beaconId) && expStorage.isOn()) {
//                expStorage.add(rssi);
//            }
        }




        storage.add(moments.values().toArray(new Moment[0]));

        if (expStorage.isOn()) {
            Moment[] ms = storage.get(-1);
            for (Moment m : ms) {
                if (expStorage.inExperiment(m.getMacAddress())) {
                    try {
                        expStorage.estimate(m);
                    } catch (Exception ex) {
                        U.nout("error, estimate");
                        U.nout(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        }

        try {
            double[] d = new double[newPlotData.size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = newPlotData.get(i);
            }
            U.invertArray(d);
            c.plotNewData(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        int a = 4;
        // запустить здесь estimation для каждого MAC в отдельном потоке
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
    public Experiment getExperiment() {
        return expStorage;
    }


    private static Model instance;
    public static Model getClearInstance(boolean clear, Controller controller) {
        if (instance == null || clear) {
            synchronized (Model.class) {
                if (instance == null) {
                    instance = new Model(controller);
                } else if (clear) {
                    instance.inUse = false;
                    instance = new Model(controller);
                }
            }
        }
        return instance;
    }
}
