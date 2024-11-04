package com.functions;

import com.database.DatabaseConnector;
import com.database.Handler;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Doctor extends Person {

    private static final String UPDATE_DOCTOR_SQL = "UPDATE doctors SET name = ?, address = ?, phoneNumber = ?, specialty = ? WHERE idDoctors = ?";
    private static final String DELETE_DOCTOR_SQL = "DELETE FROM doctors WHERE idDoctors = ?";
    private static final String INSERT_DOCTOR_SQL = "INSERT INTO doctors (idDoctors, name, address, phoneNumber, specialty) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_MAX_ID_SQL = "SELECT MAX(idDoctors) AS idDoctors FROM doctors";

    private String specialty;
    private static int lastId;
    private final int ID;
    private static HashSet<Doctor> doctors = new HashSet<>();
    private static Map<Integer, Doctor> doctorMap = new HashMap<>();

    static {
        lastId = loadLastId();
        doctorMap = Handler.loadDoctors();
        doctors = loadToHashSet(); // Load from database after initialization
    }

    public Doctor(String name, String address, String phoneNumber, String specialty) {
        super(name, address, phoneNumber);
        this.specialty = specialty;
        this.ID = ++lastId;
        addDoctor(this);
    }

    public Doctor(int ID, String name, String address, String phoneNumber, String specialty) {
        super(name, address, phoneNumber);
        this.specialty = specialty;
        this.ID = ID;
        addDoctor(this);
    }

    public static void updateSet(ObservableList<Doctor> newDoctors) {
        doctors.clear();
        doctors.addAll(newDoctors);
    }

    public String getSpecialty() {
        return specialty;
    }

    public int getID() {
        return ID;
    }

    public static Set<Doctor> getDoctors() {
        return doctors;
    }

    private void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void editDoctor(String newName, String newAddress, String newPhoneNumber, String newSpecialty) {
        this.name = (newName != null) ? newName : this.name;
        this.address = (newAddress != null) ? newAddress : this.address;
        this.phoneNumber = (newPhoneNumber != null) ? newPhoneNumber : this.phoneNumber;
        this.specialty = (newSpecialty != null) ? newSpecialty : this.specialty;
    }

    public static boolean save(Doctor doctor) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStmt = connection.prepareStatement(UPDATE_DOCTOR_SQL)) {
            preparedStmt.setString(1, doctor.getName());
            preparedStmt.setString(2, doctor.getAddress());
            preparedStmt.setString(3, doctor.getPhoneNumber());
            preparedStmt.setString(4, doctor.getSpecialty());
            preparedStmt.setInt(5, doctor.getID());
            preparedStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving doctor: " + e.getMessage(), e);
        }
    }

    public static boolean delete(Doctor doctor) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStmt = connection.prepareStatement(DELETE_DOCTOR_SQL)) {
            preparedStmt.setInt(1, doctor.getID());
            preparedStmt.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting doctor: " + e.getMessage(), e);
        }
    }

    public static boolean add(Doctor doctor) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStmt = connection.prepareStatement(INSERT_DOCTOR_SQL)) {
            preparedStmt.setInt(1, doctor.getID());
            preparedStmt.setString(2, doctor.getName());
            preparedStmt.setString(3, doctor.getAddress());
            preparedStmt.setString(4, doctor.getPhoneNumber());
            preparedStmt.setString(5, doctor.getSpecialty());
            preparedStmt.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error adding doctor: " + e.getMessage(), e);
        }
    }

    private static int loadLastId() {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(SELECT_MAX_ID_SQL);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("idDoctors");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading last ID: " + e.getMessage(), e);
        }
        return 1; // Default value if loading fails
    }

    public static HashSet<Doctor> loadDoctors() {
        return doctors;
    }

    public static HashSet<Doctor> loadToHashSet() {
        return new HashSet<>(doctorMap.values());
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s %s %s %s", getID(), name, address, phoneNumber, specialty);
    }
}