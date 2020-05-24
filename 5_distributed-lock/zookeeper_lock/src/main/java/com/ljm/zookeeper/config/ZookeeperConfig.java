package com.ljm.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lijunming
 * @date 2019/3/24 6:59 PM
 */
@Configuration
public class ZookeeperConfig {

    @Value("${zk.url}")
    private String zkUrl;
    @Value("${zk.serviceName}")
    private String serviceName;

    Log log = LogFactory.getLog(ZookeeperConfig.class);

    /**
     * 这是一个线程安全的类,可以用它完成所有的zk操作
     * RetryPolicy  用于设置重连策略,当某种原因导致zk不可用的时候进行重连尝试的策略,
     * 如下设置的是1000是初始化的间隔时间,3代表重连次数
     *
     * @return CuratorClient
     */
    @Bean
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
        client.getCuratorListenable().addListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                CuratorEventType type = curatorEvent.getType();
                if (type == CuratorEventType.WATCHED) {
                    WatchedEvent we = curatorEvent.getWatchedEvent();
                    Watcher.Event.EventType et = we.getType();
                    if (we.getType() != Watcher.Event.EventType.None) {
                        log.info(et + ":" + we.getPath());
                        //zk得到监听消息后,客户端必须再设置一次监听,才能收到后面的节点变化事件
                        client.checkExists().watched().forPath(we.getPath());
                    }
                }
            }
        });
        client.start();
        //添加到服务注册中心
        try {
            registerService(client);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("注册到服务中心失败!");
        }
        return client;
    }

    /**
     * 注册到服务中心
     *
     * @param client
     * @throws Exception
     */
    protected void registerService(CuratorFramework client) throws Exception {
        //构造一个服务描述
        log.info("registerService ....");
        ServiceInstanceBuilder<Map> service = ServiceInstance.builder();
        service.address("127.0.0.1"); //服务地址,如果没调用,curator会自动设置本机地址
        service.port(8080);           //端口号
        service.name("test");         //服务名称
        Map config = new HashMap();   //服务的配置信息
        config.put("url", "/set");
        service.payload(config);
        ServiceInstance serviceInstance = service.build();
        //basePath指定服务注册的根节点,client指定客户端,serializer设置序列化类，采用jackson作为序列化类
        ServiceDiscovery<Map> serviceDiscovery = ServiceDiscoveryBuilder.builder(Map.class).
                client(client).serializer(new JsonInstanceSerializer<Map>(Map.class)).basePath(serviceName).build();
        //服务注册
        serviceDiscovery.registerService(serviceInstance);
        serviceDiscovery.start();
    }

}