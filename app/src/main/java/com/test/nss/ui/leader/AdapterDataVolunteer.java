package com.test.nss.ui.leader;

public class AdapterDataVolunteer {
    private String date;
    private String act;
    private String hours;
    private String id;
    private String state;

    public AdapterDataVolunteer(String date, String act, String hours, String id, String state) {
        this.date = date;
        this.act = act;
        this.hours = hours;
        this.id = id;
        this.state = state;
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

    public String getState() {
        return state;
    }
}
