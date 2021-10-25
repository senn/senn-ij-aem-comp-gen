package com.senn.aem.plugin.intellij.compgen.create.impl;

import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.ComponentCreationException;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.utils.ComponentConfig;
import com.senn.aem.plugin.intellij.compgen.utils.PathUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ideally we want to work with actual templates in IntelliJ but for now an <i>on-the-fly</i> file generation approach will do.
 * @author bart.senn@gmail.com
 */
public class FileWritingComponentFilesCreator implements ComponentFilesCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWritingComponentFilesCreator.class);

    private interface Clientlib {
        String CSS = "css";
        String JS = "js";
    }

    private final Project project;
    private final ComponentConfig componentConfig;

    public FileWritingComponentFilesCreator(final Project project, final ComponentConfig componentOptions) {
        this.componentConfig = componentOptions;
        this.project = project;
    }

    @Override
    public void createComponentXmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createComponentXmlFiles for " + componentConfig.getFullComponentName());

        try {
            File xmlFile = new File(PathUtils.validatePath(getFullComponentPath(), false, true) + ".content.xml");
            if (!xmlFile.exists()) {
                try (InputStream xmlStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("component.content.xml"))) {
                    if (xmlStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the component content.xml template");
                    }

                    final File componentFileDir = new File(getFullComponentPath());
                    componentFileDir.mkdirs();
                    if (!componentFileDir.exists() || !componentFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the component folder path: " + getFullComponentPath());
                    }

                    try (InputStreamReader xmlStreamReader = new InputStreamReader(xmlStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedReader = new BufferedReader(xmlStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                line = line.replace("{%COMP_TITLE%}", componentConfig.getComponentTitle())
                                        .replace("{%COMP_GROUP%}", componentConfig.getComponentGroup());
                                contentLines.add(line);
                            }

                            writeLinesToFile(xmlFile, contentLines);
                        }

                    }
                }
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating component XML file", ioe);
        }

        LOGGER.info("Finished createComponentXmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createHtmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createHtmlFiles for " + componentConfig.getFullComponentName());

        final String fullComponentPath = getFullComponentPath();
        final File htmlFile = new File(fullComponentPath + componentConfig.getShortComponentName() + ".html");
        if(htmlFile.exists()) {
            throw new ComponentCreationException("Component HTML file already exists!");
        }

        try(InputStream htmlStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("component.html"))) {
            if (htmlStream == null) {
                throw new ComponentCreationException("An error occurred while reading the HTML template");
            }

            try(InputStreamReader htmlStreamReader = new InputStreamReader(htmlStream, StandardCharsets.UTF_8)) {
                try(BufferedReader htmlReader = new BufferedReader(htmlStreamReader)) {
                    List<String> htmlContentLines = new ArrayList<>();
                    String line;
                    while((line = htmlReader.readLine()) != null) {
                       line = line.replace("{%COMP_NAME%}", componentConfig.getShortComponentName());

                       if(componentConfig.makeCSS()) {
                           line = line.replace("{%USE_CLIENTLIB_CSS%}", "<sly data-sly-use.clientlib=\"/libs/granite/sightly/templates/clientlib.html\" data-sly-call=\"${clientlib.css @ categories='" + componentConfig.getClientlibCategory() + "'}\"></sly>");
                       } else {
                           line = line.replace("{%USE_CLIENTLIB_CSS%}", StringUtils.EMPTY);
                       }

                       if(componentConfig.makeJS()) {
                           line = line.replace("{%USE_CLIENTLIB_JS%}", "<sly data-sly-use.clientlib=\"/libs/granite/sightly/templates/clientlib.html\" data-sly-call=\"${clientlib.js @ categories='" + componentConfig.getClientlibCategory() + "'}\"></sly>");
                       } else {
                           line = line.replace("{%USE_CLIENTLIB_JS%}", StringUtils.EMPTY);
                       }

                       if(componentConfig.makeSlingModelCode()) {
                            line = line.replace("{%USE_SLING_MODEL%}", " data-sly-use." + componentConfig.getShortComponentName() + "=\"" + componentConfig.getFullyQualifiedSlingModelName() + "\"");
                       } else {
                            line = line.replace("{%USE_SLING_MODEL%}", StringUtils.EMPTY);
                       }

                       //only add lines to html if they contain something
                       if(StringUtils.isNotBlank(line)) {
                           htmlContentLines.add(line);
                       }
                    }

                    final File componentFileDir = new File(fullComponentPath);
                    componentFileDir.mkdirs();
                    if (!componentFileDir.exists() || !componentFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the component folder path: " + fullComponentPath);
                    }

                    writeLinesToFile(htmlFile, htmlContentLines);
                }
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating HTML component files", ioe);
        }
        LOGGER.info("Finished createHtmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createJavaScriptFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createJavaScriptFiles for " + componentConfig.getFullComponentName());
        createClientlib(Clientlib.JS);
        LOGGER.info("Finished createJavaScriptFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createCSSFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createCSSFiles for " + componentConfig.getFullComponentName());
        createClientlib(Clientlib.CSS);
        LOGGER.info("Finished createCSSFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createDialogXmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createDialogXmlFiles for " + componentConfig.getFullComponentName());

        try {
            File dialogXmlFile = new File(PathUtils.validatePath(getFullComponentPath(), false, true) + "_cq_dialog/.content.xml");
            if (!dialogXmlFile.exists()) {
                try (InputStream dialogXmlStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("dialogconfig.content.xml"))) {
                    if (dialogXmlStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the dialog content.xml template");
                    }

                    final File dialogFileDir = new File(PathUtils.validatePath(getFullComponentPath(), false, true) + "_cq_dialog");
                    dialogFileDir.mkdirs();
                    if (!dialogFileDir.exists() || !dialogFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the dialog folder path: " + getFullComponentPath());
                    }

                    try (InputStreamReader dialogXmlStreamReader = new InputStreamReader(dialogXmlStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedDialogReader = new BufferedReader(dialogXmlStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedDialogReader.readLine()) != null) {
                                line = line.replace("{%COMP_TITLE%}", componentConfig.getComponentTitle());
                                contentLines.add(line);
                            }

                            writeLinesToFile(dialogXmlFile, contentLines);
                        }

                    }
                }
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating component XML file", ioe);
        }

        LOGGER.info("Finished createDialogXmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createEditConfigXmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createEditConfigXmlFiles for " + componentConfig.getFullComponentName());

        try {
            File editCfgXmlFile = new File(PathUtils.validatePath(getFullComponentPath(), false, true) + "_cq_editConfig.xml");
            if (!editCfgXmlFile.exists()) {
                try (InputStream editCfgXmlStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("editconfig.xml"))) {
                    if (editCfgXmlStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the editconfig.xml template");
                    }

                    final File editCfgFileDir = new File(PathUtils.validatePath(getFullComponentPath(), false, true));
                    editCfgFileDir.mkdirs();
                    if (!editCfgFileDir.exists() || !editCfgFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the dialog folder path: " + getFullComponentPath());
                    }

                    try (InputStreamReader editCfgXmlStreamReader = new InputStreamReader(editCfgXmlStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedEditCfgReader = new BufferedReader(editCfgXmlStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedEditCfgReader.readLine()) != null) {
                                contentLines.add(line);
                            }

                            writeLinesToFile(editCfgXmlFile, contentLines);
                        }

                    }
                }
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating component XML file", ioe);
        }

        LOGGER.info("Finished createEditConfigXmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createSlingModelCodeFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createSlingModelCodeFiles for " + componentConfig.getFullComponentName());
        //TODO: impl
        LOGGER.info("Finished createSlingModelCodeFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private String getFullComponentPath() {
        return project.getBasePath() +
                PathUtils.validatePath(componentConfig.getUiAppsRoot(), true, true) +
                PathUtils.validatePath(componentConfig.getFullComponentName(), false, true);
    }

    private void createClientlib(String clientlibType) throws ComponentCreationException {
        clientlibType = clientlibType.toLowerCase();

        final String fullComponentPath = getFullComponentPath();
        //make directories
        final String clientlibPath = getFullComponentPath() + "clientlib";
        final String clientlibTypePath = PathUtils.validatePath(clientlibPath, false, true) + clientlibType;
        File clientlibDir = new File(clientlibTypePath);
        clientlibDir.mkdirs();

        //make actual files
        try {
            //clientlib overview file (js.txt / css.txt)
            final File overviewFile = new File(PathUtils.validatePath(clientlibPath, false, true) + clientlibType + ".txt");
            try (BufferedWriter overviewFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(overviewFile)))) {
                overviewFileWriter.write("#base=" + clientlibType);
                overviewFileWriter.newLine();
                overviewFileWriter.newLine();
                overviewFileWriter.write( componentConfig.getShortComponentName() + "." + clientlibType);
            }

            //clientlib folder .content.xml file
            File contentFile = new File(PathUtils.validatePath(clientlibPath, false, true) + ".content.xml");
            if(!contentFile.exists()) {
                try (InputStream clientlibContentStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("clientlib.content.xml"))) {
                    if (clientlibContentStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the clientlib content.xml template");
                    }

                    try (InputStreamReader clientlibContentStreamReader = new InputStreamReader(clientlibContentStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader clientlibContentReader = new BufferedReader(clientlibContentStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = clientlibContentReader.readLine()) != null) {
                                line = line.replace("{%CLIENTLIB_CATEGORY%}", componentConfig.getClientlibCategory());
                                contentLines.add(line);
                            }

                            writeLinesToFile(contentFile, contentLines);
                        }

                    }
                }
            }

            //empty clientlib impl file
            File clientlibFile = new File(PathUtils.validatePath(clientlibTypePath, false, true) + componentConfig.getShortComponentName() + "." + clientlibType);
            if(!clientlibFile.exists()) {
                if(!clientlibFile.createNewFile()) {
                    throw new ComponentCreationException("An occurred while creating the clientlib implementation file: " + clientlibFile.getName());
                }
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating " + clientlibType + " clientlib files", ioe);
        }
    }

    private void writeLinesToFile(final File target, List<String> linesToWrite) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target)))) {
            linesToWrite.forEach(contentLine -> {
                try {
                    writer.write(contentLine);
                    writer.newLine();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            });
        }
    }

}
