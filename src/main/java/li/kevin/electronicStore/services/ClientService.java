package li.kevin.electronicStore.services;

import li.kevin.electronicStore.models.*;
import li.kevin.electronicStore.repositories.BasketItemRepository;
import li.kevin.electronicStore.repositories.DiscountRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class ClientService {
    private ProductRepository productRepository;
    private DiscountRepository discountRepository;
    private BasketItemRepository basketItemRepository;

    @Autowired
    public ClientService(ProductRepository productRepository,
                         DiscountRepository discountRepository,
                         BasketItemRepository basketItemRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.basketItemRepository = basketItemRepository;
    }

    public BasketItem saveBasketItem(BasketItem basketItem) {
        return basketItemRepository.save(basketItem);
    }

    public List<BasketItem> getBasket(String clientName) {
        return basketItemRepository.findByClientName(clientName);
    }

    public String removeBasketItem(int id) {
        if (basketItemRepository.existsById(id)) {
            basketItemRepository.deleteById(id);
            return id + " deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " not found!");
        }
    }

    public BasketItem updateBasketItem(BasketItem basketItem) {
        if (basketItemRepository.existsById(basketItem.getId())) {
            return basketItemRepository.save(basketItem);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, basketItem.getId() + " not found!");
        }
    }

    public Receipt calculateReceipt(String clientName) {
        List<ReceiptItem> receiptItems = new ArrayList<>();
        getBasket(clientName).forEach(basketItem -> {
            Product product = productRepository.findById(basketItem.getProductName()).orElseThrow();
            List<Discount> discounts = discountRepository.findByProductName(basketItem.getProductName());

            ArrayList<BigDecimal> itemPrices = new ArrayList<>(
                    Collections.nCopies(basketItem.getQuantity(), product.getPrice()));

            Set<Discount> appliedDiscounts = new HashSet<>();

            for (int i = 1; i <= itemPrices.size(); i++) {
                for (Discount discount: discounts) {
                    if (i % discount.getCriteriaQuantity() == 0) {
                        appliedDiscounts.add(discount);
                        if (discount.getDiscountedPercent() != null) {
                            itemPrices.set(i - 1, itemPrices.get(i - 1).multiply(discount.getDiscountedPercent()
                                    .divide(new BigDecimal("100.00"), RoundingMode.HALF_UP)));
                        }
                        if (discount.getDiscountedPrice() != null) {
                            itemPrices.set(i - 1, itemPrices.get(i - 1).subtract(discount.getDiscountedPrice()));
                        }
                    }
                }
            }

            BigDecimal subTotal = itemPrices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            receiptItems.add(new ReceiptItem(product,
                    basketItem.getQuantity(),
                    appliedDiscounts,
                    subTotal.setScale(2, RoundingMode.HALF_UP)));
        });

        BigDecimal total = receiptItems.stream().map(ReceiptItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return new Receipt(receiptItems, total);
    }
}
