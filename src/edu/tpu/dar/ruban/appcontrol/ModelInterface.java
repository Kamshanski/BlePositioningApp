package edu.tpu.dar.ruban.appcontrol;

import edu.tpu.dar.ruban.logic.ExperimentStorageOfAll;

public interface ModelInterface {
    ExperimentStorageOfAll getExperiment();

    void putNewMeasurements(long timeStart, long timeEnd, String msg);

    boolean setTargets(String sizeStr, String capacityStr, String macStr);
    boolean setSlavesNum(int slaveNum);
    String getTargetsString();
    int getTargetsNum();
    String getTargetsSetString();
}
