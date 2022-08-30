package com.ljm.boot.lowcode.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;

/**
 * @Author Dominick Li
 * @Description 条件查询类型
 **/
public enum ConditionTypeEnum {

    EQUALS(0),//等值查询 =
    BETWEEN(1),//日期范围查询  filed >= 2020-12-24  && filed <= 2021-12-24
    LIKE(2); //模糊查询  like % filed %

    @JsonValue
    private Integer value;

    public Integer getValue() {
        return value;
    }

    ConditionTypeEnum(Integer value) {
        this.value = value;
    }

    public static ConditionTypeEnum valueOf(Integer val) {
        for (ConditionTypeEnum conditionTypeEnum : values()) {
            if (conditionTypeEnum.getValue() == val) {
                return conditionTypeEnum;
            }
        }
        return null;
    }

    /**
     * 根据字段类型获取对欧元的枚举对象
     */
    public static ConditionTypeEnum getValueByType(String type) {
        if (type.equals(DataTypeEnum.INT.getValue()) || type.equals(DataTypeEnum.LONG.getValue()) || type.equals(DataTypeEnum.BOOLEAN.getValue())) {
            //等值查询
            return EQUALS;
        } else if (type.equals(DataTypeEnum.STRING.getValue()) || type.equals(DataTypeEnum.TEXT.getValue()) || type.equals(DataTypeEnum.MEDIUM_TEXT.getValue())) {
            //模糊查询
            return LIKE;
        } else {
            //日期范围查询
            return BETWEEN;
        }
    }


    public static class TypeConverter implements AttributeConverter<ConditionTypeEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(ConditionTypeEnum conditionTypeEnum) {
            if (conditionTypeEnum == null) {
                return null;
            }
            return conditionTypeEnum.getValue();
        }

        @Override
        public ConditionTypeEnum convertToEntityAttribute(Integer value) {
            if (value == null) {
                return null;
            }
            return ConditionTypeEnum.valueOf(value);
        }
    }
}