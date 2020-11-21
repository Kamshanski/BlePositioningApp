package edu.tpu.dar.ruban.appcontrol;

import edu.tpu.dar.ruban.logic.experiment.Experiment;
import edu.tpu.dar.ruban.logic.experiment.ExperimentStorageOfAll;

public interface ModelInterface {
    Experiment getExperiment();

    void putNewMeasurements(long duration, long arrivalTime, String msg);

    boolean setTargets(String sizeStr, String capacityStr, String macStr);
    boolean setSlavesNum(int slaveNum);
    String getTargetsString();
    int getTargetsNum();
    String getTargetsSetString();
}
