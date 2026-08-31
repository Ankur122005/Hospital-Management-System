package com.hms.service;

import com.hms.dao.AppointmentDao;
import com.hms.dao.BillDao;
import com.hms.dao.PatientDao;
import com.hms.dao.PrescriptionDao;
import com.hms.model.Appointment;
import com.hms.model.Bill;
import com.hms.model.Patient;
import com.hms.model.Prescription;

import java.sql.Date;
import java.util.List;

public class PatientService {

    private final PatientDao patientDao;
    private final AppointmentDao appointmentDao;
    private final PrescriptionDao prescriptionDao;
    private final BillDao billDao;

    public PatientService() {
        this.patientDao = new PatientDao();
        this.appointmentDao = new AppointmentDao();
        this.prescriptionDao = new PrescriptionDao();
        this.billDao = new BillDao();
    }

    public Patient getPatientProfile(int patientId) {
        return patientDao.getPatientById(patientId);
    }

    public boolean updatePatientProfile(Patient patient) {
        return patientDao.updatePatient(patient);
    }

    // Book an appointment with basic business logic validation
    public boolean bookAppointment(Appointment appointment) {
        Date today = new Date(System.currentTimeMillis());

        // Prevent booking appointments in the past
        if (appointment.getAppointmentDate().before(today)) {
            System.err.println("Validation Error: Cannot book an appointment in the past.");
            return false;
        }

        // In a full production app, you would also check if the doctor is already booked for this time slot here.

        return appointmentDao.bookAppointment(appointment);
    }

    public List<Appointment> getPatientAppointments(int patientId) {
        return appointmentDao.getAppointmentsByPatientId(patientId);
    }

    public List<Prescription> getPatientPrescriptions(int patientId) {
        return prescriptionDao.getPrescriptionsByPatientId(patientId);
    }

    public List<Bill> getPatientBills(int patientId) {
        return billDao.getBillsByPatientId(patientId);
    }
}