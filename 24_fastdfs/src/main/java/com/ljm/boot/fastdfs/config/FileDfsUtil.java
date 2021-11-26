package com.ljm.boot.fastdfs.config;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.ljm.boot.fastdfs.util.FileTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author Dominick Li
 * @CreateTime 2020/3/29 17:56
 * @description 操作dfs 工具类
 **/
@Component
public class FileDfsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDfsUtil.class);

    @Resource
    private FastFileStorageClient storageClient;

    @Value("${fdfs.thumb-image.enabled}")
    private boolean thumbEnabled;

    @Value("${fdfs.thumb-image.width}")
    private String width;

    @Value("${fdfs.thumb-image.height}")
    private String height;

    /**
     * 上传文件
     */
    public String upload(MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().
                        lastIndexOf(".") + 1);
        StorePath storePath = null;
        //上传文件是图片并开启了生成缩略图配置
        if (thumbEnabled && FileTypeUtils.isImage(multipartFile.getInputStream())) {
            //如果返回的图片名称 test.jpg,则缩略图的名称是test_宽x高.jpg,例如test_250x150.jpg
            storePath = this.storageClient.uploadImageAndCrtThumbImage(multipartFile.getInputStream(), multipartFile.getSize(), originalFilename, null);
        } else {
            //上传文件
            storePath = this.storageClient.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), originalFilename, null);
        }
        return storePath.getFullPath();
    }


    /**
     * 删除文件
     */
    public void deleteFile(String fileUrl, boolean hasThumbnail) {
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            if (hasThumbnail) {
                //删除缩略图
                String[] arr = fileUrl.split("\\.");
                String thumbnailFileUrl = arr[0] +"_"+ width + "x" + height + "." + arr[1];
                storePath = StorePath.parseFromUrl(thumbnailFileUrl);
                storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    /**
     * 下载文件
     */
    public byte[] downloadFile(String fileUrl) {
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
    }
}
