package com.senn.aem.plugin.intellij.compgen;

/**
 * General plugin wide constants
 * @author bart.senn@gmail.com
 */
public interface PluginConstants {

    String PLUGIN_ID = "com.senn.aem.plugin.intellij.compgen";

    //persistent keys
    String KEY_JAVA_ROOT = buildKey("javaRoot");
    String KEY_UI_APPS_ROOT = buildKey("uiAppsRoot");
    String KEY_PACKAGE = buildKey("package");
    String KEY_COMPONENT_GROUP = buildKey("componentGroup");
    String KEY_SELECT_HTML = buildKey("selectHtml");
    String KEY_SELECT_DIALOG_XML = buildKey("selectDialogXml");
    String KEY_SELECT_EDIT_CONFIG_XML = buildKey("selectEditConfigXml");
    String KEY_SELECT_JS = buildKey("selectJS");
    String KEY_SELECT_CSS = buildKey("selectCSS");
    String KEY_SELECT_SLING_MODEL = buildKey("selectSlingModel");
    String KEY_SELECT_OPEN_AFTER_CREATION = buildKey("selectOpenAfterCreation");

    private static String buildKey(final String setting) {
        return PLUGIN_ID + "." + setting;
    }
}
