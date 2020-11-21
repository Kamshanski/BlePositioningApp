package edu.tpu.dar.ruban.appcontrol;

public interface AppInterface {
    public static final int ERROR                   = 0;
    public static final int EXP_IS_ON               = 1;
    public static final int EXP_IS_OFF              = 2;

    String getComPortNum();
    String getExpName();


    void setConnectionStatus(boolean connected);


    void setSlaveNum(int slaveNum);
    void setTargetsNum(int targetsNum);
    void setTargetsList(String targetsList);

    void setExperimentStatus(boolean isOn);
    void printToExperimentsResultsLabel(String s);

    void printToProgramLog(String s);

    void println(String s);
}
