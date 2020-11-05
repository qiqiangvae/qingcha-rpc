package com.qingcha.rpc.springboot.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiqiang
 * @date 2020-11-04 5:04 下午
 */
@ConfigurationProperties(prefix = "qingcha.rpc.client")
public class RpcClientProperties {
    private String packagePath;

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }
}