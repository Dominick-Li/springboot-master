package com.ljm.boot.lowcode.model.dto;

import com.ljm.boot.lowcode.enums.ConditionTypeEnum;
import lombok.Data;

import java.util.List;


/**
 * @author Dominick Li
 * @description 通用查询条件
 **/
@Data
public class UniversalQueryParamDTO {

    /**
     * 初始化加载
     */
    private boolean firstInit;

    /**
     * 表的id
     */
    private Long metaId;

    /**
     * 当前页
     *
     * @required
     */
    private Integer currentPage;

    /**
     * 每页显示的数量
     *
     * @required
     */
    private Integer pageSize;

    /**
     * 查询条件列表
     */
    private List<ConditionFiled> conditionList;


    @Data
    public static class ConditionFiled implements Comparable<ConditionFiled> {

        /**
         * 字段名称
         */
        private String name;

        /**
         * 字段编码
         */
        private String code;

        /**
         * 查询条件类型
         */
        @javax.persistence.Convert(converter = ConditionTypeEnum.TypeConverter.class)
        private ConditionTypeEnum type;

        /**
         * 查询条件的值
         */
        private String value;

        /**
         * 日期范围查询条件
         */
        private List<String> betweenValue;

        public ConditionFiled() {
        }

        public ConditionFiled(String name, String value, ConditionTypeEnum conditionTypeEnum) {
            this.name = name;
            this.value = value;
            this.type = conditionTypeEnum;
        }

        public ConditionFiled(String name, String code, String dataType) {
            this.name = name;
            this.code = code;
            this.type = ConditionTypeEnum.getValueByType(dataType);
        }

        @Override
        public int compareTo(ConditionFiled o) {
            return this.getType().getValue() - o.getType().getValue();
        }
    }

}
