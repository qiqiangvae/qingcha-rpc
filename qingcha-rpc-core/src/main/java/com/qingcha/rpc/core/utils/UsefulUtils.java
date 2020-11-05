package com.qingcha.rpc.core.utils;

/**
 * 实用工具
 *
 * @author qiqiang
 * @date 2020-11-05 1:35 下午
 */
public class UsefulUtils {
    /**
     * 判断是否为空字符串
     *
     * @param text 字符串
     * @return 是否为空
     */
    public static boolean isBlack(String text) {
        return text == null || "".equals(text) || isSpace(text);
    }

    private static boolean isSpace(String text) {
        for (char c : text.toCharArray()) {
            if (c != '\0') {
                return false;
            }
        }
        return true;
    }
}