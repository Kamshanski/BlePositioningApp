package edu.tpu.dar.ruban.view;

import edu.tpu.dar.ruban.appcontrol.AppInterface;
import edu.tpu.dar.ruban.appcontrol.Controller;
import edu.tpu.dar.ruban.utils.U;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

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
    private JScrollPane scrollComPortLog;

    PaintPanel paintPanel;
    private CardLayout cardLayout;

    private AppInterfaceImpl app;
    private Controller controller;

    public Application() {
        initApplication();

        setContentPane(panel1);

        createUIComponents();

        setVisible(true);

    }

    public void initApplication() {
        app = new AppInterfaceImpl();

        setSize(900, 700);
        setTitle("BLE Positioning App");
        try {
            setIconImage(ImageIO.read(new File("./res/images/Bluetooth.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        controller = Controller.getInstance(app);
    }

    private void createUIComponents() {
        cardLayout = (CardLayout) cardsHolder.getLayout();

        paintPanel = new PaintPanel();
        trackingCard.add(paintPanel);

        cmbComPortNum.setSelectedIndex(3);

        setListeners();
    }

    private void setListeners() {
        btnConnect.addActionListener(controller::onOpenConnectionClick);

        // CardLayout switches
        btnExperiment.addActionListener(event -> cardLayout.show(cardsHolder, expCard.getName()));
        btnPositioning.addActionListener(event -> cardLayout.show(cardsHolder, trackingCard.getName()));
        button99.addActionListener(event ->
            {
                //cardLayout.show(cardsHolder, zapasCard.getName());
                paintPanel.mydraw();
            }
        );

        // ExpCard
        btnStartStopExperiment.addActionListener(controller::onStartStopExperimentClick);
        btnRecordExperiment.addActionListener(controller::onRecordClick);
        btnRemoveRecords.addActionListener(controller::onRemoveClick);
        btnShowResults.addActionListener(controller::onDisplayExperimentsResultsClick);

        // TrackCard
//        pField.setPreferredSize(new Dimension(500, 500));
//        pField.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                Container pd = e.getComponent().getParent();
//                int w = pd.getWidth();
//                int h = pd.getHeight();
//                Dimension d = e.getComponent().getPreferredSize();
//                d.setSize(h, h);
//                e.getComponent().setPreferredSize(d);
//                U.nout("new");
//
////                if (w != h) {
////                    e.getComponent().setSize(h, h);
////                }
//            }
//        });
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
        public void setConnectionStatus(boolean connected) {
            if (connected) {
                btnConnect.setText("Disconnect from Main");
            } else {
                btnConnect.setText("Connect to Main");
            }
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
        public void setExperimentStatus(boolean isOn) {
            if (isOn) {
                btnStartStopExperiment.setText("Start Experiment");
                lblExperimentOnOff.setText("Off");
            }
            else {
                btnStartStopExperiment.setText("Stop Experiment");
                lblExperimentOnOff.setText("On");
            }
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
            JScrollBar vBar = scrollComPortLog.getVerticalScrollBar();
            vBar.setValue(vBar.getMaximum());
        }
    }
}
