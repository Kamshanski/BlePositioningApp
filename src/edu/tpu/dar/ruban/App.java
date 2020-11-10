package edu.tpu.dar.ruban;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App implements ComPortListener {
    private BlePositioningModel model;
    private ComRead comReader;
    ExecutorService calcThread = Executors.newSingleThreadExecutor();
    
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JTextArea textArea;

    public App(){
        prepareGUI();
        comReader = new ComRead(3, this);
        model = new BlePositioningModel();
    }

    private void prepareGUI(){
        mainFrame = new JFrame("Java SWING Examples");
        mainFrame.setSize(1200,8000);
        mainFrame.setLayout(new GridLayout(4, 1));

        headerLabel = new JLabel("",JLabel.CENTER );
        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.add(textArea);
        mainFrame.setVisible(true);
    }
    public void showEventDemo(){
        headerLabel.setText("Control in action: Button");

        JButton btnOpen = new JButton("Open");
        JButton submitButton = new JButton("Submit");
        JButton viewAllPorts = new JButton("View All Ports");

        btnOpen.setActionCommand("open");
        submitButton.setActionCommand("Submit");
        viewAllPorts.setActionCommand("Cancel");

        btnOpen.addActionListener(actionEvent -> {
            comReader.open();
        });
        submitButton.addActionListener(actionEvent -> {
            statusLabel.setText("Submit Button clicked.");
        });
        viewAllPorts.addActionListener(actionEvent -> {
            SerialPort[] ports = comReader.getPortsList();
            StringBuilder builder = new StringBuilder(150);
            for (int i = 0; i < ports.length; i++) {
                builder.append(i)
                        .append(") DescriptivePortName: ").append(ports[i].getDescriptivePortName())
                        .append(", PortDescription: ").append(ports[i].getPortDescription())
                        .append(", SystemPortName: ").append(ports[i].getSystemPortName())
                        .append(" |\n ");
            }
            statusLabel.setText(builder.toString());
        });


        controlPanel.add(btnOpen);
        controlPanel.add(submitButton);
        controlPanel.add(viewAllPorts);

        mainFrame.setVisible(true);
    }

    @Override
    public void onPayload(long timeStart, long timeEnd, String msg) {
        model.putNewMeasurements(timeStart, timeEnd, msg);
        textArea.append(msg);
    }

    @Override
    public void onError(String msg) {
        U.nout(msg);
        textArea.append(msg);
    }

    @Override
    public void onReady(String msg) {
        U.nout(msg);
        model = new BlePositioningModel();
        textArea.append(msg);
    }

    @Override
    public void onRestarting(String msg) {
        U.nout(msg);
        model = null;
        textArea.append(msg);
    }

    @Override
    public void onTargetsSet(StringBuilder sizeBuf, StringBuilder capacityBuff, StringBuilder addrBuf) {
        model.update(sizeBuf, capacityBuff, addrBuf);
        textArea.append(model.targetsSetToString());
    }

    @Override
    public void onSlavesNumber(String msg) {
        try {
            model.setSlavesNum(Integer.parseInt(msg));
        } catch (NumberFormatException e) {
            U.nout("ERROR!!!!  " + e.getMessage());
        }

    }
}
