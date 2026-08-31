package com.hms.service;

import com.hms.dao.AppointmentDao;
import com.hms.dao.DoctorDao;
import com.hms.dao.PrescriptionDao;
import com.hms.model.Appointment;
import com.hms.model.Doctor;
import com.hms.model.Prescription;

import java.util.List;

public class DoctorService {

    private final DoctorDao doctorDao;
    private final AppointmentDao appointmentDao;
    private final PrescriptionDao prescriptionDao;

    public DoctorService() {
        this.doctorDao = new DoctorDao();
        this.appointmentDao = new AppointmentDao();
        this.prescriptionDao = new PrescriptionDao();
    }

    public Doctor getDoctorProfile(int doctorId) {
        return doctorDao.getDoctorById(doctorId);
    }

    public boolean updateDoctorProfile(Doctor doctor) {
        return doctorDao.updateDoctor(doctor);
    }

    public List<Appointment> getDoctorAppointments(int doctorId) {
        return appointmentDao.getAppointmentsByDoctorId(doctorId);
    }

    public boolean updateAppointmentStatus(int appointmentId, String status) {
        return appointmentDao.updateAppointmentStatus(appointmentId, status);
    }

    // Add a prescription and automatically mark the appointment as 'Completed'
    public boolean issuePrescription(Prescription prescription) {
        boolean isAdded = prescriptionDao.addPrescription(prescription);

        if (isAdded) {
            // Business Logic: If a prescription is given, the appointment is effectively complete
            appointmentDao.updateAppointmentStatus(prescription.getAppointmentId(), "Completed");
        }

        return isAdded;
    }

    public Prescription getPrescriptionForAppointment(int appointmentId) {
        return prescriptionDao.getPrescriptionByAppointmentId(appointmentId);
    }
}