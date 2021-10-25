package com.senn.aem.plugin.intellij.compgen.utils;

import java.io.InputStream;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class for path related stuff
 * @author bart.senn@gmail.com
 */
public final class PathUtils {

    private static final String TEMPLATE_FOLDER_PATH = "fileTemplates/internal/";

    private PathUtils() {}

    private static final String FOLDER_LEVEL_SEPARATOR = "/";

    public static String validatePath(String path, boolean inclPrefix, boolean inclSuffix) {
        if(path == null) return "";
        if(StringUtils.isBlank(path)) return "";

        if(inclPrefix && !path.startsWith(FOLDER_LEVEL_SEPARATOR)) {
            path = FOLDER_LEVEL_SEPARATOR + path;
        }

        if(inclSuffix && ! path.endsWith(FOLDER_LEVEL_SEPARATOR)) {
            path += FOLDER_LEVEL_SEPARATOR;
        }

        return path;
    }

    public static InputStream getResourceAsStream(String fileName) {
        return PathUtils.class.getClassLoader().getResourceAsStream(fileName);
    }

    public static String getTemplatePath(String templateName) {
        return TEMPLATE_FOLDER_PATH + templateName;
    }

    public static String getJavaPackageAsFolderPath(String packageName) {
        return validatePath(packageName.replace(".", "/"), false, true);
    }

}
