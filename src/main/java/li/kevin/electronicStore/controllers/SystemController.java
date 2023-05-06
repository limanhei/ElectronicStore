package li.kevin.electronicStore.controllers;

import li.kevin.electronicStore.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    SystemService systemService;
    @RequestMapping("ping")
    public String ping() {
        return systemService.ping();
    }
}
