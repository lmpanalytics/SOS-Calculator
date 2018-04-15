/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import java.util.Objects;

/**
 * Models SOS data for Spare parts and Maintenance work
 *
 * @author SEPALMM
 */
public class SOSdata {

    private String sosCategory;
    private String cluster;
    private String marketGroup;
    private String market;
    private String assortment;
    private String finalCustomerNumber;
    private String customerName;
    private String customerGroup;
    private double sosResult;

    /**
     * Constructor for Spare part data
     *
     * @param sosCategory
     * @param cluster
     * @param marketGroup
     * @param market
     * @param assortment
     * @param finalCustomerNumber
     * @param customerName
     * @param customerGroup
     * @param sosResult
     */
    public SOSdata(String sosCategory, String cluster, String marketGroup, String market, String assortment, String finalCustomerNumber, String customerName, String customerGroup, double sosResult) {
        this.sosCategory = sosCategory;
        this.cluster = cluster;
        this.marketGroup = marketGroup;
        this.market = market;
        this.assortment = assortment;
        this.finalCustomerNumber = finalCustomerNumber;
        this.customerName = customerName;
        this.customerGroup = customerGroup;
        this.sosResult = sosResult;
    }

    /**
     * Constructor for Maintenence data
     *
     * @param sosCategory
     * @param cluster
     * @param marketGroup
     * @param market
     * @param finalCustomerNumber
     * @param customerName
     * @param customerGroup
     * @param sosResult
     */
    public SOSdata(String sosCategory, String cluster, String marketGroup, String market, String finalCustomerNumber, String customerName, String customerGroup, double sosResult) {
        this.sosCategory = sosCategory;
        this.cluster = cluster;
        this.marketGroup = marketGroup;
        this.market = market;
        this.finalCustomerNumber = finalCustomerNumber;
        this.customerName = customerName;
        this.customerGroup = customerGroup;
        this.sosResult = sosResult;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.sosCategory);
        hash = 17 * hash + Objects.hashCode(this.cluster);
        hash = 17 * hash + Objects.hashCode(this.marketGroup);
        hash = 17 * hash + Objects.hashCode(this.market);
        hash = 17 * hash + Objects.hashCode(this.assortment);
        hash = 17 * hash + Objects.hashCode(this.finalCustomerNumber);
        hash = 17 * hash + Objects.hashCode(this.customerName);
        hash = 17 * hash + Objects.hashCode(this.customerGroup);
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.sosResult) ^ (Double.doubleToLongBits(this.sosResult) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SOSdata other = (SOSdata) obj;
        if (Double.doubleToLongBits(this.sosResult) != Double.doubleToLongBits(other.sosResult)) {
            return false;
        }
        if (!Objects.equals(this.sosCategory, other.sosCategory)) {
            return false;
        }
        if (!Objects.equals(this.cluster, other.cluster)) {
            return false;
        }
        if (!Objects.equals(this.marketGroup, other.marketGroup)) {
            return false;
        }
        if (!Objects.equals(this.market, other.market)) {
            return false;
        }
        if (!Objects.equals(this.assortment, other.assortment)) {
            return false;
        }
        if (!Objects.equals(this.finalCustomerNumber, other.finalCustomerNumber)) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.customerGroup, other.customerGroup)) {
            return false;
        }
        return true;
    }

    public String getSosCategory() {
        return sosCategory;
    }

    public void setSosCategory(String sosCategory) {
        this.sosCategory = sosCategory;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getMarketGroup() {
        return marketGroup;
    }

    public void setMarketGroup(String marketGroup) {
        this.marketGroup = marketGroup;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getAssortment() {
        return assortment;
    }

    public void setAssortment(String assortment) {
        this.assortment = assortment;
    }

    public String getFinalCustomerNumber() {
        return finalCustomerNumber;
    }

    public void setFinalCustomerNumber(String finalCustomerNumber) {
        this.finalCustomerNumber = finalCustomerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public double getSosResult() {
        return sosResult;
    }

    public void setSosResult(double sosResult) {
        this.sosResult = sosResult;
    }

}
