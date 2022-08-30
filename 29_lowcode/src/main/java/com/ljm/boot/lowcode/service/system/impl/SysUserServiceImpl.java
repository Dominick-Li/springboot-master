package com.ljm.boot.lowcode.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.ljm.boot.lowcode.config.JwtTokenConfig;
import com.ljm.boot.lowcode.enums.RoleTypeEnum;
import com.ljm.boot.lowcode.model.dto.UpdatePasswordDTO;
import com.ljm.boot.lowcode.model.system.*;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import com.ljm.boot.lowcode.model.vo.PageVO;
import com.ljm.boot.lowcode.model.vo.TokenVO;
import com.ljm.boot.lowcode.repository.system.SysLoginLogRepository;
import com.ljm.boot.lowcode.repository.system.SysTokenRepository;
import com.ljm.boot.lowcode.repository.system.SysUserRepository;
import com.ljm.boot.lowcode.service.system.SysRoleService;
import com.ljm.boot.lowcode.service.system.SysUserService;
import com.ljm.boot.lowcode.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;


/**
 * @author Dominick Li
 * @CreateTime 2020/3/17 10:25
 * @description
 **/
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysTokenRepository sysTokenRepository;

    @Autowired
    private SysLoginLogRepository sysLoginLogRepository;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    private SysRoleService sysRoleService;


    @Override
    public JsonResult login(String data, String clientIp) {
        JSONObject object = JSON.parseObject(data);
        SysUser sysUser = sysUserRepository.findByUsernameAndDeleted(object.getString("username"), 0);
        if (sysUser == null) {
            return JsonResult.failureResult("帐号不存在!");
        } else if (!sysUser.getPassword().equals(AESUtil.encrypt(object.getString("password"), sysUser.getSalt()))) {
            return JsonResult.failureResult("密码错误!");
        } else if (sysUser.getEnabled() == 0) {
            return JsonResult.failureResult("用户已被注销了!");
        }
        String roleCode = RoleTypeEnum.valueOf(sysUser.getRoleId()).getName();
        SysToken sysToken = new SysToken(sysUser.getId(), jwtTokenConfig.getToken(sysUser.getId(), roleCode.toUpperCase()));
        sysTokenRepository.deleteAllByUserId(sysUser.getId());
        sysTokenRepository.save(sysToken);
        sysLoginLogRepository.save(new SysLoginLog(sysUser.getUsername(), clientIp));
        List<SysMenu> sysMenuList = sysRoleService.getMenuListByRole(sysUser.getSysRole());
        return JsonResult.successResult(new TokenVO(sysToken.getAccessToken(), sysUser, roleCode, sysMenuList));
    }

    @Override
    public JsonResult save(SysUser sysUser) {
        if (sysUser.getId() == null) {
            SysUser exists = sysUserRepository.findByUsernameAndDeleted(sysUser.getUsername(), 0);
            if (exists != null) {
                return JsonResult.failureResult("用户名已存在!");
            }
            //为了防止原密码一样,加密后的密码也一样的问题,用登录账号拼接密码进行加密
            sysUser.setSalt(UUID.randomUUID().toString().replace("-",""));
            sysUser.setPassword(AESUtil.encrypt(sysUser.getPassword(), sysUser.getSalt()));
        } else {
            SysUser exists = sysUserRepository.findByUsernameAndDeleted(sysUser.getUsername(), 0);
            if (exists != null && !exists.getId().equals(sysUser.getId())) {
                return JsonResult.failureResult("用户名已存在!");
            }
        }
        sysUserRepository.save(sysUser);
        return JsonResult.successResult();
    }

    @Override
    public JsonResult queryByCondition(PageParam<SysUser> pageParam) {
        org.springframework.data.domain.Page<SysUser> page = this.findAllByCondition(pageParam);
        List<SysUser> sysUserList = new ArrayList<>();
        for (SysUser sysUser : page.getContent()) {
            sysUser.setRoleName(sysUser.getSysRole().getRoleName());
            sysUser.setChannelName(sysUser.getSysChannel().getChannelName());
            sysUserList.add(sysUser);
        }
        PageVO<SysUser> pageVo = new PageVO(page.getTotalElements(), page.getTotalPages(), sysUserList);
        return JsonResult.successResult(pageVo);
    }

    @Override
    public JsonResult userEnabled(String data) {
        JSONObject object = JSON.parseObject(data);
        Optional<SysUser> optionalUser = sysUserRepository.findById(object.getInteger("id"));
        SysUser sysUser = optionalUser.get();
        sysUser.setEnabled(object.getInteger("enabled"));
        sysUserRepository.save(sysUser);
        return JsonResult.successResult();
    }


    @Override
    public JsonResult deleteByIdList(List<Integer> idList) {
        sysUserRepository.updateUserDeleteStatu(idList);
        return JsonResult.successResult();
    }

    @Override
    public JsonResult updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        Optional<SysUser> optionalUser = sysUserRepository.findById(updatePasswordDTO.getUserId());
        if (optionalUser.isPresent()) {
            SysUser sysUser = optionalUser.get();
            String oldPwd = AESUtil.encrypt(updatePasswordDTO.getOldPassword(), sysUser.getSalt());
            if (sysUser.getPassword().equals(oldPwd)) {
                sysUser.setPassword(AESUtil.encrypt(updatePasswordDTO.getPassword(), sysUser.getSalt()));
                sysUserRepository.save(sysUser);
                return JsonResult.successResult();
            }
            return JsonResult.failureResult("原密码错误!");
        }
        return JsonResult.failureResult("用户不存在!");
    }




    @Override
    public JsonResult restPassword(Integer id) {
        Optional<SysUser> sysUserOptional = sysUserRepository.findById(id);
        if (sysUserOptional.isPresent()) {
            SysUser sysUser = sysUserOptional.get();
            sysUser.setPassword(AESUtil.encrypt("123456", sysUser.getSalt()));
            sysUserRepository.save(sysUser);
            return JsonResult.successResult();
        }
        return JsonResult.failureResult();
    }

    @Override
    public org.springframework.data.domain.Page<SysUser> findAllByCondition(PageParam<SysUser> pageParam) {
        Specification<SysUser> querySpecifi = new Specification<SysUser>() {
            @Override
            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                pageParam.setPredicate(predicates, cb, root);
                predicates.add(cb.equal(root.get("deleted").as(boolean.class), false));
                predicates.add(cb.notEqual(root.get("username").as(String.class), "admin"));
                // and到一起的话所有条件就是且关系，or就是或关系82
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        pageParam.initPageable();
        return this.sysUserRepository.findAll(querySpecifi, pageParam.getPageable());
    }

}
