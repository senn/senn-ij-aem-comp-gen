package com.senn.aem.plugin.intellij.compgen.ui;

/**
 * Some fields that should be kept in memory during the IntelliJ session.
 * @author bart.senn@gmail.com
 */
public class IJSessionConstants {

    //textfields
    public static String JAVA_ROOT = "core/src/main/java";
    public static String UI_APPS_ROOT = "ui.apps/src/main/content/jcr_root";
    public static String PACKAGE = "";
    public static String COMPONENT_GROUP = "";

    //booleans what to create
    public static boolean SELECT_HTML = true;
    public static boolean SELECT_DIALOG_XML = true;
    public static boolean SELECT_EDIT_CONFIG_XML = true;
    public static boolean SELECT_JS = false;
    public static boolean SELECT_CSS = false;
    public static boolean SELECT_SLING_MODEL = false;

    //booleans what to do
    public static boolean OPEN_AFTER_CREATION = true;
}
