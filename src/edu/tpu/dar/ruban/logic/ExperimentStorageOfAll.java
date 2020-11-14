package edu.tpu.dar.ruban.logic;

import edu.tpu.dar.ruban.utils.U;
import edu.tpu.dar.ruban.utils.ArrayListInt;
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
import java.util.*;

public class ExperimentStorageOfAll {
    public Map<String, ArrayListInt> rssiSet;
    public String currentExpName;
    public String targetMac;
    public int sourceId;
    boolean experimentAllowed = false;

    public ExperimentStorageOfAll(String targetMac, int sourceId) {
        this.targetMac = targetMac;
        this.sourceId = sourceId;
        rssiSet = new HashMap<>(50);
    }

    public void add(int rssi) {
        rssiSet.computeIfAbsent(currentExpName, k -> new ArrayListInt()).add(rssi);
    }

    public void setExperimentName(String expName) {
        currentExpName = expName;
    }

    public void startExperiment() {
        experimentAllowed = true;
    }
    public void stopExperiment() {
        experimentAllowed = false;
    }

    public boolean inExperiment(String mac, int id) {
        return mac.equals(this.targetMac) && id == this.sourceId;
    }
    public boolean isOn() {
        return experimentAllowed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(60);
        rssiSet.forEach( (expName, data) -> {
            builder.append(expName).append(": ");
            int size = data.size();
            for (int i = 0; i < size; i++) {
                builder.append((int) data.get(i)).append('\t');
            }
            builder.append("END");
        });
        return builder.toString();
    }

    public String toExcel(String tag) {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = (HSSFSheet) wb.createSheet("Experiments");
        // create rows
        int[] maxLength = {0};
        rssiSet.forEach((__, data) -> {
            if (maxLength[0] < data.size()) {
                maxLength[0] = data.size();
            }
        });
        for (int i = 0; i < maxLength[0]+30; i++) {
            sheet.createRow(i);
        }


        //set label
        sheet.getRow(12).createCell(0, CellType.STRING).setCellValue("Числа");
        sheet.getRow(11).createCell(0, CellType.STRING).setCellValue("Кол-во");
        sheet.getRow(10).createCell(0, CellType.STRING).setCellValue("Номер");
        sheet.getRow(9).createCell(0, CellType.STRING).setCellValue("Сумма");
        sheet.getRow(8).createCell(0, CellType.STRING).setCellValue("Среднее");

        // save data
        Row headerRow = sheet.getRow(10);
        Row numberRow = sheet.getRow(11);
        int[] col = {1};
        rssiSet.forEach((expName, data) -> {
            headerRow.createCell(col[0], CellType.NUMERIC).setCellValue(Integer.parseInt(expName));
            numberRow.createCell(col[0], CellType.NUMERIC).setCellValue(data.size());
            int limit = data.size();
            for (int i = 0; i < limit; i++) {
                sheet.getRow(i+12).createCell(col[0], CellType.NUMERIC).setCellValue(data.get(i));
            }

            // add formulas to data
            String colChar = String.valueOf((char) ('A' + col[0]));
            StringBuilder builder = new StringBuilder();
            builder.append("СУММ(").append(colChar).append("13:").append(colChar).append(13+data.size()).append(")");
            sheet.getRow(9).createCell(col[0], CellType.FORMULA).setCellValue(builder.toString());

            builder.setLength(0);
            builder.append("=").append(colChar).append("10/").append(colChar).append("12");
            sheet.getRow(8).createCell(col[0], CellType.FORMULA).setCellValue(builder.toString());

            col[0] = col[0] + 1;
        });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd__HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        StringBuilder builder = new StringBuilder();
        builder .append("C:\\Users\\epr2\\Desktop\\Exp_results__")
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
}
