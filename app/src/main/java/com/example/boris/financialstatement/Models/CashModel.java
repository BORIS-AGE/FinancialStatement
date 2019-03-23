package com.example.boris.financialstatement.Models;

public class CashModel {
    String name;
    int cash;

    public CashModel() {
    }

    public CashModel(String name, int cash) {
        this.name = name;
        this.cash = cash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
}
