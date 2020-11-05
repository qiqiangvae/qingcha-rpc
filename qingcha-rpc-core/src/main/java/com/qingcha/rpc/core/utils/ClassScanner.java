package com.qingcha.rpc.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 类扫描器
 *
 * @author qiqiang
 * @date 2020-11-03 3:28 下午
 */
public class ClassScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    private static final String FILE_PROTOCOL = "file";
    private static final String CLASS_SUFFIX = ".class";


    public static List<Class<?>> scan(String packagePath) {
        String rootPath = packagePath;
        List<Class<?>> classes = new ArrayList<>();
        if (packagePath == null) {
            return classes;
        }
        packagePath = packagePath.replace(".", File.separator);
        URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);
        if (url == null) {
            return classes;
        }
        if (url.getProtocol().equals(FILE_PROTOCOL)) {
            try {
                File file = new File(url.toURI());
                scan(rootPath, file, classes);
            } catch (URISyntaxException e) {
                logger.error(String.format("扫描文件错误，错误的URL[%s]", url), e);
            }
        }
        return classes;
    }

    public static void scan(String packagePath, File file, List<Class<?>> classes) {
        if (file == null) {
            return;
        }
        String fileName = file.getName();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File childFile : files) {
                scan(packagePath, childFile, classes);
            }
        }
        if (fileName.endsWith(CLASS_SUFFIX)) {
            String className = packagePath + "." + fileName.substring(0, fileName.lastIndexOf("."));
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                logger.error(String.format("扫描文件错误，找不到文件[%s]", className), e);
            }
        }
    }
}