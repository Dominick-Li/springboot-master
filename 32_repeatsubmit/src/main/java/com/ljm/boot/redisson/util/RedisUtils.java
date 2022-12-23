package com.ljm.boot.redisson.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.time.Duration;

/**
 * redis 工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisUtils {

    public static NameMapper getNameMapper() {
        Config config = getClient().getConfig();
        if (config.isClusterConfig()) {
            return config.useClusterServers().getNameMapper();
        }
        return config.useSingleServer().getNameMapper();
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public static <T> void setCacheObject(final String key, final T value, final Duration duration) {
        RBatch batch = getClient().createBatch();
        RBucketAsync<T> bucket = batch.getBucket(key);
        bucket.setAsync(value);
        bucket.expireAsync(duration);
        batch.execute();
    }


    /**
     * 删除单个对象
     * @param key 缓存的键值
     */
    public static boolean deleteObject(final String key) {
        return getClient().getBucket(key).delete();
    }

    /**
     * 检查redis中是否存在key
     *
     * @param key 缓存的键值
     */
    public static Boolean hasKey(String key) {
        RKeys rKeys = getClient().getKeys();
        return rKeys.countExists(getNameMapper().map(key)) > 0;
    }

    public static RedissonClient getClient() {
        return Lazy.CLIENT;
    }

    /**
     * 使用懒加载方式实例化RedissongetClient()客户端工具
     */
    private static class Lazy {
        private static final RedissonClient CLIENT = SpringUtil.getBean(RedissonClient.class);
    }
}
