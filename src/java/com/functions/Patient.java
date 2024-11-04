package com.functions;

import com.database.DatabaseConnector;
import com.database.Handler;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.*;

/**
 * Represents a patient.
 */
public abstract class Patient extends Person {
    private String gender;
    private String symptoms;
    private String paymentMethod;
    private String diagnosis;
    private boolean emergency;
    private String roomNumber = "N/A";
    private int doctorInCharge;
    private static int lastId;
    private final int ID;
    private static final Set<Patient> patients = new HashSet<>();
    private static final Map<Integer, Patient> patientsMap = new HashMap<>();

    private static final String UPDATE_PATIENT_SQL = "UPDATE patients SET name = ?, `address` = ?, `phoneNumber` = ?, `gender` = ?, `symptoms` = ?, `paymentMethod` = ?, `diagno` = ?, `emergency` = ?, `roomNumber` = ?, `doctorid` = ? WHERE (`idpatients` = ?);";
    private static final String DELETE_PATIENT_SQL = "DELETE FROM patients WHERE idpatients= ?";
    private static final String INSERT_PATIENT_SQL = "INSERT INTO patients (idpatients, name, address, phoneNumber, gender, symptoms, paymentMethod, diagno, emergency, roomNumber, doctorid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_MAX_ID_SQL = "SELECT MAX(idpatients) AS idpatients FROM patients";
    private static final String UPDATE_DIAGNOSIS_SQL = "UPDATE patients SET diagno = ? WHERE idpatients = ?";
    private static final String VACATE_ROOM_SQL = "UPDATE patients SET roomNumber = NULL WHERE idpatients = ?";
    private static final String DISCHARGE_PATIENT_SQL = "UPDATE patients SET emergency = 0 WHERE idpatients = ?";

    static {
        lastId = loadLastId();
        patientsMap.putAll(Handler.loadPatients());
        patients.addAll(patientsMap.values());
    }

    public Patient(String name, String address, String phoneNumber, String gender,
                   String symptoms, String paymentMethod, boolean emergency, int doctorInCharge) {
        super(name, address, phoneNumber);
        this.gender = gender;
        this.symptoms = symptoms;
        this.paymentMethod = paymentMethod;
        this.emergency = emergency;
        this.ID = ++lastId;
        this.doctorInCharge = doctorInCharge;
        addPatient(this);
    }

    public Patient(int ID, String name, String address, String phoneNumber, String gender,
                   String symptoms, String paymentMethod, boolean emergency, int doctorInCharge) {
        super(name, address, phoneNumber);
        this.gender = gender;
        this.symptoms = symptoms;
        this.paymentMethod = paymentMethod;
        this.emergency = emergency;
        this.ID = ID;
        this.doctorInCharge = doctorInCharge;
        addPatient(this);
    }

    private void addPatient(Patient patient) {
        patients.add(patient);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public int getID() {
        return ID;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = (roomNumber == null || roomNumber == 0) ? "N/A" : String.valueOf(roomNumber);
    }

    public void setDoctorInCharge(int doctorInCharge) {
        this.doctorInCharge = doctorInCharge;
    }

    public int getDoctorInCharge() {
        return doctorInCharge;
    }

    public static void updateSet(ObservableList<Patient> newPatients) {
        patients.clear();
        patients.addAll(newPatients);
    }

    public void editPatient(String newName, String newAddress, String newPhoneNumber, String newGender,
                            String newSymptoms, String newPaymentMethod, String newRoomNumber, Boolean newEmergency, Integer doctorInCharge) {
        this.name = (newName != null) ? newName : this.name;
        this.address = (newAddress != null) ? newAddress : this.address;
        this.phoneNumber = (newPhoneNumber != null) ? newPhoneNumber : this.phoneNumber;
        this.gender = (newGender != null) ? newGender : this.gender;
        this.symptoms = (newSymptoms != null) ? newSymptoms : this.symptoms;
        this.paymentMethod = (newPaymentMethod != null) ? newPaymentMethod : this.paymentMethod;
        this.emergency = (newEmergency != null) ? newEmergency : this.emergency;
        this.roomNumber = (newEmergency != null && newEmergency) ? newRoomNumber : "N/A";
        this.doctorInCharge = (doctorInCharge != null) ? doctorInCharge : this.doctorInCharge;
    }

    public static boolean save(Patient patient) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStmt = connection.prepareStatement(UPDATE_PATIENT_SQL)) {
            preparedStmt.setString(1, patient.getName());
            preparedStmt.setString(2, patient.getAddress());
            preparedStmt.setString(3, patient.getPhoneNumber());
            preparedStmt.setString(4, patient.getGender());
            preparedStmt.setString(5, patient.getSymptoms());
            preparedStmt.setString(6, patient.getPaymentMethod());
            preparedStmt.setString(7, patient.getDiagnosis());
            preparedStmt.setInt(8, patient.isEmergency() ? 1 : 0);
            preparedStmt.setString(9, patient.isEmergency() ? patient.getRoomNumber() : "0");
            preparedStmt.setInt(10, patient.getDoctorInCharge());
            preparedStmt.setInt(11, patient.getID());
            preparedStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving patient: " + e.getMessage(), e);
        }
    }

    public static boolean delete(Patient patient) {
        patients.remove(patient);
        for (Appointment A : Appointment.loadAppointments()) {
            if (A.getPatientId() == patient.getID()) {
                Appointment.delete(A);
            }
        }

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStmt = connection.prepareStatement(DELETE_PATIENT_SQL)) {
            preparedStmt.setInt(1, patient.getID());
            preparedStmt.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting patient: " + e.getMessage(), e);
        }
    }

    public static boolean add(Patient patient) {
        patients.add(patient);
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStmt = connection.prepareStatement(INSERT_PATIENT_SQL)) {
            preparedStmt.setInt(1, patient.getID());
            preparedStmt.setString(2, patient.getName());
            preparedStmt.setString(3, patient.getAddress());
            preparedStmt.setString(4, patient.getPhoneNumber());
            preparedStmt.setString(5, patient.getGender());
            preparedStmt.setString(6, patient.getSymptoms());
            preparedStmt.setString(7, patient.getPaymentMethod());
            preparedStmt.setString(8, patient.getDiagnosis());
            preparedStmt.setInt(9, patient.isEmergency() ? 1 : 0);
            preparedStmt.setString(10, patient.isEmergency() ? patient.getRoomNumber() : "0");
            preparedStmt.setInt(11, patient.getDoctorInCharge());
            preparedStmt.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error adding patient: " + e.getMessage(), e);
        }
    }

    public static boolean diagnose(Patient patient) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DIAGNOSIS_SQL)) {
            statement.setString(1, patient.getDiagnosis());
            statement.setInt(2, patient.getID());
            statement.executeUpdate();
            System.out.println("Patient diagnosed");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error diagnosing patient: " + e.getMessage(), e);
        }
    }

    public boolean vacate() {
        this.roomNumber = "N/A";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(VACATE_ROOM_SQL)) {
            statement.setInt(1, this.getID());
            statement.executeUpdate();
            System.out.println("Patient vacated");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error vacating patient: " + e.getMessage(), e);
        }
    }

    public boolean discharge() {
        this.setEmergency(false);
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(DISCHARGE_PATIENT_SQL)) {
            statement.setInt(1, this.getID());
            statement.executeUpdate();
            System.out.println("Patient discharged");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error discharging patient: " + e.getMessage(), e);
        }
    }

    private static int loadLastId() {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(SELECT_MAX_ID_SQL);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("idpatients");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading last ID: " + e.getMessage(), e);
        }
        return 1; // Default value if loading fails
    }

    public static Set<Patient> loadPatients() {
        return patients;
    }

    public static Set<Patient> loadToHashSet() {
        return new HashSet<>(patientsMap.values());
    }

    @Override
    public String toString() {
        return "Patient{[%d] %s %s %s %s %s %s %s %b %d}".formatted(getID(), name, address, phoneNumber, gender, symptoms, paymentMethod, diagnosis, emergency, doctorInCharge);
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}