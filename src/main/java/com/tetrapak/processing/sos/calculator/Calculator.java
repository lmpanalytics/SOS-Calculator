/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import static com.tetrapak.processing.sos.calculator.ExcelWriter.writeExcel;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is a calculator for SOS Data. Sales data source is BO special ledger
 * using the last 12 months, and Potentials data source is TecWeb. The results
 * are written to Excekl files in the working directory.
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
                filter(f -> f.getAssortment() != null && f.getSosCategory().equalsIgnoreCase("SP_POT")).collect(
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
        writeExcel(map, filename);

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

    }

}
