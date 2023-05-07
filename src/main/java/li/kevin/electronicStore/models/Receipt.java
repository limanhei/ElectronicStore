package li.kevin.electronicStore.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {
    private List<ReceiptItem> receiptItems;

    private BigDecimal total;

    public Receipt() {
    }

    public Receipt(List<ReceiptItem> receiptItems, BigDecimal total) {
        ArrayList<ReceiptItem> sortedReceiptItems = new ArrayList<>(receiptItems);
        Collections.sort(sortedReceiptItems);
        this.total = total;
        this.receiptItems = sortedReceiptItems;
    }

    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;
        if (!receiptItems.equals(receipt.receiptItems)) return false;
        return total.equals(receipt.total);
    }

    @Override
    public int hashCode() {
        int result = receiptItems.hashCode();
        result = 31 * result + total.hashCode();
        return result;
    }
}
