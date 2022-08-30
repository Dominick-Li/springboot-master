package com.ljm.boot.lowcode.model.tool;

import com.ljm.boot.lowcode.util.DateUtil;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.Map;


/**
 * @author Dominick Li
 * @description jpa通用分页查询条件
 **/
@Data
public class PageParam<T> {

    /**
     * jpa分页查询器对象
     * @ignore
     */
    private Pageable pageable;

    /**
     * 当前页
     * @required
     */
    private Integer currentPage;

    /**
     * 每页显示的数量
     * @required
     */
    private Integer pageSize;

    /**
     * 日期查询条件起始
     */
    private String beginDate;

    /**
     * 日期查询条件结束
     */
    private String endDate;

    /**
     * 等值查询条件
     */
    private Map<String, Object> eq = new HashMap<>();

    /**
     * 模糊查询条件
     */
    private Map<String, String> like = new HashMap<>();

    public PageParam() {

    }

    public Pageable getPageable() {
        if (pageable == null) {
            this.initPageable();
        }
        return pageable;
    }


    public Map<String, Object> getEq() {
        return eq;
    }


    /**
     * 构建jpa需要的分页对象
     */
    public void initPageable() {
        this.initPageable("createTime");
    }

    public void initPageable(String... order){
        this.initPageable(Sort.Direction.DESC,order);
    }

    /**
     * direction  升序或者降序,默认值为desc=>降序,ase为升序
     * order 根据哪些字段排序
     */
    public void initPageable(Sort.Direction direction, String... order) {
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        this.pageable = org.springframework.data.domain.PageRequest.of(currentPage - 1, pageSize, direction, order);
    }


    /**
     * 注入jpa 查询条件参数
     */
    public void setPredicate(List<Predicate> predicates, CriteriaBuilder cb, Root<T> root) {

        String value = "";
        if (!StringUtils.isEmpty(beginDate)) {
            //大于或等于传入时间
            predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), beginDate));
        }
        if (!StringUtils.isEmpty(endDate)) {
            //小于或等于传入时间
            predicates.add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), DateUtil.getDateStrIncrement(endDate)));
        }

        for (Map.Entry<String, String> entry : like.entrySet()) {
            value = entry.getValue().trim();
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            //过滤掉非法的符号,不然会影响模糊查询
            value = value.replaceAll("%", "////%").replaceAll("_", "////_");
            predicates.add(cb.like(root.get(entry.getKey()).as(String.class), "%" + value + "%"));
        }

        for (Map.Entry<String, Object> entry : eq.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }
            Class className = String.class;
            if (entry.getValue() instanceof Boolean) {
                className = boolean.class;
            }
            predicates.add(cb.equal(root.get(entry.getKey()).as(className), entry.getValue()));
        }
    }
}
