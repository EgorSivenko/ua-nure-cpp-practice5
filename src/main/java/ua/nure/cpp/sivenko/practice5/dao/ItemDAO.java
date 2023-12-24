package ua.nure.cpp.sivenko.practice5.dao;

import ua.nure.cpp.sivenko.practice5.model.Item;
import ua.nure.cpp.sivenko.practice5.model.Item.ItemStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ItemDAO {
    Item getItemById(long itemId);
    List<Item> getItemsByCategory(long itemCategoryId);
    List<Item> getItemsByStatus(ItemStatus itemStatus);
    List<Item> getAllItems();

    void addItem(Item item);
    void updateItemAppraisedValue(long itemId, BigDecimal appraisedValue);
    void deleteItem(long itemId);
}
