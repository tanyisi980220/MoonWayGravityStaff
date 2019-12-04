package com.example.moonwaygravitystaff.Model;

public class ParkingSlot {
    private String compid;
    private String status;

    public ParkingSlot() {
    }

    public String getCompid() {
        return compid;
    }

    public void setCompid(String compid) {
        this.compid = compid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ParkingSlot(String compid, String status) {
        this.compid = compid;
        this.status = status;
    }
}
