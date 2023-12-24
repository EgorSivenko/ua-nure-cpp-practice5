package ua.nure.cpp.sivenko.practice5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {
    private long itemId;
    private String itemName;
    private long itemCategory;
    private BigDecimal appraisedValue;
    private BigDecimal marketPriceMax;
    private BigDecimal marketPriceMin;
    private ItemStatus itemStatus;

    enum ItemStatus {
        PAWNED, REDEEMED, PAWNSHOP_PROPERTY;

        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }
}