package edu.tpu.dar.ruban.logic.experiment;

public interface Experiment {

    void stopExperiment();
    void startExperiment(String expName);
    boolean isOn();

    String getExpName();
    void clear();

    String toExcel(String tag);

}
