package ua.nure.cpp.sivenko.practice5.dao;

import ua.nure.cpp.sivenko.practice5.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodDAO {
    PaymentMethod getPaymentMethodById(long paymentMethodId);
    List<PaymentMethod> getAllPaymentMethods();

    void addPaymentMethod(String paymentMethodName);
    void updatePaymentMethodName(long paymentMethodId, String paymentMethodName);
    void deletePaymentMethod(long paymentMethodId);
}
