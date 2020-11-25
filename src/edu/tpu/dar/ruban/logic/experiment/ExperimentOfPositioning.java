package edu.tpu.dar.ruban.logic.experiment;

import edu.tpu.dar.ruban.logic.core.MacAddress;
import edu.tpu.dar.ruban.logic.distancefunctions.Atanh;
import edu.tpu.dar.ruban.logic.distancefunctions.RssiFunction;
import edu.tpu.dar.ruban.logic.filters.Hampel;
import edu.tpu.dar.ruban.logic.filters.Kalman;
import edu.tpu.dar.ruban.logic.filters.KaufmansAdaptiveMovingAverage;
import edu.tpu.dar.ruban.logic.filters.MeanAndVariance;
import edu.tpu.dar.ruban.logic.measurement.Measurement;
import edu.tpu.dar.ruban.logic.measurement.Moment;
import edu.tpu.dar.ruban.utils.ArrayListDouble;
import edu.tpu.dar.ruban.utils.ArrayListInt;
import edu.tpu.dar.ruban.utils.U;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ExperimentOfPositioning implements Experiment{
    public ArrayListDouble aRaw, aHampel;
    public ArrayListDouble aKalman, aKaufman, aMean;
    public ArrayListDouble aKalmanHampel, aKaufmanHampel, aMeanHampel;
    public ArrayListDouble aDistance;
    Hampel hampel;
    ArrayList<HampelFilterArrayMetadata> hfams = new ArrayList<>(10);
    Kalman kalman, kalmanHampel;
    KaufmansAdaptiveMovingAverage kaufman, kaufmanHampel;
    MeanAndVariance mean, meanHampel;

    RssiFunction f = new Atanh();

    public String currentExpName;
    public MacAddress targetMac;
    public int sourceId;
    boolean experimentAllowed = false;

    public ExperimentOfPositioning(MacAddress targetMac, int sourceId)  {
        this.targetMac = targetMac;
        this.sourceId = sourceId;

        clear();
    }

    @Override
    public void clear() {
        aRaw                = new ArrayListDouble(1000, 150);
        aKalman             = new ArrayListDouble(1000, 150);
        aKaufman            = new ArrayListDouble(1000, 150);
        aHampel             = new ArrayListDouble(1000, 150);
        aKalmanHampel       = new ArrayListDouble(1000, 150);
        aKaufmanHampel      = new ArrayListDouble(1000, 150);
        aDistance           = new ArrayListDouble(1000, 150);

        hampel              = new Hampel(0.6);
        kalman              = new Kalman(1.0, 10.0);
        kaufman             = new KaufmansAdaptiveMovingAverage();
        kalmanHampel        = new Kalman(1.0, 10.0);
        kaufmanHampel       = new KaufmansAdaptiveMovingAverage();

//        aMean               = new ArrayListDouble(20, 20);
//        aMeanHampel         = new ArrayListDouble(20, 20);
//
//        for (double i = 0.1; i < 3; i+=0.1) {
//            hfams.add(new HampelFilterArrayMetadata(i));
//        }
//
//        mean                = new MeanAndVariance();
//        meanHampel          = new MeanAndVariance();
    }

    public void estimate(Moment moment) {
        Measurement[] ms = moment.getMeasurements();
        for (Measurement m : ms) {  // Clean filters and Hampel
            if (m.beacon.id == sourceId) {  // skip unnecessary data from other beacons
                continue;
            }

            int rssi = m.rssi;

            hampel.put(rssi);
            kalman.put(rssi);
            kaufman.put(rssi);

            aRaw.add(rssi);
            aKalman.add(kalman.calculate().first);
            aKaufman.add(kaufman.calculate());
//            for (int i = 0; i < hfams.size(); i++) {
//                hfams.get(i).hampel.put(rssi);
//            }
//            hampel.put(rssi);
//
//            // fill filters with raw data
//            kalman.put(rssi);
//            kaufman.put(rssi);
//            mean.put(rssi);
//
//            // save data from "one-by-one" continuous (evaluational?) filters
//            aKalman.add(kalman.calculate().first);
//            aKaufman.add(kaufman.calculate());
        }

        int[] hampelComputed = hampel.calculate();
        for (int j : hampelComputed) {
            kalmanHampel.put(j);
            kaufmanHampel.put(j);

            aHampel.add(j);
            aKalmanHampel.add(kalmanHampel.calculate().first);
            aKaufmanHampel.add(kaufmanHampel.calculate());
            aDistance.add(f.of(aKalman.get(-1)));
        }
//        int[] hampelRssi = hampel.calculate();
//
//        double meanCalculated = mean.calculate().first;
//
//        for (int r : hampelRssi) {   // filters with Hampel
//            // fill previous arrays
//            aMean.add(meanCalculated);
//            aHampel.add(r);
//
//            // fill filters with hampel output
//            kalmanHampel.put(r);
//            kaufmanHampel.put(r);
//            meanHampel.put(r);
//
//            aKalmanHampel.add(kalmanHampel.calculate().first);
//            aKaufmanHampel.add(kaufmanHampel.calculate());
//        }
//
//        meanCalculated = meanHampel.calculate().first;
//        for (int i = 0; i < hampelRssi.length; i++) {
//            aMeanHampel.add(meanCalculated);    // mean + hampel is not the best idea
//        }
    }

    @Override
    public void startExperiment(String expName) {
        currentExpName = expName;
        experimentAllowed = true;
    }
    @Override
    public void stopExperiment() {
        experimentAllowed = false;
    }
    @Override
    public boolean isOn() {
        return experimentAllowed;
    }
    @Override
    public String getExpName() {
        return currentExpName;
    }

    public boolean inExperiment(MacAddress mac) {
        return mac.equals(this.targetMac);
    }


    @Override
    public String toExcel(String tag) {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = (HSSFSheet) wb.createSheet("Experiments");

        // create empty rows and labels
        int emptyRowsNum = 10;
        for (int i = 0; i < emptyRowsNum; i++) {
            sheet.createRow(i);
        }
        Row r = sheet.getRow(9);

        r.createCell(1, CellType.STRING).setCellValue("i");
        r.createCell(2, CellType.STRING).setCellValue("Raw");
        r.createCell(3, CellType.STRING).setCellValue("Hampel");
        r.createCell(4, CellType.STRING).setCellValue("Kalman");
        r.createCell(5, CellType.STRING).setCellValue("Kaufman");
        r.createCell(6, CellType.STRING).setCellValue("Kalman-Hampel");
        r.createCell(7, CellType.STRING).setCellValue("Kaufman-Hampel");
        r.createCell(8, CellType.STRING).setCellValue("Distance");
//        r.createCell(6, CellType.STRING).setCellValue("Mean");
//        r.createCell(9, CellType.STRING).setCellValue("Mean-Hampel");
//        r.createCell(10, CellType.STRING).setCellValue("Raw-Hampel");
//        for (int i = 0; i < hfams.size(); i++) {
//            HampelFilterArrayMetadata h = hfams.get(i);
//            r.createCell(i+3, CellType.NUMERIC).setCellValue(h.metadata);
//        }

        // create data rows and insert data
        int rowsNum = aRaw.size();
        for (int i = 0; i < rowsNum; i++) {
            r = sheet.createRow(i+emptyRowsNum);
            r.createCell(1, CellType.NUMERIC).setCellValue(i+1);
            r.createCell(2, CellType.NUMERIC).setCellValue(aRaw.get(i));
            r.createCell(3, CellType.NUMERIC).setCellValue(aHampel.get(i));
            r.createCell(4, CellType.NUMERIC).setCellValue(aKalman.get(i));
            r.createCell(5, CellType.NUMERIC).setCellValue(aKaufman.get(i));
            r.createCell(6, CellType.NUMERIC).setCellValue(aKalmanHampel.get(i));
            r.createCell(7, CellType.NUMERIC).setCellValue(aKaufmanHampel.get(i));
            r.createCell(8, CellType.NUMERIC).setCellValue(aDistance.get(i));

//            for (int j = 0; j < hfams.size(); j++) {
//                HampelFilterArrayMetadata h = hfams.get(j);
//                r.createCell(j+3, CellType.NUMERIC).setCellValue(h.storage.get(i));
//            }
//            r.createCell(6, CellType.NUMERIC).setCellValue(aMean.get(i));
//            r.createCell(9, CellType.NUMERIC).setCellValue(aMeanHampel.get(i));
//            r.createCell(10, CellType.NUMERIC).setCellValue(aRaw.get(i)-aHampel.get(i));
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd__HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        StringBuilder builder = new StringBuilder();
        builder .append("C:\\Users\\epr2\\Desktop\\Filters_Exp_")
                .append(dtf.format(now));
        if (tag != null) {
            builder.append(tag);
        }
        builder.append(".xlsx");
        Path expPath = Paths.get(builder.toString());
        try {
            OutputStream fileOut = Files.newOutputStream(expPath);
            wb.write(fileOut);
            fileOut.close();
            wb.close();
            U.nout("Experiment is recorded successfully");
            return expPath.toString();
        } catch (IOException e) {
            U.nout("FILE WRITE FAIL");
            e.printStackTrace();
            return null;
        }
    }

    public static class HampelFilterArrayMetadata {
        public String metadata;
        public Hampel hampel;
        public ArrayListDouble storage;

        public HampelFilterArrayMetadata(double n) {
            hampel = new Hampel(n);
            storage = new ArrayListDouble(1000, 150);
            metadata = String.format("Hampel(%.1f)", n+0.0001);
        }

        public void compute() {
            int[] result = hampel.calculate();
            storage.add(result);
        }
    }
}
