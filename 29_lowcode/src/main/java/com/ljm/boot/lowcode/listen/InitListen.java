package com.ljm.boot.lowcode.listen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author Dominick Li
 * @description 容器加载后的初始化操作
 **/
@Component
@Slf4j
public class InitListen implements CommandLineRunner {

    private static String serverIp;

    public static String getServerIp() {
        return serverIp;
    }

    @Override
    public void run(String... args) {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String serverIP = addr.getHostAddress(); //获取本机ip
            String hostName = addr.getHostName(); //获取本机计算机名称
            log.info("serverIp={} ,hostName={}", serverIP, hostName);
            InitListen.serverIp = serverIP;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
