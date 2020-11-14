//package edu.tpu.dar.ruban;
//
//import edu.tpu.dar.ruban.appcontrol.AppInterface;
//import edu.tpu.dar.ruban.appcontrol.Controller;
//import edu.tpu.dar.ruban.comport.ComPortListener;
//import edu.tpu.dar.ruban.comport.ComReader;
//import edu.tpu.dar.ruban.logic.BlePositioningModel;
//import edu.tpu.dar.ruban.utils.ArrayListInt;
//import edu.tpu.dar.ruban.utils.U;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class App implements AppInterface {
//    Controller controller;
//    String targetsList;
//    int slaveNum = 0;
//    ExecutorService calcThread = Executors.newSingleThreadExecutor();
//
//    private JFrame mainFrame;
//    private JTextArea headerLabel;
//    private JTextArea statusLabel;
//    private JPanel controlPanel;
//    private JTextArea terminal;
//    private JLabel experimentStatusLabel = new JLabel("OFF");
//    private JLabel slavesStatusLabel = new JLabel("Slaves: 0");
////    private JLabel targetsStatusLabel = new JLabel("Targets: 0");
//    private JTextField textField = new JTextField("", 50);
//    private JTextField txtComPort = new JTextField("", 3);
//    JButton btnOpen;
//    JButton btnStop;
//    JButton btnStart;
//    JButton btnRecord;
//    JButton btnRemove;
//    JButton btnShowResults;
//
//    public App(){
//        controller = Controller.getInstance(this);
//        prepareGUI();
//    }
//
//    @Override
//    public String getComPortNum() {
//        return txtComPort.getText();
//    }
//
//    @Override
//    public String getExpName() {
//        return textField.getText();
//    }
//
//    @Override
//    public void setSlaveNum(int slaveNum) {
//        this.slaveNum = slaveNum;
//        updateTargetsAndSlavesLabel();
//    }
//
//    @Override
//    public void setTargetsList(String targetsList) {
//        this.targetsList = targetsList;
//        updateTargetsAndSlavesLabel();
//    }
//
//    @Override
//    public void setExperimentOn() {
//        experimentStatusLabel.setText("Experiment: ON");
//        experimentStatusLabel.setBackground(new Color(0.0f, 1.0f, 0.0f));
//    }
//
//    @Override
//    public void setExperimentOff() {
//        experimentStatusLabel.setBackground(new Color(0.0f, 0.0f, 1.0f));
//        experimentStatusLabel.setText("Experiment: OFF");
//    }
//
//    private void updateTargetsAndSlavesLabel() {
//        headerLabel.setText("Slaves: " + slaveNum + "\n" + targetsList);
//    }
//
//    @Override
//    public void printToExperimentsResultsLabel(String s) {
//        statusLabel.setText(s);
//    }
//
//    @Override
//    public void println(String s) {
//        terminal.append(s);
//        terminal.append("\n");
//    }
//
//    private void prepareGUI(){
//        mainFrame = new JFrame("Java SWING Examples");
//        mainFrame.setSize(900,1050);
//        mainFrame.setLayout(new GridLayout(6, 1));
//
//        headerLabel = new JTextArea(7,JLabel.CENTER );
//        statusLabel = new JTextArea(30, JLabel.RIGHT);
//        statusLabel.setSize(350,100);
//
//        mainFrame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent windowEvent){
//                System.exit(0);
//            }
//        });
//        controlPanel = new JPanel();
//        controlPanel.setLayout(new FlowLayout());
//
//        terminal = new JTextArea();
//        terminal.setEditable ( false ); // set textArea non-editable
//        JScrollPane scroll = new JScrollPane (terminal);
//        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
//
//        terminal.setEditable(true);
//
//
//        mainFrame.add(experimentStatusLabel);
//        mainFrame.add(slavesStatusLabel);
//        mainFrame.add(headerLabel);
//        mainFrame.add(controlPanel);
//        mainFrame.add(statusLabel);
//        mainFrame.add(scroll);
//        mainFrame.setVisible(true);
//    }
//    public void showEventDemo(){
//
//        btnOpen = new JButton("Open");
//        btnStop = new JButton("Stop Experiment");
//        btnStart = new JButton("Start Experiment");
//        btnRecord = new JButton("Record experiment in excel");
//        btnRemove = new JButton("Remove records");
//        btnShowResults = new JButton("Show results");
//
//
//        controlPanel.add(btnOpen);
//        controlPanel.add(btnStop);
//        controlPanel.add(btnStart);
//        controlPanel.add(btnRecord);
//        controlPanel.add(btnRemove);
//        controlPanel.add(btnShowResults);
//        controlPanel.add(textField);
//        controlPanel.add(txtComPort);
//
//        mainFrame.setVisible(true);
//    }
//}
