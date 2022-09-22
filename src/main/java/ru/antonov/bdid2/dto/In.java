package ru.antonov.bdid2.dto;

import lombok.Data;

import java.util.List;

@Data
public class In {
    String pathToCsv;
    List<String> pathToSortFile;
}
