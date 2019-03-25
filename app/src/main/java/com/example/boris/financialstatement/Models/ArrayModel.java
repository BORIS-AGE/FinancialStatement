package com.example.boris.financialstatement.Models;

import java.util.List;

public class ArrayModel {
    List<CashModel> listIn;
    List<CashModel> listOut;
    List<CashModel> listAss;
    List<CashModel> listLiab;
    int cash;

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public List<CashModel> getListIn() {
        return listIn;
    }

    public void setListIn(List<CashModel> listIn) {
        this.listIn = listIn;
    }

    public List<CashModel> getListOut() {
        return listOut;
    }

    public void setListOut(List<CashModel> listOut) {
        this.listOut = listOut;
    }

    public List<CashModel> getListAss() {
        return listAss;
    }

    public void setListAss(List<CashModel> listAss) {
        this.listAss = listAss;
    }

    public List<CashModel> getListLiab() {
        return listLiab;
    }

    public void setListLiab(List<CashModel> listLiab) {
        this.listLiab = listLiab;
    }

    public ArrayModel(List<CashModel> listIn, List<CashModel> listOut, List<CashModel> listAss, List<CashModel> listLiab, int cash) {

        this.listIn = listIn;
        this.listOut = listOut;
        this.listAss = listAss;
        this.listLiab = listLiab;
        this.cash = cash;
    }
    public ArrayModel() {
    }
}
