package ru.antonov.bdid2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@Builder
public class OrderModel implements Comparable<OrderModel>{
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

    /**
     * Дата принятия решения
     */
    @JsonProperty(value = "Дата принятия решения")
    private String decisionDate;

    @JsonProperty(value = "Фамилия")
    private String lastName;

    @JsonProperty(value = "Имя")
    private String firstname;

    @JsonProperty(value = "Отчество")
    private String middleName;

    @JsonProperty(value = "Дата рождения")
    private String birthDate;

    @JsonProperty(value = "Пол")
    private String genderId;

    /**
     * Гражданство из документа заявителя
     */
    @JsonProperty(value = "Гражданство")
    private String citizenshipId;

    /**
     * Страна рождения
     */
    @JsonProperty(value = "Страна рождения")
    private String birthCountryId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderModel that = (OrderModel) o;
        return Objects.equals(regionId, that.regionId) && Objects.equals(caseTypeId, that.caseTypeId) &&
            Objects.equals(applicationBasisId, that.applicationBasisId) && Objects.equals(decisionDate, that.decisionDate) &&
            Objects.equals(lastName, that.lastName) && Objects.equals(firstname, that.firstname) && Objects.equals(middleName, that.middleName) &&
            Objects.equals(birthDate, that.birthDate) && Objects.equals(genderId, that.genderId) && Objects.equals(citizenshipId, that.citizenshipId) &&
            Objects.equals(birthCountryId, that.birthCountryId) && Objects.equals(decisionTypeId, that.decisionTypeId) &&
            Objects.equals(basisId, that.basisId);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(regionId, caseTypeId, applicationBasisId, decisionDate, lastName, firstname, middleName, birthDate, genderId, citizenshipId, birthCountryId, decisionTypeId,
                basisId);
    }

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public int compareTo(OrderModel o) {
        return LocalDate.parse(this.getDecisionDate(),dateTimeFormatter).compareTo(LocalDate.parse(o.getDecisionDate(),dateTimeFormatter));
    }
}
