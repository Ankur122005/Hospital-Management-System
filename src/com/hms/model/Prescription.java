package com.hms.model;

import java.sql.Date;

public class Prescription {
    private int prescriptionId;
    private int appointmentId;
    private String diagnosis;
    private String medicines;
    private String instructions;
    private Date prescribedDate;

    // Optional fields for UI tables (populated via JOIN queries)
    private String doctorName;
    private String patientName;

    // Default constructor
    public Prescription() {}

    // Constructor for creating a new prescription record
    public Prescription(int appointmentId, String diagnosis, String medicines, String instructions, Date prescribedDate) {
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.medicines = medicines;
        this.instructions = instructions;
        this.prescribedDate = prescribedDate;
    }

    // Full constructor for retrieving records from the database
    public Prescription(int prescriptionId, int appointmentId, String diagnosis, String medicines, String instructions, Date prescribedDate) {
        this.prescriptionId = prescriptionId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.medicines = medicines;
        this.instructions = instructions;
        this.prescribedDate = prescribedDate;
    }

    // Getters and Setters
    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Date getPrescribedDate() {
        return prescribedDate;
    }

    public void setPrescribedDate(Date prescribedDate) {
        this.prescribedDate = prescribedDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", appointmentId=" + appointmentId +
                ", diagnosis='" + diagnosis + '\'' +
                ", prescribedDate=" + prescribedDate +
                '}';
    }
}