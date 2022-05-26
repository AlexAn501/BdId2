package ru.antonov.bdid2.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Order {
    /**
     * Регион
     */
    private Long regionId;

    /**
     * Тип дела
     */
    private Long caseTypeId;

    /**
     * Основание приема заявления
     */
    private Long applicationBasisId;


    private String lastName;
    private String firstname;
    private String middleName;
    private Long genderId;

    /**
     * Страна рождения
     */
    private Long birthCountryId;
    private LocalDate birthDate;

    /**
     * Гражданство из документа заявителя
     */
    private Long citizenshipId;

    /**
     * Решение по делу
     */
    private Long decisionTypeId;

    /**
     * Основание решения
     */
    private Long basisId;

    /**
     * Дата принятия решения
     */
    private LocalDate decisionDate;
}
