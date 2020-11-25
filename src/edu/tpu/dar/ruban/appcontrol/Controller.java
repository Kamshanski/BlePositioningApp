package edu.tpu.dar.ruban.appcontrol;


import edu.tpu.dar.ruban.comport.ComPortListener;
import edu.tpu.dar.ruban.comport.ComReader;
import edu.tpu.dar.ruban.logic.Model;
import edu.tpu.dar.ruban.logic.experiment.Experiment;
import edu.tpu.dar.ruban.logic.experiment.ExperimentStorageOfAll;
import edu.tpu.dar.ruban.utils.U;

import java.awt.event.ActionEvent;

import static edu.tpu.dar.ruban.appcontrol.AppInterface.EXP_IS_OFF;
import static edu.tpu.dar.ruban.appcontrol.AppInterface.EXP_IS_ON;

public class Controller {
    private PlotInterface plot;
    private AppInterface app;
    private ModelInterface model;

    private String comPortNumStr;
    private int comPortNum;
    private ComReader comReader;
    ComPortListener comPortListener;

    private Controller(AppInterface app) {
        this.app = app;
        this.model = Model.getClearInstance(false, this);
        comPortListener = new ComPortListenerImpl();
        comReader = new ComReader(comPortListener);
    }

    // Button Listeners
    public void onOpenConnectionClick(ActionEvent e) {
        String s = app.getComPortNum();
        // Check comport num to be non-negative integer
        try {
            int comPortNum = Integer.parseInt(s);
            if (comPortNum < 0) {
                throw new NumberFormatException("Number must be non-negative");
            }
            this.comPortNum = comPortNum;
        } catch (NumberFormatException ex) {
            println("Inserted ComPort Number isn't an Integer value. Insert int");
            printException(ex);
            return;
        }

        if (comReader.isOpened()) {
            if (comReader.close()) {
                app.setConnectionStatus(false);
                println("Disconnected Successfully");
            } else {
                println("Disconnection failed");
            }
        } else {
            if (comReader.open(comPortNum)) {
                app.setConnectionStatus(true);
                println("Connected Successfully");
            } else {
                println("Connection failed");
            }
        }
    }

    public void onStartStopExperimentClick(ActionEvent e) {
        Experiment experiment = model.getExperiment();
        if (experiment.isOn()) {
            experiment.stopExperiment();
            println("Experiment " + experiment.getExpName() + " started");
            app.setExperimentStatus(false);
        } else {
            String expName = app.getExpName();
            if (expName == null) {
                println("Experiment name is null!!!");
                return;
            }

            try {
                // Just check if data is Integer
                Integer.parseInt(expName);
            } catch (NumberFormatException ex) {
                println("Experiment name isn't a number");
                return;
            }

            app.setExperimentStatus(true);
            experiment.startExperiment(expName);
            println("--Experiment " + expName + " started");
        }
    }

    public void onRecordClick(ActionEvent e) {
        try {
            println("Experiment is saved as" + model.getExperiment().toExcel("onDemand"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRemoveClick(ActionEvent e) {
        String expName = app.getExpName();
        if (expName == null) {
            println("Experiment name is null!!!");
            return;
        }
        model.getExperiment().clear();

    }

    public void onDisplayExperimentsResultsClick(ActionEvent e) {
        StringBuilder builder = new StringBuilder();

        if (model.getExperiment() instanceof ExperimentStorageOfAll) {
            ((ExperimentStorageOfAll) model.getExperiment()).rssiSet.forEach((expName, data) -> {
                builder.append(expName).append(": ").append(data.size()).append("\n");
            });
        }

        app.printToExperimentsResultsLabel(builder.toString());
    }

    public void plotNewData(double[] data) {
        plot.putNewData(data);
    }

    private class ComPortListenerImpl implements ComPortListener {
        @Override
        public void onTerminal(String msg) {
            println(msg);
        }

        @Override
        public void onPayload(long timeStart, long timeEnd, long arrivalTime, String msg) {
            model.putNewMeasurements(timeEnd - timeStart, arrivalTime, msg);
            println("New payload arrived");
        }

        @Override
        public void onError(String msg) {
            println("ERROR OCCURRED!");
            String path = model.getExperiment().toExcel("onError");
            println("Experiment data is saved to" + path);
        }

        @Override
        public void onReady(String msg) {
            model = Model.getClearInstance(true, Controller.this);
            println("Device is On. All previous data was erased");
        }

        @Override
        public void onRestarting(String msg) {
            println("Device is restarting");
        }

        @Override
        public void onTargetsSet(String sizeStr, String capacityStr, String macStr) {
            if (model.setTargets(sizeStr, capacityStr, macStr)) {
                println(model.getTargetsString());
                app.setTargetsNum(model.getTargetsNum());
                app.setTargetsList(model.getTargetsSetString());
            } else {
                println("Received TargetSet is corrupted");
            }
        }

        @Override
        public void onSlavesNumber(String msg) {
            int slaveNum = -1;
            try {
                slaveNum = Integer.parseInt(msg);
                if (!model.setSlavesNum(slaveNum)) {
                    println("Received slave num is not correct!");
                    return;
                }
                model.setSlavesNum(slaveNum);
            } catch (NumberFormatException e) {
                U.nout("ERROR!!!!  " + e.getMessage());
            }
            app.setSlaveNum(slaveNum);
        }
    }

    public void setPlotInterface(PlotInterface plot) {
        this.plot = plot;
    }

    // Utils
    private void printException(Exception ex) {
        StackTraceElement[] stes = ex.getStackTrace();
        for (StackTraceElement ste : stes) {
            println(ste.toString());
        }
    }

    private void println(String s) {
        app.println(s);
    }

    //---- Singleton stuff
    private static Controller instance;
    public static Controller getInstance(AppInterface app) {
        if (instance == null) {
            synchronized (Controller.class) {
                if (instance == null) {
                    instance = new Controller(app);
                }
            }
        }
        return instance;
    }


    ///// trash

    // //Show serial Ports
    //            SerialPort[] ports = comReader.getPortsList();
    //            StringBuilder builder = new StringBuilder(150);
    //            for (int i = 0; i < ports.length; i++) {
    //                builder.append(i)
    //                        .append(") DescriptivePortName: ").append(ports[i].getDescriptivePortName())
    //                        .append(", PortDescription: ").append(ports[i].getPortDescription())
    //                        .append(", SystemPortName: ").append(ports[i].getSystemPortName())
    //                        .append(" |\n ");
    //            }
    //            statusLabel.setText(builder.toString());
}