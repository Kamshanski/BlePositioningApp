package edu.tpu.dar.ruban.view;

import edu.tpu.dar.ruban.appcontrol.AppInterface;
import edu.tpu.dar.ruban.appcontrol.Controller;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame{
    private JPanel panel1;
    private JButton btnExperiment;
    private JButton button99;
    private JButton btnPositioning;
    private JPanel trackingCard;
    private JPanel expCard;
    private JPanel zapasCard;
    private JButton btnConnect;
    private JLabel lblSlavesNum;
    private JLabel lblTargetsNum;
    private JLabel lblExperimentOnOff;
    private JButton btnStartStopExperiment;
    private JButton btnRecordExperiment;
    private JTextArea txProgramLog;
    private JButton btnRemoveRecords;
    private JButton btnShowResults;
    private JTextArea txExperimentName;
    private JComboBox cmbComPortNum;
    private JTextArea txExperimentLog;
    private JTextArea txComPortLog;
    private JPanel cardsHolder;
    private JTextArea txTargetsSet;

    private CardLayout cardLayout;

    private AppInterfaceImpl app;
    private Controller controller;

    public Application() {
        app = new AppInterfaceImpl();

        controller = Controller.getInstance(app);
        setContentPane(panel1);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createUIComponents();
    }

    private void createUIComponents() {
        cardLayout = (CardLayout) cardsHolder.getLayout();
        setListeners();
    }

    private void setListeners() {
        btnConnect.addActionListener(controller::onOpenConnectionClick);

        // CardLayout switches
        btnExperiment.addActionListener(event -> cardLayout.show(cardsHolder, expCard.getName()));
        btnPositioning.addActionListener(event -> cardLayout.show(cardsHolder, trackingCard.getName()));
        button99.addActionListener(event -> cardLayout.show(cardsHolder, zapasCard.getName()));

        // ExpCard
        btnStartStopExperiment.addActionListener(controller::onStartStopExperimentClick);
        btnRecordExperiment.addActionListener(controller::onRecordClick);
        btnRemoveRecords.addActionListener(controller::onRemoveClick);
        btnShowResults.addActionListener(controller::onDisplayExperimentsResultsClick);
    }

    private class AppInterfaceImpl implements AppInterface{

        @Override
        public String getComPortNum() {
            return (String) cmbComPortNum.getSelectedItem();
        }

        @Override
        public String getExpName() {
            return txExperimentName.getText();
        }

        @Override
        public void setSlaveNum(int slaveNum) {
            lblSlavesNum.setText(Integer.toString(slaveNum));
        }

        @Override
        public void setTargetsNum(int targetsNum) {
            lblTargetsNum.setText(Integer.toString(targetsNum));
        }

        @Override
        public void setTargetsList(String targetsList) {
            txTargetsSet.setText(targetsList);
        }

        @Override
        public void setExperimentOn() {
            btnConnect.setText("Stop Experiment");
            lblExperimentOnOff.setText("On");
        }

        @Override
        public void setExperimentOff() {
            btnConnect.setText("Start Experiment");
            lblExperimentOnOff.setText("Off");
        }

        @Override
        public void printToExperimentsResultsLabel(String s) {
            txExperimentLog.setText(s);
        }

        @Override
        public void printToProgramLog(String s) {
            txProgramLog.append(s);
            txProgramLog.append("\n");
        }

        @Override
        public void println(String s) {
            txComPortLog.append(s);
            txComPortLog.append("\n");
        }
    }
}
