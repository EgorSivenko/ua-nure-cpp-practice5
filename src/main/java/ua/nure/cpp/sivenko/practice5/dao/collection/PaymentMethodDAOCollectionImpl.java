package ua.nure.cpp.sivenko.practice5.dao.collection;

import ua.nure.cpp.sivenko.practice5.dao.PaymentMethodDAO;
import ua.nure.cpp.sivenko.practice5.model.PaymentMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PaymentMethodDAOCollectionImpl implements PaymentMethodDAO {
    private final List<PaymentMethod> paymentMethods = new ArrayList<>();
    private final AtomicInteger id = new AtomicInteger(1);

    public PaymentMethodDAOCollectionImpl() {
        addPaymentMethod("Cash");
        addPaymentMethod("Credit Card");
    }

    @Override
    public PaymentMethod getPaymentMethodById(long paymentMethodId) {
        return paymentMethods.stream()
                .filter(paymentMethod -> paymentMethod.getPaymentMethodId() == paymentMethodId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethods;
    }

    @Override
    public void addPaymentMethod(String paymentMethodName) {
        paymentMethods.add(new PaymentMethod(id.getAndIncrement(), paymentMethodName));
    }

    @Override
    public void updatePaymentMethodName(long paymentMethodId, String paymentMethodName) {
        paymentMethods.get((int) paymentMethodId - 1).setPaymentMethodName(paymentMethodName);
    }

    @Override
    public void deletePaymentMethod(long paymentMethodId) {
        paymentMethods.remove((int) paymentMethodId - 1);
    }
}
