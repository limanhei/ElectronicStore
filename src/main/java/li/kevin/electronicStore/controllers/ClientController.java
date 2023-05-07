package li.kevin.electronicStore.controllers;

import li.kevin.electronicStore.entities.BasketItem;
import li.kevin.electronicStore.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService service;

    @PostMapping("addBasketItem")
    public BasketItem addBasketItem(@RequestBody BasketItem basketItem) {
        return service.saveBasketItem(basketItem);
    }

    @GetMapping("basket/{clientName}")
    public Iterable<BasketItem> findBasket(@PathVariable String clientName) {
        return service.getBasket(clientName);
    }

    @PutMapping("updateBasketItem")
    public BasketItem updateBasketItem(@RequestBody BasketItem basketItem) {
        return service.updateBasketItem(basketItem);
    }

    @DeleteMapping("removeBasketItem/{id}")
    public String removeBasketItem(@PathVariable int id) {
        return service.removeBasketItem(id);
    }

}
