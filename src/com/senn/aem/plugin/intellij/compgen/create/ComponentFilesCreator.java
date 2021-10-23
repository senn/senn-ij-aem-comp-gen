package com.senn.aem.plugin.intellij.compgen.create;

import com.senn.aem.plugin.intellij.compgen.ComponentCreationException;

/**
 * Contract for the class that creates the AEM component files
 * @author bart.senn@gmail.com
 */
public interface ComponentFilesCreator {

    void createComponentXmlFiles() throws ComponentCreationException;
    void createHtmlFiles() throws ComponentCreationException;
    void createJavaScriptFiles() throws ComponentCreationException;
    void createCSSFiles() throws ComponentCreationException;
    void createDialogXmlFiles() throws ComponentCreationException;
    void createEditConfigXmlFiles() throws ComponentCreationException;
    void createSlingModelCodeFiles() throws ComponentCreationException;
}
