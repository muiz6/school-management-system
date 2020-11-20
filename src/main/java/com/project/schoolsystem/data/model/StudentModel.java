/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.schoolsystem.data.model;

/**
 *
 * @author msufwan
 */
public class StudentModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    private int id;
    private String name;
    private String father_name;
    private String mobile_no;
    private String emergency_no;
    private String registration_date;
    private String dob;
    private String active;
    private String address;
    
    

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmergency_no() {
        return emergency_no;
    }

    public void setEmergency_no(String emergency_no) {
        this.emergency_no = emergency_no;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
