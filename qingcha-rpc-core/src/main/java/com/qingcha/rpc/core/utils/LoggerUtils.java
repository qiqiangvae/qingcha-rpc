package com.qingcha.rpc.core.utils;

import org.slf4j.Logger;

/**
 * 日志工具
 *
 * @author qiqiang
 * @date 2020-11-04 12:42 下午
 */
public class LoggerUtils {
    public static void debug(Logger logger, LogAction logAction) {
        if (logger.isDebugEnabled()) {
            logAction.action();
        }
    }

    public static void info(Logger logger, LogAction logAction) {
        if (logger.isInfoEnabled()) {
            logAction.action();
        }
    }

    public static void warn(Logger logger, LogAction logAction) {
        if (logger.isWarnEnabled()) {
            logAction.action();
        }
    }

    public static void error(Logger logger, LogAction logAction) {
        if (logger.isErrorEnabled()) {
            logAction.action();
        }
    }

    public interface LogAction {
        void action();
    }
}