package com.ljm.boot.minio;

import com.ljm.boot.minio.config.MinioConfig;
import com.ljm.boot.minio.util.MinioUtil;
import io.minio.messages.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MinioApplicationTests {

    @Autowired
    MinioConfig minioConfig;

    @Autowired
    MinioUtil minioUtil;

    @Test
    void contextLoads() throws Exception {
        //创建桶
        minioUtil.createBucket(minioConfig.getDefaultBucketName());
        //删除桶
        boolean flag = minioUtil.removeBucket(minioConfig.getDefaultBucketName());
        //获取所有桶信息
        List<Bucket> list = minioUtil.getAllBucket();
        String localFileName = "C:\\Users\\Administrator\\Desktop\\booklist.txt";
        String objectName = minioUtil.getDatePath() + "booklist.txt";
//        //上传到minio的存储路径
        flag = minioUtil.upload(minioConfig.getDefaultBucketName(), objectName, localFileName);
        //获取文件的带时效的外链
        System.out.println(minioUtil.getPresignedObjectUrl(minioConfig.getDefaultBucketName(), objectName, 10, TimeUnit.SECONDS));
        //重minio下载文件到本地
        String localPath = "C:\\Users\\Administrator\\Desktop\\test.txt";
        minioUtil.downLocal(minioConfig.getDefaultBucketName(), objectName, localPath);
        //删除minio文件
        minioUtil.delete(minioConfig.getDefaultBucketName(), objectName);
        //删除多个minio文件
        flag = minioUtil.deletes(minioConfig.getDefaultBucketName(), Arrays.asList(objectName));
    }

}
