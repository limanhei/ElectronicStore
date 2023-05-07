package li.kevin.electronicStore.services;

import li.kevin.electronicStore.entities.BasketItem;
import li.kevin.electronicStore.repositories.BasketItemRepository;
import li.kevin.electronicStore.repositories.DiscountRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ClientService {
    private ProductRepository productRepository;
    private DiscountRepository discountRepository;
    private BasketItemRepository basketItemRepository;

    @Autowired
    public ClientService(ProductRepository productRepository, DiscountRepository discountRepository, BasketItemRepository basketItemRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.basketItemRepository = basketItemRepository;
    }

    public BasketItem saveBasketItem(BasketItem basketItem) {
        return basketItemRepository.save(basketItem);
    }

    public Iterable<BasketItem> getBasket(String clientName) {
        return basketItemRepository.findByClientName(clientName);
    }

    public String removeBasketItem(int id) {
        if(basketItemRepository.existsById(id)) {
            basketItemRepository.deleteById(id);
            return id + " deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " not found!");
        }
    }

    public BasketItem updateBasketItem(BasketItem basketItem) {
        if(basketItemRepository.existsById(basketItem.getId())) {
            return basketItemRepository.save(basketItem);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, basketItem.getId() + " not found!");
        }
    }
}
