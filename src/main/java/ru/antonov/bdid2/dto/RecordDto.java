package ru.antonov.bdid2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDto {
    /**
     * Атрибуты
     */
    @JsonProperty(namespace = "attributeSet")
    private Map<String, Object> attributeSet;

    /**
     * Выбор наименования из атрибутов.
     * @return наименование элемента справочника
     */
    public String getName() {
        return getField("name");
    }

    /**
     * Выбор одного из атрибутов по наименованию.
     * @param fieldName наименование атрибута
     * @return значение атрибута
     */
    public String getField(String fieldName) {
        String defaultValue = null;
        if (attributeSet != null) {
            return (String) attributeSet.getOrDefault(fieldName, defaultValue);
        }
        return defaultValue;
    }
}
