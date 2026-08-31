package com.hms.dao;

import com.hms.model.Bill;
import com.hms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDao {

    public boolean generateBill(Bill bill) {
        String query = "INSERT INTO bills (patient_id, appointment_id, particulars, amount, status, payment_method, bill_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bill.getPatientId());

            // Handle optional appointment_id
            if (bill.getAppointmentId() != null && bill.getAppointmentId() > 0) {
                pstmt.setInt(2, bill.getAppointmentId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            pstmt.setString(3, bill.getParticulars());
            pstmt.setBigDecimal(4, bill.getAmount());
            pstmt.setString(5, bill.getStatus() != null ? bill.getStatus() : "Pending");
            pstmt.setString(6, bill.getPaymentMethod());
            pstmt.setDate(7, bill.getBillDate() != null ? bill.getBillDate() : new Date(System.currentTimeMillis()));

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error generating bill: " + e.getMessage());
            return false;
        }
    }

    public List<Bill> getBillsByPatientId(int patientId) {
        return fetchBills("WHERE b.patient_id = ?", patientId);
    }

    public List<Bill> getAllBills() {
        return fetchBills("", null);
    }

    public boolean updatePaymentStatus(int billId, String status, String paymentMethod) {
        String query = "UPDATE bills SET status = ?, payment_method = ? WHERE bill_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setString(2, paymentMethod);
            pstmt.setInt(3, billId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating bill status: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBill(int billId) {
        String query = "DELETE FROM bills WHERE bill_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, billId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting bill: " + e.getMessage());
            return false;
        }
    }

    // Centralized fetch method joining the patients table to get the patient name
    private List<Bill> fetchBills(String condition, Integer param) {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT b.*, p.name AS patient_name " +
                "FROM bills b " +
                "JOIN patients p ON b.patient_id = p.patient_id " +
                condition + " ORDER BY b.bill_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (param != null) {
                pstmt.setInt(1, param);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(extractBillFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bills: " + e.getMessage());
        }
        return bills;
    }

    private Bill extractBillFromResultSet(ResultSet rs) throws SQLException {
        int aptId = rs.getInt("appointment_id");
        Integer appointmentId = rs.wasNull() ? null : aptId;

        Bill bill = new Bill(
                rs.getInt("bill_id"),
                rs.getInt("patient_id"),
                appointmentId,
                rs.getString("particulars"),
                rs.getBigDecimal("amount"),
                rs.getString("status"),
                rs.getString("payment_method"),
                rs.getDate("bill_date")
        );
        // Map the joined UI display field
        bill.setPatientName(rs.getString("patient_name"));
        return bill;
    }
}