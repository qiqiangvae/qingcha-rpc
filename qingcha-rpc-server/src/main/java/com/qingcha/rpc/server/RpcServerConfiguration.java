package com.qingcha.rpc.server;

/**
 * @author qiqiang
 * @date 2020-11-04 2:46 下午
 */
public class RpcServerConfiguration {
    private int port;
    private String packagePath;

    public static RpcServerConfiguration configuration() {
        return RpcServerConfigurationHolder.INSTANCE;
    }


    static class RpcServerConfigurationHolder {
        private static final RpcServerConfiguration INSTANCE = new RpcServerConfiguration();
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
}