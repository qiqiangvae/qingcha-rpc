package com.qingcha.rpc.client;

/**
 * @author qiqiang
 * @date 2020-11-04 10:29 上午
 */
public class RpcClientConfiguration {

    private String host;
    private int port;
    /**
     * 扫描路径
     */
    private String packagePath;

    public static RpcClientConfiguration configuration() {
        return RpcClientConfigurationHolder.INSTANCE;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    static class RpcClientConfigurationHolder {
        private static final RpcClientConfiguration INSTANCE = new RpcClientConfiguration();
    }
}