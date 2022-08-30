package com.ljm.boot.lowcode.service.system.impl;

import com.alibaba.fastjson.JSONObject;

import com.ljm.boot.lowcode.config.JwtTokenConfig;
import com.ljm.boot.lowcode.model.system.SysErrorLog;
import com.ljm.boot.lowcode.model.system.SysLoginLog;
import com.ljm.boot.lowcode.model.system.SysOperationLog;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import com.ljm.boot.lowcode.repository.system.SysErrorLogRepository;
import com.ljm.boot.lowcode.repository.system.SysLoginLogRepository;
import com.ljm.boot.lowcode.repository.system.SysOperationLogRepository;
import com.ljm.boot.lowcode.repository.system.SysUserRepository;
import com.ljm.boot.lowcode.service.system.SysLogService;
import com.ljm.boot.lowcode.util.DateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Dominick Li
 * @CreateTime 2020/4/22 15:03
 * @description
 **/
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLoginLogRepository sysLoginLogRepository;

    @Autowired
    private SysOperationLogRepository sysOperationLogRepository;

    @Autowired
    private SysErrorLogRepository sysErrorLogRepository;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;
    @Autowired
    private SysUserRepository sysUserRepository;

    @Value("${jwt.tokenHead}")
    private String tokenKey;

    @Override
    public JsonResult findLoginLog(PageParam<SysLoginLog> pageParam) {
        Specification<SysLoginLog> querySpecifi = new Specification<SysLoginLog>() {
            @Override
            public Predicate toPredicate(Root<SysLoginLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                pageParam.setPredicate(predicates, cb, root);
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        pageParam.initPageable();
        return JsonResult.successResult(this.sysLoginLogRepository.findAll(querySpecifi, pageParam.getPageable()));
    }

    @Override
    public JsonResult findOperationLoginLog(PageParam<SysOperationLog> pageParam) {
        Specification<SysOperationLog> querySpecifi = new Specification<SysOperationLog>() {
            @Override
            public Predicate toPredicate(Root<SysOperationLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                pageParam.setPredicate(predicates, cb, root);
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        pageParam.initPageable();
        return JsonResult.successResult(sysOperationLogRepository.findAll(querySpecifi, pageParam.getPageable()));
    }

    @Override
    public JsonResult findErrorLog(PageParam<SysErrorLog> pageParam) {
        Specification<SysErrorLog> querySpecifi = new Specification<SysErrorLog>() {
            @Override
            public Predicate toPredicate(Root<SysErrorLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                pageParam.setPredicate(predicates, cb, root);
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        pageParam.initPageable();
        return JsonResult.successResult(this.sysErrorLogRepository.findAll(querySpecifi, pageParam.getPageable()));
    }


    @Override
    public JsonResult deleteLoginlog(List<Integer> idList) {
        sysLoginLogRepository.deleteAllByIdIn(idList);
        return JsonResult.successResult();
    }

    @Override
    public void saveOperationLog(JoinPoint joinPoint, String methodName, String module, String description) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SysOperationLog sysOperationLog = new SysOperationLog();
        sysOperationLog.setIpAddr(request.getRemoteAddr());
        sysOperationLog.setModule(module);
        sysOperationLog.setDescription(description);
        sysOperationLog.setUsername(sysUserRepository.getOne(jwtTokenConfig.getUserIdFromToken(request.getHeader("token"))).getUsername());
        sysOperationLog.setContent(JSONObject.toJSONString(getRequestParamsByJoinPoint(joinPoint)));
        sysOperationLogRepository.save(sysOperationLog);
    }


    @Override
    public JsonResult deleteErrorlog(List<Integer> idList) {
        sysErrorLogRepository.deleteAllByIdIn(idList);
        return JsonResult.successResult();
    }


    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        Map<String, Object> requestParams = new HashMap<>(paramNames.length);
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(tokenKey)) {
                //过滤token参数
                continue;
            }
            Object value = paramValues[i];
            //如果是文件对象
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename();  //获取文件名
            } else if (value instanceof StandardMultipartHttpServletRequest) {
                StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) value;
                value = req.getParameterMap().get("filename");
                // MultipartFile file=
            } else if (value instanceof HttpServletRequest || value instanceof HttpServletResponse) {
                continue;
            }
            requestParams.put(paramNames[i], value);
        }
        return requestParams;
    }

    private Map<String, Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = joinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

}
