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
 * Base class for every patient in the system.
 * Outpatients and Emergency patients use this class directly.
 * Inpatients use the Inpatient subclass, which adds ward/bed info.
 */
public class Patient {

    private final String id; // never changes once registered
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String medicalCondition;
    private final PatientCategory category; 

    public Patient(String id, String firstName, String lastName, int age,
                   String gender, String medicalCondition, PatientCategory category) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.category = category;
    }

    public String getId(){
        return id;
    }
    
    public String getFirstName(){ 
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public int getAge(){ 
        return age;
    }
    
    public String getGender(){ 
        return gender;
    }
    
    
    public String getMedicalCondition(){
        return medicalCondition;
    }
    
    public PatientCategory getCategory() {
        return category; 
    }

    public void setFirstName(String firstName){
        this.firstName = firstName; 
    }
    
    public void setLastName(String lastName){ 
        this.lastName = lastName;
    }
    
    public void setAge(int age){ 
        this.age = age; 
    }
    
    public void setGender(String gender){
        this.gender = gender; 
    }
    
    public void setMedicalCondition(String medicalCondition){ 
        this.medicalCondition = medicalCondition;
    }

    /**
     * Prints this patient's details. Inpatient overrides this to also
     * show ward and bed number.
     */
    public void displayDetails() {
        System.out.printf("%-12s %-10s %-10s %-4d %-8s %-15s %-10s%n",
            id, firstName, lastName, age, gender, medicalCondition, category);
    }
}

