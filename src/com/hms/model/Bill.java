package com.hms.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Bill {
    private int billId;
    private int patientId;
    private Integer appointmentId; // Integer allows for null values if a bill isn't linked to a specific appointment
    private String particulars;
    private BigDecimal amount;
    private String status; // 'Paid' or 'Pending'
    private String paymentMethod;
    private Date billDate;

    // Optional field for UI tables (populated via JOIN queries)
    private String patientName;

    // Default constructor
    public Bill() {}

    // Constructor for generating a new bill
    public Bill(int patientId, Integer appointmentId, String particulars, BigDecimal amount, String status, String paymentMethod, Date billDate) {
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.particulars = particulars;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.billDate = billDate;
    }

    // Full constructor for retrieving existing bills from the database
    public Bill(int billId, int patientId, Integer appointmentId, String particulars, BigDecimal amount, String status, String paymentMethod, Date billDate) {
        this.billId = billId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.particulars = particulars;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.billDate = billDate;
    }

    // Getters and Setters
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public String getParticulars() { return particulars; }
    public void setParticulars(String particulars) { this.particulars = particulars; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", patientId=" + patientId +
                ", particulars='" + particulars + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}