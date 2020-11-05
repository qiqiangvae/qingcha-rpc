package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.client.RpcClientResponseHandler;
import com.qingcha.rpc.core.common.RpcResponseBody;
import com.qingcha.rpc.core.utils.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author qiqiang
 * @date 2020-11-04 11:35 上午
 */
public class ProxyResponseProcessor {
    private final Logger logger = LoggerFactory.getLogger(RpcClientResponseHandler.class);
    private final RpcClientHolder holder;

    public ProxyResponseProcessor(RpcClientHolder holder) {
        this.holder = holder;
    }

    public void handleResponse(String key, RpcResponseBody rpcResponseBody) {
        boolean success = rpcResponseBody.isSuccess();
        String message = rpcResponseBody.getMessage();
        Object bodyBody = rpcResponseBody.getBody();
        LoggerUtils.debug(logger, () -> logger.debug("success[{}],message[{}]", success, message));
        ArrayBlockingQueue<Object> queue = holder.getQueue(key);
        try {
            // 写入返回值
            queue.put(bodyBody);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}