/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import java.util.Map;

/**
 * This is a calculator for SOS. It takes arguments for the cluster. Sales data
 * source is BO special ledger using the last 12 months, and Potentials data
 * source is TecWeb.
 *
 * @author SEPALMM
 */
public class Calculator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello World");
        
        QueryDB q = new QueryDB();
        q.calculateSparePartPotential("E&CA");
        Map<String, SOSdata> map = q.getSparePartResultsMap();
        System.out.println("HOLD");
    }

}
