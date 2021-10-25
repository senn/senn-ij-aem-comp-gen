package com.senn.aem.plugin.intellij.compgen.create;

import com.senn.aem.plugin.intellij.compgen.ComponentCreationException;
import java.io.File;
import java.util.List;

/**
 * Contract for the class that creates the AEM component files
 * @author bart.senn@gmail.com
 */
public interface ComponentFilesCreator {

    List<File> createComponentXmlFiles() throws ComponentCreationException;
    List<File> createHtmlFiles() throws ComponentCreationException;
    List<File> createJavaScriptFiles() throws ComponentCreationException;
    List<File> createCSSFiles() throws ComponentCreationException;
    List<File> createDialogXmlFiles() throws ComponentCreationException;
    List<File> createEditConfigXmlFiles() throws ComponentCreationException;
    List<File> createSlingModelCodeFiles() throws ComponentCreationException;
}
