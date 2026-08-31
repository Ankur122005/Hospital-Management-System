package com.hms.model;

import java.math.BigDecimal;

public class Doctor {
    private int doctorId;
    private String name;
    private String email;
    private String password;
    private String specialty;
    private String phone;
    private String qualification;
    private BigDecimal consultationFee;

    // Default constructor
    public Doctor() {}

    // Constructor for registering/adding a new doctor
    public Doctor(String name, String email, String password, String specialty, String phone, String qualification, BigDecimal consultationFee) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.specialty = specialty;
        this.phone = phone;
        this.qualification = qualification;
        this.consultationFee = consultationFee;
    }

    // Full constructor for retrieving existing records from database
    public Doctor(int doctorId, String name, String email, String password, String specialty, String phone, String qualification, BigDecimal consultationFee) {
        this.doctorId = doctorId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.specialty = specialty;
        this.phone = phone;
        this.qualification = qualification;
        this.consultationFee = consultationFee;
    }

    // Getters and Setters
    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", qualification='" + qualification + '\'' +
                ", consultationFee=" + consultationFee +
                '}';
    }
}