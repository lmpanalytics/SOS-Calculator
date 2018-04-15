/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an excel writer class.
 *
 * @author SEPALMM
 */
public class ExcelWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriter.class);

    /**
     * Writes an Excel file
     *
     * @param map to read from and write to file
     * @param fileName of output file
     */
    public static void writeExcel(Map<Integer, SOSdata> map, String fileName) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("db");

        LOGGER.info("Creating excel");

//        Write header
        String[] header = {"sosCategory", "cluster", "marketGroup", "market", "assortment", "finalCustomerNumber", "customerName", "customerGroup", "sosResult"};

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            String s = header[i];
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(s);
        }

//        Write table contents
        SOSdata[] data = map.values().toArray(new SOSdata[0]);

        for (int tableRow = 0; tableRow < data.length; tableRow++) {
            SOSdata d = data[tableRow];
            Row row = sheet.createRow(tableRow + 1);
            for (int col = 0; col < 9; col++) {
                Cell cell = row.createCell(col);
                switch (col) {
                    case 0:
                        cell.setCellValue(d.getSosCategory());
                        break;
                    case 1:
                        cell.setCellValue(d.getCluster());
                        break;
                    case 2:
                        cell.setCellValue(d.getMarketGroup());
                        break;
                    case 3:
                        cell.setCellValue(d.getMarket());
                        break;
                    case 4:
                        cell.setCellValue(d.getAssortment());
                        break;
                    case 5:
                        cell.setCellValue(d.getFinalCustomerNumber());
                        break;
                    case 6:
                        cell.setCellValue(d.getCustomerName());
                        break;
                    case 7:
                        cell.setCellValue(d.getCustomerGroup());
                        break;
                    case 8:
                        cell.setCellValue(d.getSosResult());
                        break;
                    default:
                        break;
                }
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException {}.", e.getMessage());
        }

        LOGGER.info("Wrote {} rows to file {}.", map.size(), fileName);
    }
}
