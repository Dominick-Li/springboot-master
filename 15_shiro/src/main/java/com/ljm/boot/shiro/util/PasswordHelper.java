package com.ljm.boot.shiro.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;


public class PasswordHelper {

    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    //加密算法
    private static String algorithmName = Sha256Hash.ALGORITHM_NAME;
    //迭代次数
    private static int hashIterations = 1024;

    //生成随机的盐
    public static String randomSalt() {
        return randomNumberGenerator.nextBytes().toBase64();
    }

    public static String encryptPassword(String username, String password, String salt) {
        String encryPassword = new SimpleHash(algorithmName, password,
                ByteSource.Util.bytes(salt), hashIterations).toBase64();
        System.out.printf("username=%s ,password=%s , salt=%s , encryPassword=%s ", username, password, salt, encryPassword);
        return encryPassword;
    }

}
