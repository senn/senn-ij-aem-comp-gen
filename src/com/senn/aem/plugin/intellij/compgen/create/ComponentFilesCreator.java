package com.senn.aem.plugin.intellij.compgen.create;

/**
 * Contract for the class that creates the AEM component files
 * @author bart.senn@gmail.com
 */
public interface ComponentFilesCreator {

    void createHtmlFiles(String componentName, String uiAppsRoot);
    void createJavaScriptFiles(String componentName, String uiAppsRoot);
    void createCSSFiles(String componentName, String uiAppsRoot);
    void createDialogXmlFiles(String componentName, String uiAppsRoot);
    void createEditConfigXmlFiles(String componentName, String uiAppsRoot);
    void createSlingModelCodeFiles(String componentName, String javaRoot, String packageName);
}
