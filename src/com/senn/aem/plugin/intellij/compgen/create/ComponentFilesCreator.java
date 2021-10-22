package com.senn.aem.plugin.intellij.compgen.create;

/**
 * Contract for the class that creates the AEM component files
 * @author bart.senn@gmail.com
 */
public interface ComponentFilesCreator {

    void createHtmlFiles();
    void createJavaScriptFiles();
    void createCSSFiles();
    void createDialogXmlFiles();
    void createEditConfigXmlFiles();
    void createSlingModelCodeFiles();
}
