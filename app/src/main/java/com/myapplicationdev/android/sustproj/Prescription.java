package com.myapplicationdev.android.sustproj;

import java.io.Serializable;

public class Prescription implements Serializable {
    private int prescriptionId, quantity;
    private String date, bnfCode, bnfName, vendingLocation, status;
    private double actCost;

    public Prescription(int prescriptionId, int quantity, String date, String bnfCode, String bnfName, String vendingLocation, double actCost, String status) {
        this.prescriptionId = prescriptionId;
        this.quantity = quantity;
        this.date = date;
        this.bnfCode = bnfCode;
        this.bnfName = bnfName;
        this.vendingLocation = vendingLocation;
        this.actCost = actCost;
        this.status = status;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBnfCode() {
        return bnfCode;
    }

    public void setBnfCode(String bnfCode) {
        this.bnfCode = bnfCode;
    }

    public String getBnfName() {
        return bnfName;
    }

    public void setBnfName(String bnfName) {
        this.bnfName = bnfName;
    }

    public String getVendingLocation() {
        return vendingLocation;
    }

    public void setVendingLocation(String vendingLocation) {
        this.vendingLocation = vendingLocation;
    }

    public double getActCost() {
        return actCost;
    }

    public void setActCost(double actCost) {
        this.actCost = actCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
