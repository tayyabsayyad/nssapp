package com.test.nss.ui.main;

public class AdapterDataMain {
    private String date;
    private String act;
    private String hours;
    private String id;
    private int isApproved;

    public AdapterDataMain(String date, String act, String hours, String id, int isApproved) {
        this.date = date;
        this.act = act;
        this.hours = hours;
        this.id = id;
        this.isApproved = isApproved;

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

    public String getId() {
        return id;
    }

    public int isApproved() {
        return isApproved;
    }
}
