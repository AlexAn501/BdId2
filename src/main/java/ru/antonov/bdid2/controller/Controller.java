package ru.antonov.bdid2.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.antonov.bdid2.service.FirstService;

@RestController()
@AllArgsConstructor
public class Controller {

    private final FirstService firstService;

    @GetMapping("/start")
    public void start(){
       firstService.execute();
    }
}
