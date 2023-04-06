package com.example.demo.inner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final MyService service;

    public Controller(MyService service){

        this.service = service;
    }

    @GetMapping("/name")
    public Controller.Dummy getDummy() {
        return new Dummy(service.calcName());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Dummy {
        String name;
    }
}
