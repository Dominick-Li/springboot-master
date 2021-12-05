package com.ljm.boot.minio.controller;

import com.ljm.boot.minio.config.MinioConfig;
import com.ljm.boot.minio.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author Dominick Li
 * @CreateTime 2021/12/5 21:42
 * @Description 测试文件上传下载预览和删除
 **/
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private MinioConfig minioConfig;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传文件
     * @param file
     * @param bucketName 桶名称
     * @return 返回对象名称和外链地址
     */
    @PostMapping(value = "/")
    public ResponseEntity<HashMap<String, String>> uploadFile(MultipartFile file, @RequestParam(required = false) String bucketName) {
        bucketName = StringUtils.hasLength(bucketName) ? bucketName : minioConfig.getDefaultBucketName();
        String objectName = minioUtil.getDatePath() + file.getOriginalFilename();
        minioUtil.upload(bucketName, objectName, file);
        String viewPath = minioUtil.getPresignedObjectUrl(bucketName, objectName, 60, TimeUnit.SECONDS);
        HashMap<String, String> objectInfo = new HashMap<>();
        objectInfo.put("objectName", objectName);
        //只能预览图片、txt等部分文件
        objectInfo.put("viewPath", viewPath);
        return ResponseEntity.ok(objectInfo);
    }


    /**
     * 删除指定桶里的某个对象
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return
     */
    @DeleteMapping(value = "/")
    public ResponseEntity<String> deleteByPath(@RequestParam(required = false) String bucketName, String objectName) {
        bucketName = StringUtils.hasLength(bucketName) ? bucketName : minioConfig.getDefaultBucketName();
        minioUtil.delete(bucketName, objectName);
        return ResponseEntity.ok("删除成功");
    }

    /**
     * 下载文件
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param response  相应结果
     */
    @GetMapping("/")
    public void downLoad(@RequestParam(required = false) String bucketName, String objectName,HttpServletResponse response) {
        bucketName = StringUtils.hasLength(bucketName) ? bucketName : minioConfig.getDefaultBucketName();
        // 获取文件
        minioUtil.downResponse(bucketName,objectName,response);
    }
}
