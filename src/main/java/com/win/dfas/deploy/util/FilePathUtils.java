package com.win.dfas.deploy.util;

import com.alibaba.druid.sql.visitor.functions.Char;

import java.io.File;

/**
 * @包名 com.win.dfas.deploy.util
 * @类名 FileExtUtils
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/23 15:29
 */
public class FilePathUtils {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static String getHttpURLPath(String path) {
        return path.replace("\\", "/");
    }
}
