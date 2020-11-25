package edu.tpu.dar.ruban.view;

import edu.tpu.dar.ruban.appcontrol.AppInterface;
import edu.tpu.dar.ruban.appcontrol.Controller;
import edu.tpu.dar.ruban.appcontrol.PlotInterface;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Application extends JFrame{
    private JPanel panel1;
    private JButton btnExperiment;
    private JButton btnPlot;
    private JButton btnPositioning;
    private JPanel trackingCard;
    private JPanel expCard;
    private JPanel plotCard;
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
    XYChart chart;
    XChartPanel<XYChart> plotPanel;
    private CardLayout cardLayout;

    private PlotInterfaceImpl plotInterface;
    private AppInterfaceImpl app;
    private Controller controller;

    public Application() {
        initApplication();

        setContentPane(panel1);

        createUIComponents();

        setVisible(true);

    }

    public void initApplication() {
        chart = new XYChartBuilder()
                .xAxisTitle("points")
                .yAxisTitle("rssi")
                .build();

        app = new AppInterfaceImpl();
        plotInterface = new PlotInterfaceImpl(200);

        setSize(900, 700);
        setTitle("BLE Positioning App");
        try {
            setIconImage(ImageIO.read(new File("./res/images/Bluetooth.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        controller = Controller.getInstance(app);
        controller.setPlotInterface(plotInterface);
    }

    private void createUIComponents() {
        cardLayout = (CardLayout) cardsHolder.getLayout();

        paintPanel = new PaintPanel();
        trackingCard.add(paintPanel);


        plotPanel = new XChartPanel<>(chart);
        plotCard.add(plotPanel);

        cmbComPortNum.setSelectedIndex(3);

        setListeners();
    }

    private void setListeners() {
        btnConnect.addActionListener(controller::onOpenConnectionClick);

        // CardLayout switches
        btnExperiment.addActionListener(event -> cardLayout.show(cardsHolder, expCard.getName()));
        btnPositioning.addActionListener(event -> cardLayout.show(cardsHolder, trackingCard.getName()));
        btnPlot.addActionListener(event -> cardLayout.show(cardsHolder, plotCard.getName()));

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

    private class PlotInterfaceImpl implements PlotInterface {
        double[] x;
        double[] y, yt;
        ArrayList<Integer> delimiters = new ArrayList<>();
        int N;
        String SERIES_NAME = "0";
        String UPPER_LIMIT_SERIES_NAME = "Up";
        String LOWER_LIMIT_SERIES_NAME = "Low";

        public PlotInterfaceImpl(int N) {
            this.N = N;
            chart.addSeries(SERIES_NAME, new int[] {10, 20}, new int[] {-10, -20});
            chart.addSeries(UPPER_LIMIT_SERIES_NAME, new int[] {0, 200}, new int[] {-20, -20});
            chart.addSeries(LOWER_LIMIT_SERIES_NAME, new int[] {0, 200}, new int[] {-90, -90});

            x = new double[N];
            for (int i = 0; i < N; i++) {
                x[i] = i;
            }

            y = new double[N];
            yt = new double[N];
            Arrays.fill(y, 0);
        }

        @Override
        public void plot() {
            chart.updateXYSeries(SERIES_NAME, x, y, null);
            plotPanel.repaint();
        }

        @Override
        public void putNewData(double[] y) {
            int size = y.length;
            // add new data to plot using temp variable
            System.arraycopy(y, 0, yt, 0, size);
            System.arraycopy(this.y, 0, yt, size, N-size);

            double[] temp = this.y;
            this.y = yt;
            yt = temp;

            // add and remove all unnecessary delimiters
            delimiters.add(0);
            for (int i = 0; i < delimiters.size(); i++) {
                delimiters.set(i, delimiters.get(i) + size);
            }
            delimiters.removeIf(i -> i > N);

            plot();
        }
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
