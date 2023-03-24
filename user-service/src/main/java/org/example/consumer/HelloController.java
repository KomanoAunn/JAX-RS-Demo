package org.example.consumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/say/hello")
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
