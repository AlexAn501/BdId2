package ru.antonov.bdid2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderModel {
    @JsonProperty(value = "Регион")
    private String regionId;

    /**
     * Тип дела
     */
    @JsonProperty(value = "Тип дела")
    private String caseTypeId;

    /**
     * Основание приема заявления
     */
    @JsonProperty(value = "Основание приема заявления")
    private String applicationBasisId;

    @JsonProperty(value = "Фамилия")
    private String lastName;
    @JsonProperty(value = "Имя")
    private String firstname;
    @JsonProperty(value = "Отчество")
    private String middleName;
    @JsonProperty(value = "Пол")
    private String genderId;

    /**
     * Страна рождения
     */
    @JsonProperty(value = "Страна рождения")
    private String birthCountryId;
    @JsonProperty(value = "Дата рождения")
    private String birthDate;

    /**
     * Гражданство из документа заявителя
     */
    @JsonProperty(value = "Гражданство")
    private String citizenshipId;

    /**
     * Решение по делу
     */
    @JsonProperty(value = "Решение по делу")
    private String decisionTypeId;

    /**
     * Основание решения
     */
    @JsonProperty(value = "Основание решения")
    private String basisId;

    /**
     * Дата принятия решения
     */
    @JsonProperty(value = "Дата принятия решения")
    private String decisionDate;
}
