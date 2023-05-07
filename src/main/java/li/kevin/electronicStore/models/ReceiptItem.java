package li.kevin.electronicStore.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ReceiptItem implements Comparable<ReceiptItem> {

    private Product product;

    private int quantity;

    private Set<Discount> discounts;

    private BigDecimal subTotal;

    public ReceiptItem() {
    }

    public ReceiptItem(Product product, int quantity, Set<Discount> discounts, BigDecimal subTotal) {
        ArrayList<Discount> sortedDiscounts = new ArrayList<>(discounts);
        Collections.sort(sortedDiscounts);
        this.product = product;
        this.quantity = quantity;
        this.discounts = new HashSet<>(sortedDiscounts);
        this.subTotal = subTotal;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptItem that = (ReceiptItem) o;

        if (quantity != that.quantity) return false;
        if (!product.equals(that.product)) return false;
        if (!discounts.equals(that.discounts)) return false;
        return subTotal.equals(that.subTotal);
    }

    @Override
    public int hashCode() {
        int result = product.hashCode();
        result = 31 * result + quantity;
        result = 31 * result + discounts.hashCode();
        result = 31 * result + subTotal.hashCode();
        return result;
    }


    @Override
    public int compareTo(ReceiptItem o) {
        return product.getName().compareTo(o.getProduct().getName());
    }
}
