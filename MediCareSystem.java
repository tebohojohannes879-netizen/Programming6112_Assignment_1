/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author Student
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MediCareSystem {

    private static final int WARD_ROWS = 4;
    private static final int WARD_COLS = 5;
    private static final String WARD_NUMBER = "1"; // single ward in this system

    private final ArrayList<Patient> patients = new ArrayList<>();
    private final String[][] bedNumbers = new String[WARD_ROWS][WARD_COLS];
    private final boolean[][] bedOccupied = new boolean[WARD_ROWS][WARD_COLS];

    public MediCareSystem() {
        initializeBeds();
    }

    private void initializeBeds() {
        int bedNumber = 1;
        for (int r = 0; r < WARD_ROWS; r++) {
            for (int c = 0; c < WARD_COLS; c++) {
                bedNumbers[r][c] = String.format("B%02d", bedNumber);
                bedOccupied[r][c] = false;
                bedNumber++;
            }
        }
    }

    // ==================================================
    // FEATURE 1: PATIENT MANAGEMENT
    // ==================================================

    /**
     * Registers a new patient. Returns false (and does not add the patient)
     * if the ID is already in use, the ID format is invalid, or the age is
     * out of range.
     */
    public boolean registerPatient(Patient patient) {
        if (!isValidId(patient.getId()))
            return false;
        
        if (!isValidAge(patient.getAge()))
            return false;
        
        if (findPatientById(patient.getId()) != null)
            return false; // duplicate ID
        
        patients.add(patient);
        return true;
    }

    public Patient searchPatient(String id) {
        return findPatientById(id);
    }

    /**
     * Updates a patient's editable fields. Patient ID and category cannot be
     * changed here - ID is a permanent identifier, and category is fixed by
     * class type (an Inpatient can't become an Outpatient without changing
     * Java class). To change category, delete and re-register the patient.
     * Returns false if the patient doesn't exist or the new age is invalid.
     */
    public boolean updatePatient(String id, String firstName, String lastName,
                                  int age, String gender, String medicalCondition) {
        Patient patient = findPatientById(id);
        if (patient == null)
            return false;
        if (!isValidAge(age))
            return false;

        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setMedicalCondition(medicalCondition);
        return true;
    }

    /**
     * Deletes a patient. If they currently hold a bed, it is released first
     * so no bed is ever left permanently marked occupied by a patient who no
     * longer exists.
     */
    public boolean deletePatient(String id) {
        Patient patient = findPatientById(id);
        if (patient == null)
            return false;

        if (patient instanceof Inpatient) {
            Inpatient inpatient = (Inpatient) patient;
            if (inpatient.hasBed()) {
                releaseBed(inpatient.getBedNumber());
            }
        }

        patients.remove(patient);
        return true;
    }

    public List<Patient> getAllPatients() {
        return Collections.unmodifiableList(patients);
    }

    public int getTotalPatients() {
        return patients.size();
    }

    private Patient findPatientById(String id) {
        for (Patient p : patients) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public static boolean isValidId(String id) {
        if (id == null || id.length() != 10)
            return false;
        return id.substring(0, 3).equalsIgnoreCase("MED");
    }

    public static boolean isValidAge(int age) {
        return age > 0 && age <= 130;
    }

    // ==================================================
    // FEATURE 2: BED MANAGEMENT
    // ==================================================

    /** Allocates the first available bed to an inpatient. */
    public boolean allocateBed(String patientId) {
        int[] freeBed = findFirstFreeBed();
        
        if (freeBed == null)
            return false;
        
        return allocateBed(patientId, bedNumbers[freeBed[0]][freeBed[1]]);
    }

    /** Allocates a specific bed to an inpatient. */
    public boolean allocateBed(String patientId, String bedNumber) {
        Patient patient = findPatientById(patientId);
        
        if (!(patient instanceof Inpatient))
            return false; // not found, or not an inpatient

        Inpatient inpatient = (Inpatient) patient;
        if (inpatient.hasBed())
            return false; // already has a bed

        int[] pos = findBedPosition(bedNumber);
        if (pos == null) 
            return false; // no such bed
        
        if (bedOccupied[pos[0]][pos[1]]) return false; // prevent allocating an occupied bed

        bedOccupied[pos[0]][pos[1]] = true;
        inpatient.setWardNumber(WARD_NUMBER);
        inpatient.setBedNumber(bedNumbers[pos[0]][pos[1]]);
        return true;
    }

    public boolean releaseBed(String bedNumber) {
        int[] pos = findBedPosition(bedNumber);
        if (pos == null) return false;
        if (!bedOccupied[pos[0]][pos[1]]) return false; // already available

        bedOccupied[pos[0]][pos[1]] = false;

        for (Patient p : patients) {
            if (p instanceof Inpatient) {
                Inpatient inpatient = (Inpatient) p;
                if (bedNumber.equalsIgnoreCase(inpatient.getBedNumber())) {
                    inpatient.setWardNumber("Unassigned");
                    inpatient.setBedNumber("Unassigned");
                }
            }
        }
        return true;
    }

    public String[][] getWardLayout() {
        return bedNumbers;
    }

    public boolean[][] getOccupancyGrid() {
        return bedOccupied;
    }

    public List<String> getAvailableBeds() {
        List<String> available = new ArrayList<>();
        for (int r = 0; r < WARD_ROWS; r++) {
            for (int c = 0; c < WARD_COLS; c++) {
                if (!bedOccupied[r][c]) available.add(bedNumbers[r][c]);
            }
        }
        return available;
    }

    /** Bed number -> Patient ID, for every currently occupied bed. */
    public Map<String, String> getOccupiedBeds() {
        Map<String, String> occupied = new LinkedHashMap<>();
        for (Patient p : patients) {
            if (p instanceof Inpatient) {
                Inpatient inpatient = (Inpatient) p;
                if (inpatient.hasBed()) {
                    occupied.put(inpatient.getBedNumber(), inpatient.getId());
                }
            }
        }
        return occupied;
    }

    private int[] findFirstFreeBed() {
        for (int r = 0; r < WARD_ROWS; r++) {
            for (int c = 0; c < WARD_COLS; c++) {
                if (!bedOccupied[r][c]) return new int[]{r, c};
            }
        }
        return null;
    }

    private int[] findBedPosition(String bedNumber) {
        for (int r = 0; r < WARD_ROWS; r++) {
            for (int c = 0; c < WARD_COLS; c++) {
                if (bedNumbers[r][c].equalsIgnoreCase(bedNumber))
                    return new int[]{r, c};
            }
        }
        return null;
    }

    // ==================================================
    // FEATURE 3: REPORTS
    // ==================================================

    public int getTotalOccupiedBeds() {
        int count = 0;
        for (int r = 0; r < WARD_ROWS; r++) {
            for (int c = 0; c < WARD_COLS; c++) {
                if (bedOccupied[r][c]) count++;
            }
        }
        return count;
    }

    public int getTotalBeds() {
        return WARD_ROWS * WARD_COLS;
    }

    public double getOccupancyPercentage() {
        return (getTotalOccupiedBeds() * 100.0) / getTotalBeds();
    }

    // ==================================================
    // Sorting
    // ==================================================

    public void sortBySurname() {
        patients.sort(Comparator.comparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER));
    }

    public void sortById() {
        patients.sort(Comparator.comparing(Patient::getId, String.CASE_INSENSITIVE_ORDER));
    }
}

