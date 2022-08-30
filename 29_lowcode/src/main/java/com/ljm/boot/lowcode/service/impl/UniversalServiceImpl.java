package com.ljm.boot.lowcode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ljm.boot.lowcode.enums.ConditionTypeEnum;
import com.ljm.boot.lowcode.model.MetaColumn;
import com.ljm.boot.lowcode.model.MetaInfo;
import com.ljm.boot.lowcode.model.dto.UniversalDeleteDTO;
import com.ljm.boot.lowcode.model.dto.UniversalSaveDTO;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.dto.UniversalQueryParamDTO;
import com.ljm.boot.lowcode.model.vo.MetaColumnVO;
import com.ljm.boot.lowcode.repository.MetaInfoRepository;
import com.ljm.boot.lowcode.service.UniversalService;
import com.ljm.boot.lowcode.util.DateUtil;
import com.ljm.boot.lowcode.util.IdWorkerUtil;
import com.ljm.boot.lowcode.util.NativeQueryUtil;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Dominick Li
 * @CreateTime 2022/6/12 16:13
 * @Description 封控通用的CRUD操作
 **/
@Service
public class UniversalServiceImpl implements UniversalService {

    @Autowired
    private MetaInfoRepository metaInfoRepository;

    @Autowired
    private NativeQueryUtil nativeQueryUtil;

    @Override
    public JsonResult query(UniversalQueryParamDTO universalQueryParam) {
        MetaInfo metaInfo = metaInfoRepository.getOne(universalQueryParam.getMetaId());
        //组装查询的sql
        StringBuilder querySql = new StringBuilder("select id,");
        for (MetaColumn metaColumn : metaInfo.getMetaColumnList()) {
            if (metaColumn.isViewShow()) {
                querySql.append(metaColumn.getColumnCode()).append(",");
            }
        }
        querySql.deleteCharAt(querySql.length() - 1);
        querySql.append(" from ").append(metaInfo.getTableCode()).append(" where 1=1");
        List<UniversalQueryParamDTO.ConditionFiled> conditionFiledList = universalQueryParam.getConditionList();
        //对查询条件排序 优化查询性能
        conditionFiledList = conditionFiledList.stream().sorted().collect(Collectors.toList());
        List<Object> condition = new ArrayList<>();
        Integer index = 1;

        //拼接查询条件
        for (UniversalQueryParamDTO.ConditionFiled conditionFiled : conditionFiledList) {
            if (StringUtils.hasText(conditionFiled.getValue())) {
                if (conditionFiled.getType() == ConditionTypeEnum.EQUALS) {
                    //等值查询
                    querySql.append(" and ").append(conditionFiled.getCode());
                    querySql.append(" =?").append(index);
                    condition.add(conditionFiled.getValue());
                    index++;
                }  else {
                    //模糊查询
                    querySql.append(" and ").append(conditionFiled.getCode());
                    querySql.append(" like CONCAT('%',?").append(index).append(",'%')");
                    condition.add(conditionFiled.getValue());
                    index++;
                }
            }else if(!Collections.isEmpty(conditionFiled.getBetweenValue())){
                //日期范围查询
                querySql.append(" and ").append(conditionFiled.getCode()).append(">=?").append(index);
                condition.add(conditionFiled.getBetweenValue().get(0));
                index++;
                querySql.append(" and ").append(conditionFiled.getCode()).append("<=?").append(index);
                condition.add(DateUtil.getDateStrIncrement(conditionFiled.getBetweenValue().get(1)));
                index++;
            }
        }

        String sql = querySql.toString();
        //查询符合条件的记录数
        String countSql = "select count(id) " + sql.substring(sql.indexOf("from"));
        //拼接分页的条件
        querySql.append(" limit ").append((universalQueryParam.getCurrentPage() - 1) * universalQueryParam.getPageSize()).append(",").append(universalQueryParam.getPageSize());
        //执行查询sql获取执行结果
        List<Object[]> result = nativeQueryUtil.query(querySql.toString(), condition);
        //执行获取总记录数的sql结果用于分页使用
        List<BigInteger> pageResult = nativeQueryUtil.query(countSql, condition);
        //总记录数
        Integer totalElements = pageResult.get(0).intValue();
        //总页数
        Integer totalPages = totalElements % universalQueryParam.getPageSize() == 0 ? totalElements / universalQueryParam.getPageSize() : totalElements / universalQueryParam.getPageSize() + 1;
        //把结果和字段映射起来
        List<JSONObject> tabel = new ArrayList<>();
        JSONObject tr;
        //列名称用于显示表格的title信息
        List<String> shouColumnCode = metaInfo.getMetaColumnList().stream().filter(MetaColumn::isViewShow).map(MetaColumn::getColumnCode).collect(Collectors.toList());
        for (Object[] objects : result) {
            tr = new JSONObject();
            tr.put("id", objects[0].toString());
            for (int i = 1; i < objects.length; i++) {
                tr.put(shouColumnCode.get(i - 1), objects[i]);
            }
            tabel.add(tr);
        }

        JSONObject jsonObject = new JSONObject();
        if (universalQueryParam.isFirstInit()) {
            //获取当前表的所有字段,用于在添加和修改页面进行使用
            List<MetaColumnVO> metaColumnVOList = new ArrayList<>();
            metaInfo.getMetaColumnList().forEach(metaColumn -> metaColumnVOList.add(new MetaColumnVO(metaColumn)));
            jsonObject.put("metaColumnList", metaColumnVOList);

            //组装列表页的查询条件
            List<List<MetaColumnVO>> searchMetaColumnList = new ArrayList<>();
            List<MetaColumnVO> allSearchMetaColumnList = metaColumnVOList.stream().filter(metaColumnVO -> metaColumnVO.isSearch()).collect(Collectors.toList());
            int count = 0, size = allSearchMetaColumnList.size();
            //一行显示3个查询条件,使用二维集合封装
            while (count < size) {
                searchMetaColumnList.add(allSearchMetaColumnList.subList(count, Math.min((count + 3), size)));
                count += 3;
            }
            //封装查询条件对象用于页面上展示
            List<List<UniversalQueryParamDTO.ConditionFiled>> conditionList = new ArrayList<>();
            List<UniversalQueryParamDTO.ConditionFiled> conditionFileds;
            for (List<MetaColumnVO> columnVOList : searchMetaColumnList) {
                conditionFileds = new ArrayList<>();
                conditionList.add(conditionFileds);
                for (MetaColumnVO mc : columnVOList) {
                    conditionFileds.add(new UniversalQueryParamDTO.ConditionFiled(mc.getName(), mc.getCode(), mc.getDataType()));
                }
            }
            jsonObject.put("searchMetaColumnList", conditionList);
        }

        jsonObject.put("metaName", metaInfo.getMetaName());
        jsonObject.put("tabelData", tabel);
        jsonObject.put("totalElements", totalElements);
        jsonObject.put("totalPages", totalPages);
        return JsonResult.successResult(jsonObject);
    }

    @Override
    public JsonResult save(UniversalSaveDTO universalSaveDTO) {
        MetaInfo metaInfo = metaInfoRepository.getOne(universalSaveDTO.getMetaId());
        StringBuilder sql = new StringBuilder();
        String title;
        Optional<MetaColumnVO> metaColumnVOOptional = universalSaveDTO.getData().stream().filter(metaColumnVO1 -> metaColumnVO1.getCode().equals("id")).findFirst();
        if (metaColumnVOOptional.isPresent()) {
            title = "修改";
            //修改
            sql.append("update ").append(metaInfo.getTableCode()).append(" set ");
            for (MetaColumnVO metaColumnVO : universalSaveDTO.getData()) {
                sql.append(metaColumnVO.getCode()).append("='").append(metaColumnVO.getValue()).append("',");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" where id=").append(metaColumnVOOptional.get().getValue());
        } else {
            title = "新增";
            //新增
            sql.append("insert into ").append(metaInfo.getTableCode()).append("(");
            if (!metaInfo.isIncrement()) {
                sql.append("id,");
            }
            StringBuilder valueSb = new StringBuilder();
            for (MetaColumnVO metaColumnVO : universalSaveDTO.getData()) {
                sql.append(metaColumnVO.getCode()).append(",");
                valueSb.append("'").append(metaColumnVO.getValue()).append("',");
            }
            sql.deleteCharAt(sql.length() - 1);
            valueSb.deleteCharAt(valueSb.length() - 1);
            sql.append(") values(");
            if (!metaInfo.isIncrement()) {
                sql.append("'").append(IdWorkerUtil.getId()).append("',");
            }
            sql.append(valueSb);
            sql.append(")");
        }
        boolean flag = nativeQueryUtil.executeUpdate(sql.toString());
        if (flag) {
            return JsonResult.successResult(title + "成功");
        }
        return JsonResult.failureResult(title + "失败");
    }

    @Override
    public JsonResult delete(UniversalDeleteDTO universalDeleteDTO) {
        MetaInfo metaInfo = metaInfoRepository.getOne(universalDeleteDTO.getMetaId());
        StringBuilder sb = new StringBuilder("delete from ");
        sb.append(metaInfo.getTableCode()).append(" where id in (");
        for (Object o : universalDeleteDTO.getIdList()) {
            sb.append(o);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        boolean flag = nativeQueryUtil.executeUpdate(sb.toString());
        if (flag) {
            return JsonResult.successResult("删除成功");
        }
        return JsonResult.failureResult("删除失败");
    }

}
