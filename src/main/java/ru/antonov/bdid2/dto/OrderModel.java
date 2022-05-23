package ru.antonov.bdid2.dto;

import lombok.Data;

@Data
public class OrderModel {
    private String regionId;

    /**
     * Тип дела
     */
    private String caseTypeId;

    /**
     * Основание приема заявления
     */
    private String applicationBasisId;


    private String lastName;
    private String firstname;
    private String middleName;
    private String genderId;

    /**
     * Страна рождения
     */
    private String birthCountryId;
    private String birthDate;

    /**
     * Гражданство из документа заявителя
     */
    private String citizenshipId;

    /**
     * Решение по делу
     */
    private String decisionTypeId;

    /**
     * Основание решения
     */
    private String basisId;

    /**
     * Дата принятия решения
     */
    private String decisionDate;
}
