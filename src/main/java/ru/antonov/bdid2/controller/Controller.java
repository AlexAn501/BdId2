package ru.antonov.bdid2.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.antonov.bdid2.dto.In;
import ru.antonov.bdid2.service.FirstService;

@RestController()
@AllArgsConstructor
public class Controller {

    private final FirstService firstService;

    @PostMapping("/start")
    public String start(@RequestBody In pathToRegion){
        return firstService.executeFromBdByRegion(pathToRegion);
    }
}
