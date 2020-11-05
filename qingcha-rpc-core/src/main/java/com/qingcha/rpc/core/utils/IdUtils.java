package com.qingcha.rpc.core.utils;

import java.util.UUID;

/**
 * id 生产工具
 *
 * @author qiqiang
 * @date 2020-11-04 11:45 上午
 */
public class IdUtils {
    /**
     * uuid
     *
     * @return uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}