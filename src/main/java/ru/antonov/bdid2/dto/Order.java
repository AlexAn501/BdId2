package ru.antonov.bdid2.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Order {
    private Long regionId;
    private Long caseTypeId;
    private Long applicationBasis_id;
    private String lastName;
    private String firstname;
    private String middleName;
    private Long genderId;
    private Long birthCountryId;
    private LocalDate birthDate;
    private Long citizenshipId;
}
