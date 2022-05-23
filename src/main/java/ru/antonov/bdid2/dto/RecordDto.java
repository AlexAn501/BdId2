package ru.antonov.bdid2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDto {
    /**
     * Идентификатор
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long recordId;
    /**
     * Код спавочника
     */
    private String catalogCode;
    /**
     * Признак актуальной версии
     */
    private boolean lastVersion;
    /**
     * Статус
     */
    private String statusCode;
    /**
     * Дата актуальности с
     */
    private String actualStartDt;
    /**
     * Дата актуальности по
     */
    private String actualEndDt;
    /**
     * Дата создания
     */
    private String createDttm;
    /**
     * Дата последней модификации
     */
    private String modifyDttm;
    /**
     * Атрибуты
     */
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

    /**
     * Получение списка атрибутов в виде строковых значений.
     * @return набор атрибутов записи в строковом представлении
     */
    public Map<String, String> getAttributesAsString() {
        if (attributeSet == null) {
            return Collections.emptyMap();
        }

        Map<String, String> attributes = new HashMap<>();
        for (Map.Entry<String, Object> val : attributeSet.entrySet()) {
            attributes.put(val.getKey(), val.getValue() != null ? val.getValue().toString() : null);
        }
        return attributes;
    }

    public String getCatalogCode() {
        return catalogCode;
    }
}
