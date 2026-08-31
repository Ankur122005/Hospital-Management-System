package com.hms.service;

import com.hms.dao.BillDao;
import com.hms.model.Bill;

import java.util.List;

public class BillingService {

    private final BillDao billDao;

    public BillingService() {
        this.billDao = new BillDao();
    }

    public boolean generateBill(Bill bill) {
        // Business Logic Validation: Ensure bill amount is strictly positive
        if (bill.getAmount() == null || bill.getAmount().signum() <= 0) {
            System.err.println("Validation Error: Bill amount must be greater than zero.");
            return false;
        }
        return billDao.generateBill(bill);
    }

    public boolean processPayment(int billId, String paymentMethod) {
        // Automatically updates the bill status to 'Paid' upon processing
        return billDao.updatePaymentStatus(billId, "Paid", paymentMethod);
    }

    public List<Bill> getPatientBills(int patientId) {
        return billDao.getBillsByPatientId(patientId);
    }

    public List<Bill> getAllBills() {
        return billDao.getAllBills();
    }
}