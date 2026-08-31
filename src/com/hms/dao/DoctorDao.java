package com.hms.dao;

import com.hms.model.Doctor;
import com.hms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao {

    public boolean addDoctor(Doctor doctor) {
        String query = "INSERT INTO doctors (name, email, password, specialty, phone, qualification, consultation_fee) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, doctor.getName());
            pstmt.setString(2, doctor.getEmail());
            pstmt.setString(3, doctor.getPassword());
            pstmt.setString(4, doctor.getSpecialty());
            pstmt.setString(5, doctor.getPhone());
            pstmt.setString(6, doctor.getQualification());
            pstmt.setBigDecimal(7, doctor.getConsultationFee());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
            return false;
        }
    }

    public Doctor getDoctorById(int doctorId) {
        String query = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, doctorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractDoctorFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor by ID: " + e.getMessage());
        }
        return null;
    }

    public Doctor getDoctorByEmail(String email) {
        String query = "SELECT * FROM doctors WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractDoctorFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor by email: " + e.getMessage());
        }
        return null;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors ORDER BY name ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all doctors: " + e.getMessage());
        }
        return doctors;
    }

    public boolean updateDoctor(Doctor doctor) {
        String query = "UPDATE doctors SET name=?, email=?, specialty=?, phone=?, qualification=?, consultation_fee=? WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, doctor.getName());
            pstmt.setString(2, doctor.getEmail());
            pstmt.setString(3, doctor.getSpecialty());
            pstmt.setString(4, doctor.getPhone());
            pstmt.setString(5, doctor.getQualification());
            pstmt.setBigDecimal(6, doctor.getConsultationFee());
            pstmt.setInt(7, doctor.getDoctorId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDoctor(int doctorId) {
        String query = "DELETE FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, doctorId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }

    private Doctor extractDoctorFromResultSet(ResultSet rs) throws SQLException {
        return new Doctor(
                rs.getInt("doctor_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("specialty"),
                rs.getString("phone"),
                rs.getString("qualification"),
                rs.getBigDecimal("consultation_fee")
        );
    }
}