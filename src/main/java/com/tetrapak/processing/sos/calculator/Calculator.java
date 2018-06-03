/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import static com.tetrapak.processing.sos.calculator.ExcelWriter.writeSparePartDataToExcel;

/**
 * This is a calculator for SOS Data. Sales data source is BO special ledger
 * using the last 12 months, and Potentials data source is TecWeb. The results
 * are written to Excel files in the working directory.
 *
 * Prepare Database by loading 12 month data following the procedure to load
 * data for 'MSP Dashboards'.
 *
 * Duration to load: 50 minutes. Duration to run: 1 minute.
 *
 * @author SEPALMM
 */
public class Calculator {

    private static void calculateSOSspareparts(String cluster) {
        Map<Integer, SOSdata> alfPotMmap = new HashMap<>();
        QueryDB q = new QueryDB();
        q.calculateSparePartPotential(cluster);
        Map<Integer, SOSdata> map = q.getSparePartResultsMap();

//        Calculate sum of spare part potentials per final customer number
        Map<String, Double> spPotMap1 = map.values().stream().
                filter(f -> f.getSosCategory().equalsIgnoreCase("SP_POT")).collect(
                Collectors.groupingBy(
                        SOSdata::getFinalCustomerNumber,
                        Collectors.reducing(
                                0d,
                                SOSdata::getSosResult,
                                Double::sum)));

// Calculate the potential sales for the Assortment group 'Al flow parts' at
// 30% of total spare part potential, and add this to the map
        map.values().stream().forEach(v -> {
            String finalCustomerNumber = v.getFinalCustomerNumber();
            if (spPotMap1.containsKey(finalCustomerNumber)) {

                double pot = spPotMap1.get(finalCustomerNumber);
                double alfPot = pot * 0.3;
                int compositeKey = ("SP_POT" + v.getCluster() + v.getMarketGroup() + v.getMarket() + "Al flow parts" + finalCustomerNumber).hashCode();
                alfPotMmap.put(compositeKey, new SOSdata(
                        "SP_POT", v.getCluster(), v.getMarketGroup(),
                        v.getMarket(), "Al flow parts", finalCustomerNumber,
                        v.getCustomerName(), v.getCustomerGroup(), alfPot));
            }
        });

        map.putAll(alfPotMmap);

        String filename = "SparePartSOS_" + cluster + ".xlsx";
        writeSparePartDataToExcel(map, filename);

    }

    private static void calculateSOSmaintenanceWork(String cluster) {
        Map<Integer, SOSdata> alfPotMmap = new HashMap<>();
        QueryDB q = new QueryDB();
        q.calculateMaintenancePotential(cluster);
        Map<Integer, SOSdata> map = q.getMaintenanceWorkResultsMap();

//        Calculate sum of maintenance potentials per final customer number
        Map<String, Double> spPotMap1 = map.values().stream().
                filter(f -> f.getSosCategory().equalsIgnoreCase("MW_POT")).collect(
                Collectors.groupingBy(
                        SOSdata::getFinalCustomerNumber,
                        Collectors.reducing(
                                0d,
                                SOSdata::getSosResult,
                                Double::sum)));

// Calculate the potential sales for the Assortment group 'Al flow parts' at
// 6% of total Maintenance work potential, and add this to the map
        map.values().stream().forEach(v -> {
            String finalCustomerNumber = v.getFinalCustomerNumber();
            if (spPotMap1.containsKey(finalCustomerNumber)) {

                double pot = spPotMap1.get(finalCustomerNumber);
                double adjustedPot = pot * 1.06;
                int compositeKey = ("MW_POT" + v.getCluster() + v.getMarketGroup() + v.getMarket() + finalCustomerNumber).hashCode();
                alfPotMmap.put(compositeKey, new SOSdata(
                        "MW_POT", v.getCluster(), v.getMarketGroup(),
                        v.getMarket(), finalCustomerNumber,
                        v.getCustomerName(), v.getCustomerGroup(), adjustedPot));
            }
        });

        map.putAll(alfPotMmap);

        String filename = "MaintenanceWorkSOS_" + cluster + ".xlsx";
        ExcelWriter.writeMaintenanceWorkDataToExcel(map, filename);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        calculateSOSspareparts("E&CA");
        calculateSOSspareparts("GC");
        calculateSOSspareparts("GME&A");
        calculateSOSspareparts("NC&SA");
        calculateSOSspareparts("SAEA&O");

        calculateSOSmaintenanceWork("E&CA");
        calculateSOSmaintenanceWork("GC");
        calculateSOSmaintenanceWork("GME&A");
        calculateSOSmaintenanceWork("NC&SA");
        calculateSOSmaintenanceWork("SAEA&O");

    }

}
