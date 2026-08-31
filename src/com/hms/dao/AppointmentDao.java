package com.hms.dao;

import com.hms.model.Appointment;
import com.hms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDao {

    public boolean bookAppointment(Appointment appt) {
        String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, time_slot, status, symptoms) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, appt.getPatientId());
            pstmt.setInt(2, appt.getDoctorId());
            pstmt.setDate(3, appt.getAppointmentDate());
            pstmt.setString(4, appt.getTimeSlot());
            pstmt.setString(5, appt.getStatus() != null ? appt.getStatus() : "Scheduled");
            pstmt.setString(6, appt.getSymptoms());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error booking appointment: " + e.getMessage());
            return false;
        }
    }

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        return fetchAppointments("WHERE a.patient_id = ?", patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        return fetchAppointments("WHERE a.doctor_id = ?", doctorId);
    }

    public List<Appointment> getAllAppointments() {
        return fetchAppointments("", null);
    }

    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String query = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, appointmentId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAppointment(int appointmentId) {
        String query = "DELETE FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, appointmentId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }

    // Centralized method to execute SELECT queries with JOINs
    private List<Appointment> fetchAppointments(String condition, Integer param) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT a.*, p.name AS patient_name, d.name AS doctor_name, d.specialty AS department " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                condition + " ORDER BY a.appointment_date DESC, a.time_slot ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (param != null) {
                pstmt.setInt(1, param);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
        }
        return appointments;
    }

    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        Appointment appt = new Appointment(
                rs.getInt("appointment_id"),
                rs.getInt("patient_id"),
                rs.getInt("doctor_id"),
                rs.getDate("appointment_date"),
                rs.getString("time_slot"),
                rs.getString("status"),
                rs.getString("symptoms")
        );
        // Map the joined UI display fields
        appt.setPatientName(rs.getString("patient_name"));
        appt.setDoctorName(rs.getString("doctor_name"));
        appt.setDepartment(rs.getString("department"));
        return appt;
    }
}