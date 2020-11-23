package com.qingcha.rpc.server;


import io.netty.util.concurrent.EventExecutorGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author qiqiang
 * @date 2020-11-03 10:49 上午
 */
public class RpcServerExecutor {
    private final ExecutorService serverExecutor;
    private List<EventExecutorGroup> nettyExecutorGroups;

    private RpcServerExecutor() {
        int cpus = Runtime.getRuntime().availableProcessors();
        serverExecutor = new ThreadPoolExecutor(cpus, cpus, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
            private int count;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "rpc-server-thread" + count++);
            }
        });
        // 添加 JVM 钩子函数
        Runtime.getRuntime().addShutdownHook(new ServerShutdownHookThread(this));
    }

    public static RpcServerExecutor getInstance() {
        return RpcServerExecutorHolder.INSTANCE;
    }

    static class RpcServerExecutorHolder {
        private static final RpcServerExecutor INSTANCE = new RpcServerExecutor();
    }

    public void close() {
        if (serverExecutor != null) {
            serverExecutor.shutdown();
        }
        if (nettyExecutorGroups != null && nettyExecutorGroups.size() > 0) {
            nettyExecutorGroups.forEach(EventExecutorGroup::shutdownGracefully);
        }
    }

    public void execute(Runnable r) {
        serverExecutor.execute(r);
    }

    public void addNettyExecutorGroup(EventExecutorGroup group) {
        if (nettyExecutorGroups == null) {
            nettyExecutorGroups = new ArrayList<>(2);
        }
        nettyExecutorGroups.add(group);
    }
}