package ua.nure.cpp.sivenko.practice5.dao.mysql;

import ua.nure.cpp.sivenko.practice5.ConnectionFactory;
import ua.nure.cpp.sivenko.practice5.dao.PaymentMethodDAO;
import ua.nure.cpp.sivenko.practice5.model.PaymentMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDAOMySQLImpl implements PaymentMethodDAO {
    private static final String GET_BY_ID = "SELECT * FROM payment_methods WHERE payment_method_id = ?";
    private static final String GET_ALL = "SELECT * FROM payment_methods";

    private static final String INSERT = "INSERT INTO payment_methods(payment_method_name) VALUES (?)";
    private static final String UPDATE = "UPDATE payment_methods " +
            "SET payment_method_name = ? WHERE payment_method_id = ?";
    private static final String DELETE = "DELETE FROM payment_methods WHERE payment_method_id = ?";

    @Override
    public PaymentMethod getPaymentMethodById(long paymentMethodId) {
        if (paymentMethodId < 1) {
            throw new IllegalArgumentException("PaymentMethod id cannot be <= 0");
        }
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(GET_BY_ID)) {
            ps.setLong(1, paymentMethodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapPaymentMethod(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PaymentMethod> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createMySQLConnection();
             Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(GET_ALL)) {
                while (rs.next()) {
                    paymentMethods.add(mapPaymentMethod(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paymentMethods;
    }

    @Override
    public void addPaymentMethod(String paymentMethodName) {
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT)) {
            ps.setString(1, paymentMethodName);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePaymentMethodName(long paymentMethodId, String paymentMethodName) {
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, paymentMethodName);
            ps.setLong(2, paymentMethodId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePaymentMethod(long paymentMethodId) {
        if (paymentMethodId < 1) {
            throw new IllegalArgumentException("PaymentMethod id cannot be <= 0");
        }
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setLong(1, paymentMethodId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PaymentMethod mapPaymentMethod(ResultSet rs) throws SQLException {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentMethodId(rs.getLong("payment_method_id"));
        paymentMethod.setPaymentMethodName(rs.getString("payment_method_name"));
        return paymentMethod;
    }
}
