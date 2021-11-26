package com.ljm.boot.fastdfs.controller;

import com.ljm.boot.fastdfs.config.FileDfsUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dominick Li
 * @CreateTime 2020/3/29 17:56
 * @description 测试上传下载和删除
 **/
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileDfsUtil fileDfsUtil;

    /**
     * 测试FastDFS文件上传
     * http://192.168.94.128:9999/group1/M00/00/00/wKg0gV6AXEqAQfSIAATgSjpZu9Q527.jpg
     * 如果正常图片是group1/M00/00/00/wKhegGGeylOAJx8yAB7ib2v_DBQ436.jpg 则缩略图为  group1/M00/00/00/wKhegGGeylOAJx8yAB7ib2v_DBQ436_250x150.jpg
     */
    @PostMapping(value = "/")
    public ResponseEntity<String> uploadFile(MultipartFile file) {
        String result;
        try {
            String path = fileDfsUtil.upload(file);
            if (!StringUtils.isEmpty(path)) {
                result = path;
            } else {
                result = "上传失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "服务异常";
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 文件删除
     * http://localhost:8024/file/fileUrl=group1/M00/00/00/wKhegGGeylOAJx8yAB7ib2v_DBQ436.jpg&hasThumbnail=true
     * @param fileUrl 上传接口返回的文件名
     * @param hasThumbnail 是否有缩略图
     */
    @DeleteMapping(value = "/")
    public ResponseEntity<String> deleteByPath(String fileUrl,@RequestParam(value = "hasThumbnail",required = false,defaultValue = "false") boolean hasThumbnail) {
        fileDfsUtil.deleteFile(fileUrl,hasThumbnail);
        return ResponseEntity.ok("SUCCESS");
    }

    /**
     * 文件下载
     * http://localhost:8024/file/?fileUrl=group1/M00/00/00/wKhegGGeylOAJx8yAB7ib2v_DBQ436.jpg
     * @param fileUrl 上传接口返回的文件名
     */
    @GetMapping("/")
    public void downLoad(@RequestParam String fileUrl, HttpServletResponse response) throws IOException {
        // 获取文件
        byte[] bytes = fileDfsUtil.downloadFile(fileUrl);
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        response.reset();
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        response.getOutputStream().write(bytes);
        response.getOutputStream().close();

    }


}
