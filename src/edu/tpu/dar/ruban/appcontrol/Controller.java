package edu.tpu.dar.ruban.appcontrol;


import edu.tpu.dar.ruban.comport.ComPortListener;
import edu.tpu.dar.ruban.comport.ComReader;
import edu.tpu.dar.ruban.logic.BlePositioningModel;
import edu.tpu.dar.ruban.logic.ExperimentStorageOfAll;
import edu.tpu.dar.ruban.utils.ArrayListInt;
import edu.tpu.dar.ruban.utils.U;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller {
    private AppInterface app;
    private ModelInterface model;

    private String comPortNumStr;
    private int comPortNum;
    private ComReader comReader;
    ComPortListener comPortListener;

    private Controller(AppInterface app) {
        this.app = app;
        this.model = BlePositioningModel.getClearInstance(false);
        comPortListener = new ComPortListenerImpl();
        comReader = new ComReader(comPortListener);

    }

    // Button Listeners
    public void onOpenConnectionClick(ActionEvent e) {
        String s = app.getComPortNum();
        try {
            comPortNum = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            println("Inserted ComPort Number isn't an Integer value. Insert int");
            printException(ex);
            return;
        }

        if (comPortNum > -1) {
            if (comReader.open(comPortNum)) {
                println("Connected Successfully");
            } else {
                println("Connection failed");
            }
        }
    }

    public void onStartStopExperimentClick(ActionEvent e) {
        ExperimentStorageOfAll experiment = model.getExperiment();
        if (experiment.isOn()) {
            experiment.stopExperiment();
            app.setExperimentOff();
            println("Experiment " + experiment.currentExpName + " started");
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

            experiment.setExperimentName(expName);
            experiment.startExperiment();
            app.setExperimentOn();
            println("--Experiment " + expName + " started");
        }
    }

    public void onRecordClick(ActionEvent e) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd__HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        Path expPath = Paths.get("C:\\Users\\epr2\\Desktop\\Exp_results__" + dtf.format(now) + ".txt");
        try {
            Files.write(expPath, model.getExperiment().toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (IOException ex) {
            println("FILE WRITE FAIL");
            printException(ex);
        }

        model.getExperiment().toExcel("onDemand");
    }

    public void onRemoveClick(ActionEvent e) {
        String expName = app.getExpName();
        if (expName == null) {
            println("Experiment name is null!!!");
            return;
        }
        model.getExperiment().rssiSet.computeIfPresent(expName, (k, v) -> new ArrayListInt());

    }

    public void onDisplayExperimentsResultsClick(ActionEvent e) {
        StringBuilder builder = new StringBuilder();
        model.getExperiment().rssiSet.forEach((expName, data) -> {
            builder.append(expName).append(": ").append(data.size()).append("\n");
        });
        app.printToExperimentsResultsLabel(builder.toString());
    }

    private class ComPortListenerImpl implements ComPortListener {
        @Override
        public void onTerminal(String msg) {
            println(msg);
        }

        @Override
        public void onPayload(long timeStart, long timeEnd, String msg) {
            model.putNewMeasurements(timeStart, timeEnd, msg);
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
            model = BlePositioningModel.getClearInstance(true);
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