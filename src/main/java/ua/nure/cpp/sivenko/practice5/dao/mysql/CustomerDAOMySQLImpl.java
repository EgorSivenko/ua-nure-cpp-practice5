package ua.nure.cpp.sivenko.practice5.dao.mysql;

import ua.nure.cpp.sivenko.practice5.util.ConnectionFactory;
import ua.nure.cpp.sivenko.practice5.dao.CustomerDAO;
import ua.nure.cpp.sivenko.practice5.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOMySQLImpl implements CustomerDAO {
    private static final String GET_BY_ID = "SELECT * FROM customers WHERE customer_id = ?";
    private static final String GET_BY_CONTACT_NUMBER = "SELECT * FROM customers WHERE contact_number = ?";
    private static final String GET_BY_EMAIL = "SELECT * FROM customers WHERE email = ?";
    private static final String GET_ALL = "SELECT * FROM customers";

    private static final String INSERT = "INSERT INTO customers (first_name, last_name, contact_number, email) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE customers " +
            "SET first_name = ?, last_name = ?, contact_number = ?, email = ? WHERE customer_id = ?";
    private static final String DELETE = "DELETE FROM customers WHERE customer_id = ?";

    @Override
    public Customer getCustomerById(long customerId) {
        if (customerId < 1) {
            throw new IllegalArgumentException("Client id cannot be <= 0");
        }
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(GET_BY_ID)) {
            ps.setLong(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapCustomer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getCustomerByContactNumber(String contactNumber) {
        String contactNumberRegex = "[0-9]{3}-[0-9]{3}-[0-9]{4}";
        if (!contactNumber.matches(contactNumberRegex)) {
            throw new IllegalArgumentException("Invalid contact number");
        }
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(GET_BY_CONTACT_NUMBER)) {
            ps.setString(1, contactNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapCustomer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        String emailRegex = "^[^@]+@[^@]+\\.[^@]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email");
        }
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(GET_BY_EMAIL)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapCustomer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createMySQLConnection();
             Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(GET_ALL)) {
                while (rs.next()) {
                    customers.add(mapCustomer(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public void addCustomer(Customer customer) {
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT)) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getContactNumber());
            ps.setString(4, customer.getEmail());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getContactNumber());
            ps.setString(4, customer.getEmail());
            ps.setLong(5, customer.getCustomerId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCustomer(long customerId) {
        if (customerId < 1) {
            throw new IllegalArgumentException("Client id cannot be <= 0");
        }
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setLong(1, customerId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getLong("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setContactNumber(rs.getString("contact_number"));
        customer.setEmail(rs.getString("email"));
        return customer;
    }
}
