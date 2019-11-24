package com.example.moonwaygravitystaff.Model;

public class Floors {
    private String floor_id;
    private String floorName;

    public Floors() {}

    public Floors(String floor_id, String floorName) {
        this.floor_id = floor_id;
        this.floorName = floorName;
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
