/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author Student
 */
/**
 * An Inpatient is a Patient who additionally occupies a hospital bed.
 * Category is always INPATIENT for this class - enforced via the super() call.
 */
public class Inpatient extends Patient {

    private String wardNumber;
    private String bedNumber;

    public Inpatient(String id, String firstName, String lastName, int age,
                      String gender, String medicalCondition) {
        super(id, firstName, lastName, age, gender, medicalCondition, PatientCategory.INPATIENT);
        this.wardNumber = "Unassigned";
        this.bedNumber = "Unassigned";
    }

    public String getWardNumber(){ 
        return wardNumber;
    }
    
    public String getBedNumber(){ 
        return bedNumber;
    }

    public void setWardNumber(String wardNumber){ 
        this.wardNumber = wardNumber; 
    }
    
    public void setBedNumber(String bedNumber){ 
        this.bedNumber = bedNumber; 
    }

    public boolean hasBed() {
        return !bedNumber.equals("Unassigned");
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("    Ward: " + wardNumber + "   Bed: " + bedNumber);
    }
}

