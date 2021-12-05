package com.ljm.boot.minio.util;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    /**
     * 获取当前日期字符串格式
     * @return 2021/12/5
     */
    public String getDatePath() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("/%s/%s/%s/", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
    }


    /**
     * 判断桶是否存
     * @param bucketName 桶名称
     * @return
     */
    public boolean bucketExists(String bucketName) throws Exception {
        boolean flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (flag) {
            return true;
        }
        return false;
    }


    /**
     * 创建桶
     */
    public boolean createBucket(String bucketName) {
        try {
            //判断文件存储的桶对象是否存在
            boolean isExist = bucketExists(bucketName);
            if (isExist) {
                log.info("Bucket asiatrip already exists.");
                return false;
            } else {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                return true;
            }
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
    }

    /**
     * 列出桶里的所有对象
     * @param bucketName 桶名称
     */
    public Iterable<Result<Item>> listObjects(String bucketName) {
        return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 删除桶
     * @param bucketName 桶名称
     * @return 是否删除成功
     */
    public boolean removeBucket(String bucketName) {
        try {
            boolean flag = bucketExists(bucketName);
            if (flag) {
                Iterable<Result<Item>> myObjects = listObjects(bucketName);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    // 有对象文件，则删除失败
                    if (item.size() > 0) {
                        return false;
                    }
                }
                // 删除存储桶，注意，只有存储桶为空时才能删除成功。
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
                flag = bucketExists(bucketName);
                if (!flag) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
        return false;
    }

    /**
     * 获取所有桶信息
     */
    public List<Bucket> getAllBucket() {
        try {
            // 获取minio中所以的bucket
            List<Bucket> buckets = minioClient.listBuckets();
            for (Bucket bucket : buckets) {
                log.info("bucket 名称:  {}      bucket 创建时间: {}", bucket.name(), bucket.creationDate());
            }
            return buckets;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return Collections.emptyList();
        }
    }


    /**
     * 上传本地文件到指定桶下
     * @param bucketName    桶名称
     * @param objectName    对象名称
     * @param localFileName 要上传的文件路径
     * @return
     */
    public boolean upload(String bucketName, String objectName, String localFileName) {
        try {
            File file = new File(localFileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(fileInputStream, file.length(), PutObjectArgs.MIN_MULTIPART_SIZE)
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
    }


    /**
     * 上传MultipartFile到指定桶下
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param file       文件流
     */
    public boolean upload(String bucketName, String objectName, MultipartFile file) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .stream(file.getInputStream(), file.getSize(), PutObjectArgs.MIN_MULTIPART_SIZE)
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
    }

    /**
     * 下载文件到本地
     * @param bucketName    桶名称
     * @param objectName    对象名称
     * @param localFileName 本地文件存储路径
     */
    public boolean downLocal(String bucketName, String objectName, String localFileName) {
        try {
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(localFileName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
    }


    /**
     * 下载文件写入到HttpServletResponse
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param response   HttpServletResponse对象
     */
    @SneakyThrows
    public void downResponse(String bucketName, String objectName, HttpServletResponse response) {
        GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
        response.setHeader("Content-Disposition", "attachment;filename=" + objectName.substring(objectName.lastIndexOf("/") + 1));
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        IOUtils.copy(object, response.getOutputStream());
    }


    /**
     * 删除指定桶的指定文件对象
     * @param bucketName 桶名称
     * @param objectName 对象名称
     */
    public boolean delete(String bucketName, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }catch (Exception e){
            log.error("errorMsg={}",e);
            return false;
        }
        //TODO 递归删除目录暂时未实现
//        try {
//            if (StringUtils.hasLength(remoteFileName)) {
//                if (remoteFileName.endsWith("/")) {
//                    Iterable<Result<Item>> list = getInstance().listObjects(ListObjectsArgs.builder().bucket(MinioConfig.getBucketName());
//                    list.forEach(e -> {
//                        try {
//                            getInstance().removeObject(RemoveObjectArgs.builder().bucket(MinioConfig.getBucketName()).object(e.get().objectName()).build());;
//                        }catch (Exception el){
//                            el.printStackTrace();
//                        }
//                    });
//                }else{
//                    getInstance().removeObject(RemoveObjectArgs.builder().bucket(MinioConfig.getBucketName()).object(remoteFileName).build());;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     * @param bucketName  存储桶名称
     * @param objectNames 含有要删除的多个object名称的迭代器对象
     * @return
     */
    public boolean deletes(String bucketName, List<String> objectNames) {
        try {
            List<String> deleteErrorNames = new ArrayList<>();

            List<DeleteObject> list = new LinkedList<>();
            objectNames.forEach(item -> list.add(new DeleteObject(item)));
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(list).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrorNames.add(error.objectName());
            }
            return deleteErrorNames.size() == 0 ? true : false;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
    }

    /**
     * 获取文件带时效的访问链接   失效时间（以秒为单位），默认是7天不得大于七天
     * @param bucketName     桶名称
     * @param remoteFileName 对象名称
     * @param timeout        时间
     * @param unit           单位
     * @return 文件访问链接
     */
    public String getPresignedObjectUrl(String bucketName, String remoteFileName, long timeout, TimeUnit unit) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(remoteFileName)
                            .expiry((int) unit.toSeconds(timeout))
                            .build());
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return null;
        }
    }

}
