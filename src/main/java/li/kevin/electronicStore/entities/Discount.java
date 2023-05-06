package li.kevin.electronicStore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Discount {
    @Id @GeneratedValue
    private int id;

    private String productName;
    private int criteriaQuantity;
    private BigDecimal discountedPercent;

    private BigDecimal discountedPrice;

    public Discount() {
    }

    public Discount(String productName, int criteriaQuantity, BigDecimal discountedPercent, BigDecimal discountedPrice) {
        this.productName = productName;
        this.criteriaQuantity = criteriaQuantity;
        this.discountedPercent = discountedPercent;
        this.discountedPrice = discountedPrice;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCriteriaQuantity() {
        return criteriaQuantity;
    }

    public void setCriteriaQuantity(int criteriaQuantity) {
        this.criteriaQuantity = criteriaQuantity;
    }

    public BigDecimal getDiscountedPercent() {
        return discountedPercent;
    }

    public void setDiscountedPercent(BigDecimal discountedPercent) {
        this.discountedPercent = discountedPercent;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discount discount = (Discount) o;

        if (id != discount.id) return false;
        return productName.equals(discount.productName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + productName.hashCode();
        return result;
    }
}
