package li.kevin.electronicStore.services;

import li.kevin.electronicStore.entities.Product;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AdminService {
    private ProductRepository repository;

    public AdminService(@Autowired ProductRepository repository) {
        this.repository = repository;
    }

    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    public Iterable<Product> getProducts() {
        return repository.findAll();
    }

    public String removeProduct(String name) {
        getProductOrThrow(name);
        repository.deleteById(name);
        return name + " deleted";
    }

    public Product updateProduct(Product product) {
        Product existingProduct = getProductOrThrow(product.getName());
        existingProduct.setPrice(product.getPrice());
        return repository.save(existingProduct);
    }

    private Product getProductOrThrow(String name) {
        return repository.findById(name).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, name + " not found!"));
    }
}
