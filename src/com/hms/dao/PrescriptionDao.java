package com.hms.dao;

import com.hms.model.Prescription;
import com.hms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDao {

    public boolean addPrescription(Prescription prescription) {
        String query = "INSERT INTO prescriptions (appointment_id, diagnosis, medicines, instructions, prescribed_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, prescription.getAppointmentId());
            pstmt.setString(2, prescription.getDiagnosis());
            pstmt.setString(3, prescription.getMedicines());
            pstmt.setString(4, prescription.getInstructions());
            pstmt.setDate(5, prescription.getPrescribedDate() != null ? prescription.getPrescribedDate() : new Date(System.currentTimeMillis()));

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding prescription: " + e.getMessage());
            return false;
        }
    }

    public List<Prescription> getPrescriptionsByPatientId(int patientId) {
        return fetchPrescriptions("WHERE a.patient_id = ?", patientId);
    }

    public Prescription getPrescriptionByAppointmentId(int appointmentId) {
        List<Prescription> results = fetchPrescriptions("WHERE pr.appointment_id = ?", appointmentId);
        return results.isEmpty() ? null : results.get(0);
    }

    public boolean updatePrescription(Prescription prescription) {
        String query = "UPDATE prescriptions SET diagnosis = ?, medicines = ?, instructions = ? WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, prescription.getDiagnosis());
            pstmt.setString(2, prescription.getMedicines());
            pstmt.setString(3, prescription.getInstructions());
            pstmt.setInt(4, prescription.getPrescriptionId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating prescription: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePrescription(int prescriptionId) {
        String query = "DELETE FROM prescriptions WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, prescriptionId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting prescription: " + e.getMessage());
            return false;
        }
    }

    // Centralized method to execute SELECT queries with JOINs for UI display names
    private List<Prescription> fetchPrescriptions(String condition, int param) {
        List<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT pr.*, p.name AS patient_name, d.name AS doctor_name " +
                "FROM prescriptions pr " +
                "JOIN appointments a ON pr.appointment_id = a.appointment_id " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                condition + " ORDER BY pr.prescribed_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, param);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    prescriptions.add(extractPrescriptionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching prescriptions: " + e.getMessage());
        }
        return prescriptions;
    }

    private Prescription extractPrescriptionFromResultSet(ResultSet rs) throws SQLException {
        Prescription rx = new Prescription(
                rs.getInt("prescription_id"),
                rs.getInt("appointment_id"),
                rs.getString("diagnosis"),
                rs.getString("medicines"),
                rs.getString("instructions"),
                rs.getDate("prescribed_date")
        );
        // Map the joined UI display fields
        rx.setPatientName(rs.getString("patient_name"));
        rx.setDoctorName(rs.getString("doctor_name"));
        return rx;
    }
}