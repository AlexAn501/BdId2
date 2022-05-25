package ru.antonov.bdid2.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.antonov.bdid2.dto.In;
import ru.antonov.bdid2.service.ArchiveCreatorService;
import ru.antonov.bdid2.service.FirstService;
import ru.antonov.bdid2.util.SplitUtils;

import java.nio.file.Paths;

@RestController()
@AllArgsConstructor
public class Controller {

    private final FirstService firstService;
    private final ArchiveCreatorService archiveCreatorService;

    @PostMapping("/start")
    public String start(@RequestBody In in){
        return firstService.executeFromFile(in);
    }

    @PostMapping("/delimiter")
    public void delimiter(@RequestBody In in) {
        SplitUtils.createFiles(Paths.get(in.getPathToCsv()));
    }

    @PostMapping("/archive")
    public void createArchive(@RequestBody In in){
        archiveCreatorService.execute(in);
    }
}
