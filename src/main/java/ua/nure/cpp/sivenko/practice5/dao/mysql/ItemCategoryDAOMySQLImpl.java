package ua.nure.cpp.sivenko.practice5.dao.mysql;

import ua.nure.cpp.sivenko.practice5.ConnectionFactory;
import ua.nure.cpp.sivenko.practice5.dao.ItemCategoryDAO;
import ua.nure.cpp.sivenko.practice5.model.ItemCategory;
import ua.nure.cpp.sivenko.practice5.model.Pawnbroker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemCategoryDAOMySQLImpl implements ItemCategoryDAO {
    private static final String GET_BY_ID = "SELECT * FROM item_categories WHERE item_category_id = ?";
    private static final String GET_ALL = "SELECT * FROM item_categories";

    private static final String INSERT = "INSERT INTO item_categories(item_category_name) VALUES (?)";
    private static final String UPDATE = "UPDATE item_categories " +
            "SET item_category_name = ? WHERE item_category_id = ?";
    private static final String DELETE = "DELETE FROM item_categories WHERE item_category_id = ?";

    private static final String INSERT_PAWNBROKER_SPECIALIZATION = "INSERT INTO pawnbroker_specialization VALUES (?, ?)";

    @Override
    public ItemCategory getItemCategoryById(long itemCategoryId) {
        if (itemCategoryId < 1) {
            throw new IllegalArgumentException("ItemCategory id cannot be <= 0");
        }
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(GET_BY_ID)) {
            ps.setLong(1, itemCategoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapItemCategory(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ItemCategory> getAllItemCategories() {
        List<ItemCategory> itemCategories = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createMySQLConnection();
             Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(GET_ALL)) {
                while (rs.next()) {
                    itemCategories.add(mapItemCategory(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itemCategories;
    }

    @Override
    public void addItemCategory(ItemCategory itemCategory) {
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ps_pawn_spec = connection.prepareStatement(INSERT_PAWNBROKER_SPECIALIZATION)) {
            ps.setString(1, itemCategory.getItemCategoryName());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    for (Pawnbroker pawnbroker : itemCategory.getActivePawnbrokers()) {
                        long pawnbrokerId = pawnbroker.getPawnbrokerId();
                        ps_pawn_spec.setLong(1, pawnbrokerId);
                        ps_pawn_spec.setLong(2, keys.getLong(1)); // itemCategoryId

                        ps_pawn_spec.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItemCategoryName(long itemCategoryId, String itemCategoryName) {
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, itemCategoryName);
            ps.setLong(2, itemCategoryId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteItemCategory(long itemCategoryId) {
        if (itemCategoryId < 1) {
            throw new IllegalArgumentException("ItemCategory id cannot be <= 0");
        }
        try (Connection connection = ConnectionFactory.createMySQLConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setLong(1, itemCategoryId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ItemCategory mapItemCategory(ResultSet rs) throws SQLException {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryId(rs.getLong("item_category_id"));
        itemCategory.setItemCategoryName(rs.getString("item_category_name"));
        return itemCategory;
    }
}