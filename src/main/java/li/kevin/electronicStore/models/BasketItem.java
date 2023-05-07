package li.kevin.electronicStore.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BasketItem {
    @Id @GeneratedValue
    private int id;

    private String clientName;
    private String productName;
    private int quantity;

    public BasketItem() {
    }

    public BasketItem(String clientName, String productName, int quantity) {
        this.clientName = clientName;
        this.productName = productName;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasketItem that = (BasketItem) o;

        if (!clientName.equals(that.clientName)) return false;
        return productName.equals(that.productName);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + productName.hashCode();
        return result;
    }
}
