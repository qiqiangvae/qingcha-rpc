package com.qingcha.rpc.core.utils;

/**
 * @author qiqiang
 * @date 2020-11-05 1:35 下午
 */
public class UsefulUtils {
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