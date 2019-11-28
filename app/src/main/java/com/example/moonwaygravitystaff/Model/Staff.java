package com.example.moonwaygravitystaff.Model;

public class Staff {
    private String role;
    private String name;

    public Staff() {

    }

    public Staff(String role, String name) {
        this.role = role;
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
