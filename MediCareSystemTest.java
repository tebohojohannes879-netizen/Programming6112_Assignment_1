/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */

/**
 * JUnit 5 tests for MediCareSystem.
 * Each test creates its own fresh MediCareSystem in setUp() so tests
 * don't affect each other.
 */
public class MediCareSystemTest {

    private MediCareSystem system;

    @BeforeEach
    void setUp() {
        system = new MediCareSystem();
    }

    @Test
    void testRegisterPatient() {
        Patient patient = new Patient("MED1111111", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT);

        boolean result = system.registerPatient(patient);

        assertTrue(result);
        assertEquals(1, system.getTotalPatients());
    }

    @Test
    void testSearchPatient() {
        system.registerPatient(new Patient("MED1111111", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT));

        Patient found = system.searchPatient("MED1111111");
        Patient notFound = system.searchPatient("MED9999999");

        assertNotNull(found);
        assertEquals("John", found.getFirstName());
        assertNull(notFound);
    }

    @Test
    void testUpdatePatientDetails() {
        system.registerPatient(new Patient("MED1111111", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT));

        boolean result = system.updatePatient("MED1111111", "John", "Doe", 40, "Male", "Recovered");
        Patient updated = system.searchPatient("MED1111111");

        assertTrue(result);
        assertEquals(40, updated.getAge());
        assertEquals("Recovered", updated.getMedicalCondition());
    }

    @Test
    void testDeletePatient() {
        system.registerPatient(new Patient("MED1111111", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT));

        boolean result = system.deletePatient("MED1111111");

        assertTrue(result);
        assertEquals(0, system.getTotalPatients());
        assertNull(system.searchPatient("MED1111111"));
    }

    @Test
    void testAllocateBed() {
        system.registerPatient(new Inpatient("MED1111111", "John", "Doe", 34, "Male", "Flu"));

        boolean result = system.allocateBed("MED1111111");
        Inpatient patient = (Inpatient) system.searchPatient("MED1111111");

        assertTrue(result);
        assertTrue(patient.hasBed());
        assertEquals(1, system.getTotalOccupiedBeds());
    }

    @Test
    void testReleaseBed() {
        system.registerPatient(new Inpatient("MED1111111", "John", "Doe", 34, "Male", "Flu"));
        system.allocateBed("MED1111111");
        Inpatient patient = (Inpatient) system.searchPatient("MED1111111");
        String bedNumber = patient.getBedNumber();

        boolean result = system.releaseBed(bedNumber);

        assertTrue(result);
        assertFalse(patient.hasBed());
        assertEquals(0, system.getTotalOccupiedBeds());
    }

    @Test
    void testPreventDuplicatePatientIds() {
        system.registerPatient(new Patient("MED1111111", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT));

        boolean result = system.registerPatient(
            new Patient("MED1111111", "Someone", "Else", 50, "Female", "Cold", PatientCategory.EMERGENCY));

        assertFalse(result);
        assertEquals(1, system.getTotalPatients());
    }

    @Test
    void testPreventAllocatingOccupiedBed() {
        system.registerPatient(new Inpatient("MED1111111", "John", "Doe", 34, "Male", "Flu"));
        system.registerPatient(new Inpatient("MED2222222", "Jane", "Smith", 28, "Female", "Asthma"));
        system.allocateBed("MED1111111", "B01");

        boolean result = system.allocateBed("MED2222222", "B01");

        assertFalse(result);
    }

    @Test
    void testPreventBedAllocationWhenAllBedsOccupied() {
        // Fill all 20 beds
        for (int i = 1; i <= 20; i++) {
            String id = String.format("MED%07d", i);
            system.registerPatient(new Inpatient(id, "First" + i, "Last" + i, 30, "Male", "Cond"));
            system.allocateBed(id);
        }
        system.registerPatient(new Inpatient("MED0000021", "Extra", "Patient", 30, "Male", "Cond"));

        boolean result = system.allocateBed("MED0000021");

        assertFalse(result);
        assertEquals(20, system.getTotalOccupiedBeds());
    }

    @Test
    void testSortBySurname() {
        system.registerPatient(new Patient("MED1111111", "A", "Zebra", 30, "Male", "X", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("MED2222222", "B", "Apple", 30, "Male", "X", PatientCategory.OUTPATIENT));

        system.sortBySurname();

        assertEquals("Apple", system.getAllPatients().get(0).getLastName());
    }

    @Test
    void testSortById() {
        system.registerPatient(new Patient("MED2222222", "B", "Smith", 30, "Male", "X", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("MED1111111", "A", "Doe", 30, "Male", "X", PatientCategory.OUTPATIENT));

        system.sortById();

        assertEquals("MED1111111", system.getAllPatients().get(0).getId());
    }
}

