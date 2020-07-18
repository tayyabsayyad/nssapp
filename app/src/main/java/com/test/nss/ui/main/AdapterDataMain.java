package com.test.nss.ui.main;

public class AdapterDataMain {
    private String date;
    private String act;
    private String hours;

    public AdapterDataMain(String date, String act, String hours) {
        this.date = date;
        this.act = act;
        this.hours = hours;
    }

    public String getDate() {
        return date;
    }

    public String getAct() {
        return act;
    }

    public String getHours() {
        return hours;
    }

}
