package com.ljm.boot.lowcode.controller;

import com.ljm.boot.lowcode.annotation.OperationLog;
import com.ljm.boot.lowcode.annotation.PrintLog;
import com.ljm.boot.lowcode.model.dto.UniversalDeleteDTO;
import com.ljm.boot.lowcode.model.dto.UniversalSaveDTO;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.dto.UniversalQueryParamDTO;
import com.ljm.boot.lowcode.service.UniversalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Dominick Li
 * @description 通用查询，修改,删除
 **/
@RestController
@RequestMapping("/universal")
public class UniversalController {

    @Autowired
    private UniversalService universalService;

    /**
     * 通用分页查询
     */
    @PostMapping("/query")
    @PrintLog
    public JsonResult query(@RequestBody UniversalQueryParamDTO universalQueryParam) {
        return universalService.query(universalQueryParam);
    }

    @PostMapping("/")
    @OperationLog(module = "元数据通用",description = "添加或修改")
    @PrintLog
    public JsonResult save(@RequestBody UniversalSaveDTO universalSaveDTO) {
        return universalService.save(universalSaveDTO);
    }

    @PostMapping("/delete")
    @OperationLog(module = "元数据通用组件",description = "删除")
    @PrintLog
    public JsonResult delete(@RequestBody UniversalDeleteDTO universalDeleteDTO) {
        return universalService.delete(universalDeleteDTO);
    }

}
