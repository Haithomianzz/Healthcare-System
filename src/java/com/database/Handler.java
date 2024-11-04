package com.database;

import com.company.Main;
import com.functions.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles database operations for loading and verifying data.
 */
public class Handler {

    /**
     * Loads doctors from the database.
     *
     * @return a map of doctor IDs to Doctor objects
     */
    public static Map<Integer, Doctor> loadDoctors() {
        Map<Integer, Doctor> doctors = new HashMap<>();

        String sql = "SELECT * FROM doctors";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("idDoctors");
                Doctor doctor = new Doctor(
                        id,
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("specialty"));

                doctors.put(id, doctor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    /**
     * Loads patients from the database.
     *
     * @return a map of patient IDs to Patient objects
     */
    public static Map<Integer, Patient> loadPatients() {
        Map<Integer, Patient> patientHashMap = new HashMap<>();

        String sql = "SELECT * FROM patients";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("idpatients");
                Patient patient;
                if (resultSet.getInt("emergency") == 1) {
                    patient = new EmergencyPatient(
                            id,
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getString("phoneNumber"),
                            resultSet.getString("gender"),
                            resultSet.getString("symptoms"),
                            resultSet.getString("paymentMethod"),
                            resultSet.getInt("roomNumber"),
                            resultSet.getBoolean("emergency"),
                            resultSet.getInt("doctorid"));
                    patient.setDiagnosis(resultSet.getString("diagno"));
                } else {
                    patient = new NormalPatient(
                            id,
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getString("phoneNumber"),
                            resultSet.getString("gender"),
                            resultSet.getString("symptoms"),
                            resultSet.getString("paymentMethod"),
                            resultSet.getBoolean("emergency"),
                            resultSet.getInt("doctorid"));
                    patient.setRoomNumber(resultSet.getInt("roomNumber"));
                    patient.setDiagnosis(resultSet.getString("diagno"));
                }
                patientHashMap.put(id, patient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patientHashMap;
    }

    /**
     * Loads appointments from the database.
     *
     * @return a map of appointment IDs to Appointment objects
     */
    public static Map<Integer, Appointment> loadAppointments() {
        Map<Integer, Appointment> appointments = new HashMap<>();

        String sql = "SELECT * FROM appointments";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("idappointments");
                Appointment appointment = new Appointment(id,
                        resultSet.getInt("physicianId"),
                        resultSet.getInt("patientId"),
                        resultSet.getString("date"));
                appointments.put(id, appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /**
     * Verifies user credentials.
     *
     * @param username the username
     * @param password the password
     * @return true if credentials are valid, false otherwise
     */
    public static boolean verifyCredentials(String username, String password) {
        String sql = "SELECT * FROM userdata WHERE username = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int storedId = resultSet.getInt("doctorid");
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(password)) {
                    if (storedId != 0) {
                        for (Doctor d : Doctor.getDoctors()) {
                            if (d.getID() == storedId) {
                                Main.setCurrentDoctor(d);
                                Main.setUserType("Doctor");
                            }
                        }
                    } else {
                        Main.setUserType("Admin");
                    }
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}