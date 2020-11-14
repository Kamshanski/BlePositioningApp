package edu.tpu.dar.ruban.appcontrol;

public interface AppInterface {
    String getComPortNum();
    String getExpName();


    void setSlaveNum(int slaveNum);
    void setTargetsNum(int targetsNum);
    void setTargetsList(String targetsList);

    void setExperimentOn();
    void setExperimentOff();
    void printToExperimentsResultsLabel(String s);

    void printToProgramLog(String s);

    void println(String s);
}
