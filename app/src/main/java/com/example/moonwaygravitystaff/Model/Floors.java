package com.example.moonwaygravitystaff.Model;

public class Floors {
    private String floor_id;
    private String floorName;
    private String blockid;
    private int flowNo;
    private int parkedNo;

    public Floors() {}

    public Floors(String floor_id, String floorName, int flowNo, int parkedNo) {
        this.floor_id = floor_id;
        this.floorName = floorName;
        this.flowNo = flowNo;
        this.parkedNo = parkedNo;
    }

    public int getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(int flowNo) {
        this.flowNo = flowNo;
    }

    public int getParkedNo() {
        return parkedNo;
    }

    public void setParkedNo(int parkedNo) {
        this.parkedNo = parkedNo;
    }

    public String getBlockid() {
        return blockid;
    }

    public void setBlockid(String blockid) {
        this.blockid = blockid;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }
}
