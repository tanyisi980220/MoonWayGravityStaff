package com.example.moonwaygravitystaff.Model;

public class Blocks {
    private String blockName;
    private String parkingLotid;
    private int floorNo;
    private String blockid;

    public Blocks() {
    }

    public String getBlockid() {
        return blockid;
    }

    public void setBlockid(String blockid) {
        this.blockid = blockid;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }

    public String getParkingLotid() {
        return parkingLotid;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Blocks( String blockName, int floorNo, String parkingLotid, String blockid) {
        this.parkingLotid = parkingLotid;
        this.blockid = blockid;
        this.blockName = blockName;
        this.floorNo = floorNo;
    }
}
