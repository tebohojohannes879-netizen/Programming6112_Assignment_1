/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main;

/**
 *
 * @author Student
 */
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static MediCareSystem system = new MediCareSystem();

    public static void main(String[] args) {
        int choice;

        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: 
                    register(); break;
                
                case 2: 
                    search(); break;
                    
                case 3: 
                    update(); break;
                    
                case 4: 
                    delete(); break;
                    
                case 5: 
                    displayAllPatients(); break;
                    
                case 6: 
                    allocateBed(); break;
                    
                case 7: 
                    releaseBed(); break;
                    
                case 8: 
                    displayWardLayout(); break;
                    
                case 9: 
                    displayAvailableBeds(); break;
                    
                case 10: 
                    displayOccupiedBeds(); break;
                    
                case 11: 
                    generateFullReport(); break;
                    
                case 12: 
                    system.sortBySurname(); System.out.println("Patients sorted by surname.\n"); break;
                    
                case 13: 
                    system.sortById(); System.out.println("Patients sorted by Patient ID.\n"); break;
                    
                case 14: 
                    System.out.println("Exiting MediCare System. Goodbye!"); break;
                    
                default: 
                    System.out.println("Invalid option. Please choose 1-14.\n");
            }
        } while (choice != 14);
    }

    private static void displayMenu() {
        System.out.println("---MediCare Menu: ---");
        System.out.println("1. Register a new patient");
        System.out.println("2. Search for a patient");
        System.out.println("3. Update patient details");
        System.out.println("4. Delete a patient");
        System.out.println("5. Display all patients");
        System.out.println("6. Allocate a bed to an inpatient");
        System.out.println("7. Release a bed");
        System.out.println("8. Display ward layout");
        System.out.println("9. Display available beds");
        System.out.println("10. Display occupied beds");
        System.out.println("11. Generate full report");
        System.out.println("12. Sort patients by surname");
        System.out.println("13. Sort patients by Patient ID");
        System.out.println("14. Exit");
        System.out.print("Choose a number on the menu: ");
    }

    // ---------- Feature 1: Patient Management ----------

    private static void register() {
        System.out.print("How many patients must be registered: ");
        int numOfPatients = scanner.nextInt();
        scanner.nextLine();

        for (int n = 0; n < numOfPatients; n++) {
            System.out.println("\n--- Patient " + (n + 1) + " ---");
            boolean valid;

            String patientId;
            do {
                System.out.print("Enter Patient Id(format ex.MED1234567): ");
                patientId = scanner.nextLine();
                valid = MediCareSystem.isValidId(patientId);
                if (!valid) {
                    System.out.println("Invalid ID - must be 10 characters starting with 'MED'.");
                } else if (system.searchPatient(patientId) != null) {
                    valid = false;
                    System.out.println("That ID is already registered. Please enter a unique ID.");
                }
            } while (!valid);

            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();

            int age;
            do {
                System.out.print("Enter Age: ");
                age = scanner.nextInt();
                scanner.nextLine();
                valid = MediCareSystem.isValidAge(age);
                if (!valid) System.out.println("Invalid age - must be between 1 and 130.");
            } while (!valid);

            System.out.print("Enter Gender: ");
            String gender = scanner.nextLine();

            System.out.print("Enter Condition: ");
            String condition = scanner.nextLine();

            PatientCategory category = readCategory();

            Patient patient;
            if (category == PatientCategory.INPATIENT) {
                patient = new Inpatient(patientId, firstName, lastName, age, gender, condition);
            } else {
                patient = new Patient(patientId, firstName, lastName, age, gender, condition, category);
            }

            boolean registered = system.registerPatient(patient);
            if (registered) {
                System.out.println("Patient registered successfully!");
            } else {
                System.out.println("Registration failed - ID already in use or invalid data.");
            }
        }
    }

    private static PatientCategory readCategory() {
        System.out.println("Patient Category:");
        System.out.println("  1. Inpatient");
        System.out.println("  2. Outpatient");
        System.out.println("  3. Emergency");

        while (true) {
            System.out.print("Choose an option (1-3): ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": 
                    return PatientCategory.INPATIENT;
                    
                case "2": 
                    return PatientCategory.OUTPATIENT;
                    
                case "3": 
                    return PatientCategory.EMERGENCY;
                    
                default: System.out.println("Invalid option. Please choose 1, 2 or 3.");
            }
        }
    }

    private static void search() {
        System.out.print("Enter medical ID: ");
        String id = scanner.nextLine();

        Patient patient = system.searchPatient(id);
        if (patient == null) {
            System.out.println("Patient not Found\n");
            return;
        }

        printPatientHeader();
        patient.displayDetails();
        System.out.println();
    }

    private static void update() {
        System.out.print("Enter Patient ID to update: ");
        String id = scanner.nextLine();

        Patient patient = system.searchPatient(id);
        if (patient == null) {
            System.out.println("Patient not Found\n");
            return;
        }

        System.out.println("Current details:");
        printPatientHeader();
        patient.displayDetails();

        System.out.println("\nLeave a field blank to keep its current value. (Patient ID and category cannot be changed.)");

        System.out.print("First Name [" + patient.getFirstName() + "]: ");
        String firstName = scanner.nextLine();
        if (firstName.isEmpty()) firstName = patient.getFirstName();

        System.out.print("Last Name [" + patient.getLastName() + "]: ");
        String lastName = scanner.nextLine();
        if (lastName.isEmpty()) lastName = patient.getLastName();

        int age = patient.getAge();
        System.out.print("Age [" + patient.getAge() + "]: ");
        String ageText = scanner.nextLine();
        if (!ageText.isEmpty()) {
            int newAge = Integer.parseInt(ageText);
            if (MediCareSystem.isValidAge(newAge)) {
                age = newAge;
            } else {
                System.out.println("Invalid age entered - keeping the previous value.");
            }
        }

        System.out.print("Gender [" + patient.getGender() + "]: ");
        String gender = scanner.nextLine();
        if (gender.isEmpty()) gender = patient.getGender();

        System.out.print("Condition [" + patient.getMedicalCondition() + "]: ");
        String condition = scanner.nextLine();
        if (condition.isEmpty()) condition = patient.getMedicalCondition();

        system.updatePatient(id, firstName, lastName, age, gender, condition);
        System.out.println("Patient updated successfully!\n");
    }

    private static void delete() {
        System.out.print("Enter Patient ID to delete: ");
        String id = scanner.nextLine();

        Patient patient = system.searchPatient(id);
        if (patient == null) {
            System.out.println("Patient not Found\n");
            return;
        }

        System.out.print("Are you sure you want to delete " + patient.getFirstName()
            + " " + patient.getLastName() + "? (yes/no): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Delete cancelled.\n");
            return;
        }

        system.deletePatient(id);
        System.out.println("Patient deleted successfully!\n");
    }

    private static void displayAllPatients() {
        List<Patient> all = system.getAllPatients();
        System.out.println("\n--- All Registered Patients (" + all.size() + " total) ---");

        if (all.isEmpty()) {
            System.out.println("No patients have been registered yet.\n");
            return;
        }

        printPatientHeader();
        for (Patient p : all) {
            p.displayDetails();
        }
        System.out.println();
    }

    private static void printPatientHeader() {
        System.out.printf("%-12s %-10s %-10s %-4s %-8s %-15s %-10s%n",
            "ID", "First", "Last", "Age", "Gender", "Condition", "Category");
    }

    // ---------- Feature 2: Bed Management ----------

    private static void allocateBed() {
        System.out.print("Enter Patient ID to allocate a bed: ");
        String id = scanner.nextLine();

        Patient patient = system.searchPatient(id);
        if (patient == null) {
            System.out.println("Patient not Found\n");
            return;
        }
        if (!(patient instanceof Inpatient)) {
            System.out.println("Only Inpatients may be allocated a bed. This patient is: " + patient.getCategory() + "\n");
            return;
        }

        boolean allocated = system.allocateBed(id);
        if (allocated) {
            Inpatient inpatient = (Inpatient) patient;
            System.out.println("Bed " + inpatient.getBedNumber() + " allocated to patient " + id + ".\n");
        } else if (((Inpatient) patient).hasBed()) {
            System.out.println("This patient already has bed " + ((Inpatient) patient).getBedNumber() + " allocated.\n");
        } else {
            System.out.println("No beds available. The ward is full.\n");
        }
    }

    private static void releaseBed() {
        System.out.print("Enter Bed Number to release (e.g. B05): ");
        String bedNumber = scanner.nextLine();

        boolean released = system.releaseBed(bedNumber);
        if (released) {
            System.out.println("Bed " + bedNumber + " released.\n");
        } else {
            System.out.println("That bed is invalid or already available.\n");
        }
    }

    private static void displayWardLayout() {
        String[][] bedNumbers = system.getWardLayout();
        boolean[][] occupied = system.getOccupancyGrid();

        System.out.println("\n--- Ward Layout ---");
        for (int r = 0; r < bedNumbers.length; r++) {
            for (int c = 0; c < bedNumbers[r].length; c++) {
                String status = occupied[r][c] ? "[X]" : "[ ]";
                System.out.print(bedNumbers[r][c] + status + " ");
            }
            System.out.println();
        }
        System.out.println("[ ] = Available   [X] = Occupied\n");
    }

    private static void displayAvailableBeds() {
        List<String> available = system.getAvailableBeds();
        System.out.println("\n--- Available Beds ---");
        if (available.isEmpty()) {
            System.out.println("No beds are available.");
        } else {
            System.out.println(String.join(" ", available));
        }
        System.out.println();
    }

    private static void displayOccupiedBeds() {
        Map<String, String> occupied = system.getOccupiedBeds();
        System.out.println("\n--- Occupied Beds ---");
        if (occupied.isEmpty()) {
            System.out.println("No beds are currently occupied.");
        } else {
            for (Map.Entry<String, String> entry : occupied.entrySet()) {
                System.out.println(entry.getKey() + " - Patient ID: " + entry.getValue());
            }
        }
        System.out.println();
    }

    // ---------- Feature 3: Reports ----------

    private static void generateFullReport() {
        System.out.println("\n===== MediCare Full Report =====");
        displayAllPatients();
        displayAvailableBeds();
        displayOccupiedBeds();
        System.out.println("Total registered patients: " + system.getTotalPatients());
        System.out.println("Total occupied beds: " + system.getTotalOccupiedBeds() + " / " + system.getTotalBeds());
        System.out.printf("Ward occupancy: %.1f%%%n", system.getOccupancyPercentage());
        System.out.println("=================================\n");
    }
}

