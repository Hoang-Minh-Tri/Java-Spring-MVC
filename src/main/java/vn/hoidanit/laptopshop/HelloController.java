package vn.hoidanit.laptopshop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot Khiêm 3 Nt!";
    }

    @GetMapping("/user")
    public String userpage() {
        return "Dành cho user";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "Dành cho admin";
    }
}
