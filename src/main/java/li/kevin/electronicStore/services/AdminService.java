package li.kevin.electronicStore.services;

import li.kevin.electronicStore.models.Discount;
import li.kevin.electronicStore.models.Product;
import li.kevin.electronicStore.repositories.DiscountRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AdminService {
    private ProductRepository productRepository;
    private DiscountRepository discountRepository;

    @Autowired
    public AdminService(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    public String removeProduct(String name) {
        if(productRepository.existsById(name)) {
            productRepository.deleteById(name);
            return name + " deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, name + " not found!");
        }
    }

    public Product updateProduct(Product product) {
        if(productRepository.existsById(product.getName())) {
            return productRepository.save(product);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, product.getName() + " not found!");
        }
    }

    public Discount saveDiscount(Discount discount) {
        if (productRepository.existsById(discount.getProductName())) {
            return discountRepository.save(discount);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Product " + discount.getProductName() + " not found!");
        }
    }

    public Iterable<Discount> getDiscounts() {
        return discountRepository.findAll();
    }

    public String removeDiscount(int id) {
        if(discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
            return "Discount " + id + " deleted";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Discount " + id + " not found!");
        }
    }

    public Discount updateDiscount(Discount discount) {
        if(discountRepository.existsById(discount.getId())) {
            return discountRepository.save(discount);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Discount " + discount.getId() + " not found!");
        }
    }
}
