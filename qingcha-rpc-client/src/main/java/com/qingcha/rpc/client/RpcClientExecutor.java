package com.qingcha.rpc.client;


import io.netty.util.concurrent.EventExecutorGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author qiqiang
 * @date 2020-11-03 10:49 上午
 */
public class RpcClientExecutor {
    private ExecutorService serverExecutor;
    private List<EventExecutorGroup> nettyExecutorGroups;

    private RpcClientExecutor() {
        int cpus = Runtime.getRuntime().availableProcessors();
        serverExecutor = new ThreadPoolExecutor(cpus, cpus, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
            private int count;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "rpc-client-thread" + count++);
            }
        });
        // 添加 JVM 钩子函数
        Runtime.getRuntime().addShutdownHook(new ClientShutdownHookThread(this));
    }

    public static RpcClientExecutor getInstance() {
        return RpcClientExecutorHolder.INSTANCE;
    }

    static class RpcClientExecutorHolder {
        private static final RpcClientExecutor INSTANCE = new RpcClientExecutor();
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

    public void submit(Callable<?> c) {
        serverExecutor.submit(c);
    }

    public void addNettyExecutorGroup(EventExecutorGroup group) {
        if (nettyExecutorGroups == null) {
            nettyExecutorGroups = new ArrayList<>(2);
        }
        nettyExecutorGroups.add(group);
    }
}