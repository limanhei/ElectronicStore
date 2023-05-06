package li.kevin.electronicStore.controllers;

import li.kevin.electronicStore.entities.Product;
import li.kevin.electronicStore.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService service;

    @PostMapping("addProduct")
    public Product addProduct(@RequestBody Product product) {
        return service.saveProduct(product);
    }

    @GetMapping("products")
    public Iterable<Product> findAllProducts() {
        return service.getProducts();
    }

    @PutMapping("updateProduct")
    public Product updateProduct(@RequestBody Product product) {
        return service.updateProduct(product);
    }

    @DeleteMapping("removeProduct/{name}")
    public String removeProduct(@PathVariable String name) {
        return service.removeProduct(name);
    }
}
