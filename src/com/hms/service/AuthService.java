package com.hms.service;

import com.hms.dao.DoctorDao;
import com.hms.dao.PatientDao;
import com.hms.model.Doctor;
import com.hms.model.Patient;

public class AuthService {

    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    public AuthService() {
        this.patientDao = new PatientDao();
        this.doctorDao = new DoctorDao();
    }

    // Authenticate Patient
    public Patient loginPatient(String email, String password) {
        Patient patient = patientDao.getPatientByEmail(email);

        // Note: In a production app, use BCrypt to verify hashed passwords instead of plain text
        if (patient != null && patient.getPassword().equals(password)) {
            return patient;
        }
        return null; // Invalid credentials
    }

    // Authenticate Doctor
    public Doctor loginDoctor(String email, String password) {
        Doctor doctor = doctorDao.getDoctorByEmail(email);

        if (doctor != null && doctor.getPassword().equals(password)) {
            return doctor;
        }
        return null; // Invalid credentials
    }

    // Register a new Patient with validation
    public boolean registerPatient(Patient patient) {
        // Check if the email is already registered
        if (patientDao.getPatientByEmail(patient.getEmail()) != null) {
            System.err.println("Registration failed: Email already exists.");
            return false;
        }

        // Proceed to save to database
        return patientDao.addPatient(patient);
    }
}