package com.senn.aem.plugin.intellij.compgen.create.impl;

import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.ComponentCreationException;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.ComponentConfig;
import com.senn.aem.plugin.intellij.compgen.utils.FocusPriorityFile;
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
    public List<File> createComponentXmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createComponentXmlFiles for " + componentConfig.getFullComponentName());

        final List<File> newlyCreatedFiles = new ArrayList<>();
        try {
            File xmlFile = new FocusPriorityFile(PathUtils.validatePath(getFullComponentPath(), false, true) + ".content.xml", 1);
            if (!xmlFile.exists()) {
                LOGGER.debug("Component XML file does not exist yet... Continuing with creation");
                try (InputStream xmlStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("component.content.xml"))) {
                    if (xmlStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the component content.xml template");
                    }

                    final File componentFileDir = new File(getFullComponentPath());
                    LOGGER.debug("Making directory path: " + componentFileDir.getAbsolutePath());
                    componentFileDir.mkdirs();
                    if (!componentFileDir.exists() || !componentFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the component folder path: " + getFullComponentPath());
                    }

                    LOGGER.debug("Reading template and replacing placeholders with values...");
                    try (InputStreamReader xmlStreamReader = new InputStreamReader(xmlStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedReader = new BufferedReader(xmlStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                line = line.replace("{%COMP_TITLE%}", componentConfig.getComponentTitle())
                                        .replace("{%COMP_GROUP%}", componentConfig.getComponentGroup());
                                contentLines.add(line);
                            }
                            LOGGER.debug("Finished read and replace");
                            writeLinesToFile(xmlFile, contentLines);
                        }

                    }
                    newlyCreatedFiles.add(xmlFile);
                }
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating component XML file", ioe);
        }

        LOGGER.info("Finished createComponentXmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
        return newlyCreatedFiles;
    }

    @Override
    public List<File> createHtmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createHtmlFiles for " + componentConfig.getFullComponentName());

        final List<File> newlyCreatedFiles = new ArrayList<>();

        final String fullComponentPath = getFullComponentPath();
        final File htmlFile = new FocusPriorityFile(fullComponentPath + componentConfig.getShortComponentName() + ".html", 2);
        if(htmlFile.exists()) {
            throw new ComponentCreationException("Component HTML file already exists!");
        }

        try(InputStream htmlStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("component.html"))) {
            if (htmlStream == null) {
                throw new ComponentCreationException("An error occurred while reading the HTML template");
            }

            LOGGER.debug("Reading template and replacing placeholders with values...");
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
                    LOGGER.debug("Finished read and replace");

                    final File componentFileDir = new File(fullComponentPath);
                    LOGGER.debug("Making directory path: " + componentFileDir.getAbsolutePath());
                    componentFileDir.mkdirs();
                    if (!componentFileDir.exists() || !componentFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the component folder path: " + fullComponentPath);
                    }

                    writeLinesToFile(htmlFile, htmlContentLines);
                }
            }
            newlyCreatedFiles.add(htmlFile);
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating HTML component files", ioe);
        }
        LOGGER.info("Finished createHtmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
        return newlyCreatedFiles;
    }

    @Override
    public List<File> createJavaScriptFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createJavaScriptFiles for " + componentConfig.getFullComponentName());
        final List<File> newlyCreatedFiles = createClientlib(Clientlib.JS);
        LOGGER.info("Finished createJavaScriptFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
        return newlyCreatedFiles;
    }

    @Override
    public List<File> createCSSFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createCSSFiles for " + componentConfig.getFullComponentName());
        final List<File> newlyCreatedFiles = createClientlib(Clientlib.CSS);
        LOGGER.info("Finished createCSSFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
        return newlyCreatedFiles;
    }

    @Override
    public List<File> createDialogXmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createDialogXmlFiles for " + componentConfig.getFullComponentName());

        final List<File> newlyCreatedFiles = new ArrayList<>();

        try {
            File dialogXmlFile = new FocusPriorityFile(PathUtils.validatePath(getFullComponentPath(), false, true) + "_cq_dialog/.content.xml", 3);
            if (!dialogXmlFile.exists()) {
                LOGGER.debug("Dialog XML file does not exist yet... Continuing with creation");
                try (InputStream dialogXmlStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("dialogconfig.content.xml"))) {
                    if (dialogXmlStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the dialog content.xml template");
                    }

                    final File dialogFileDir = new File(PathUtils.validatePath(getFullComponentPath(), false, true) + "_cq_dialog");
                    LOGGER.debug("Making directory path: " + dialogFileDir.getAbsolutePath());
                    dialogFileDir.mkdirs();
                    if (!dialogFileDir.exists() || !dialogFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the dialog folder path: " + getFullComponentPath());
                    }

                    LOGGER.debug("Reading template and replacing placeholders with values...");
                    try (InputStreamReader dialogXmlStreamReader = new InputStreamReader(dialogXmlStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedDialogReader = new BufferedReader(dialogXmlStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedDialogReader.readLine()) != null) {
                                line = line.replace("{%COMP_TITLE%}", componentConfig.getComponentTitle());
                                contentLines.add(line);
                            }
                            LOGGER.debug("Finished read and replace");

                            writeLinesToFile(dialogXmlFile, contentLines);
                        }
                    }
                }
                newlyCreatedFiles.add(dialogXmlFile);
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating dialog XML file", ioe);
        }

        LOGGER.info("Finished createDialogXmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
        return newlyCreatedFiles;
    }

    @Override
    public List<File> createEditConfigXmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createEditConfigXmlFiles for " + componentConfig.getFullComponentName());

        final List<File> newlyCreatedFiles = new ArrayList<>();

        try {
            File editCfgXmlFile = new File(PathUtils.validatePath(getFullComponentPath(), false, true) + "_cq_editConfig.xml");
            if (!editCfgXmlFile.exists()) {
                LOGGER.debug("Edit config XML file does not exist yet... Continuing with creation");
                try (InputStream editCfgXmlStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("editconfig.xml"))) {
                    if (editCfgXmlStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the editconfig.xml template");
                    }

                    final File editCfgFileDir = new File(PathUtils.validatePath(getFullComponentPath(), false, true));
                    LOGGER.debug("Making directory path: " + editCfgFileDir.getAbsolutePath());
                    editCfgFileDir.mkdirs();
                    if (!editCfgFileDir.exists() || !editCfgFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the dialog folder path: " + getFullComponentPath());
                    }

                    LOGGER.debug("Reading template and replacing placeholders with values...");
                    try (InputStreamReader editCfgXmlStreamReader = new InputStreamReader(editCfgXmlStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedEditCfgReader = new BufferedReader(editCfgXmlStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedEditCfgReader.readLine()) != null) {
                                contentLines.add(line);
                            }
                            LOGGER.debug("Finished read and replace");

                            writeLinesToFile(editCfgXmlFile, contentLines);
                        }
                    }
                }
                newlyCreatedFiles.add(editCfgXmlFile);
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating edit config XML file", ioe);
        }

        LOGGER.info("Finished createEditConfigXmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
        return newlyCreatedFiles;
    }

    @Override
    public List<File> createSlingModelCodeFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createSlingModelCodeFiles for " + componentConfig.getFullComponentName());

        final List<File> newlyCreatedFiles = new ArrayList<>();

        try {
            //make package folders
            final String packageFolderPath = PathUtils.validatePath(project.getBasePath(), false, true)
                    + PathUtils.validatePath(componentConfig.getJavaCodeRoot(), false, true)
                    + PathUtils.getJavaPackageAsFolderPath(componentConfig.getPackageName());
            final File packageFolders = new File(packageFolderPath);
            final File packageImplFolder = new File(packageFolderPath + "impl");
            LOGGER.debug("Making directory path: " + packageFolders.getAbsolutePath());
            packageFolders.mkdirs();
            LOGGER.debug("Making directory path: " + packageImplFolder.getAbsolutePath());
            packageImplFolder.mkdirs();

            //make package-info
            final File packageInfoFile = new File(packageFolderPath + "package-info.java");
            if (!packageInfoFile.exists()) {
                LOGGER.debug("Package info file does not exist yet... Continuing with creation");
                try (InputStream packageInfoStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("package-info.java"))) {
                    if (packageInfoStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the package-info.java template");
                    }

                    LOGGER.debug("Reading template and replacing placeholders with values...");
                    try (InputStreamReader packageInfoStreamReader = new InputStreamReader(packageInfoStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedPackageInfoStreamReader = new BufferedReader(packageInfoStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedPackageInfoStreamReader.readLine()) != null) {
                                line = line.replace("{%PACKAGE_NAME%}", componentConfig.getPackageName());
                                contentLines.add(line);
                            }
                            LOGGER.debug("Finished read and replace");

                            writeLinesToFile(packageInfoFile, contentLines);
                        }
                    }
                }
                newlyCreatedFiles.add(packageInfoFile);
            }

            //make inter
            final File interFile = new FocusPriorityFile(packageFolderPath + componentConfig.getSlingModelName() + ".java", 5);
            if (!interFile.exists()) {
                LOGGER.debug("Interface class file does not exist yet... Continuing with creation");
                try (InputStream interStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("inter.java"))) {
                    if (interStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the Sling model interface template");
                    }

                    LOGGER.debug("Reading template and replacing placeholders with values...");
                    try (InputStreamReader interStreamReader = new InputStreamReader(interStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedInterStreamReader = new BufferedReader(interStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedInterStreamReader.readLine()) != null) {
                                line = line.replace("{%PACKAGE_NAME%}", componentConfig.getPackageName())
                                        .replace("{%COMP_NAME_SHORT%}", componentConfig.getSlingModelName())
                                        .replace("{%COMP_NAME_FULL%}", componentConfig.getFullComponentName());
                                contentLines.add(line);
                            }
                            LOGGER.debug("Finished read and replace");

                            writeLinesToFile(interFile, contentLines);
                        }
                    }
                }
                newlyCreatedFiles.add(interFile);
            }

            //make impl
            final File implFile = new FocusPriorityFile(packageFolderPath + "impl/" + componentConfig.getSlingModelName() + "Impl.java", 4);
            if (!implFile.exists()) {
                LOGGER.debug("Impl class file does not exist yet... Continuing with creation");
                try (InputStream implStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("impl.java"))) {
                    if (implStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the Sling model implementation template");
                    }

                    LOGGER.debug("Reading template and replacing placeholders with values...");
                    try (InputStreamReader implStreamReader = new InputStreamReader(implStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader bufferedImplStreamReader = new BufferedReader(implStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = bufferedImplStreamReader.readLine()) != null) {
                                line = line.replace("{%PACKAGE_NAME%}", componentConfig.getPackageName())
                                        .replace("{%COMP_NAME_SHORT%}", componentConfig.getSlingModelName())
                                        .replace("{%SLING_MODEL_NAME_FULL%}", componentConfig.getFullyQualifiedSlingModelName());
                                contentLines.add(line);
                            }
                            LOGGER.debug("Finished read and replace");

                            writeLinesToFile(implFile, contentLines);
                        }
                    }
                }
                newlyCreatedFiles.add(implFile);
            }

        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating Sling model files", ioe);
        }

        LOGGER.info("Finished createSlingModelCodeFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
        return newlyCreatedFiles;
    }

    private String getFullComponentPath() {
        return project.getBasePath() +
                PathUtils.validatePath(componentConfig.getUiAppsRoot(), true, true) +
                PathUtils.validatePath(componentConfig.getFullComponentName(), false, true);
    }

    private List<File> createClientlib(String clientlibType) throws ComponentCreationException {
        clientlibType = clientlibType.toLowerCase();

        final List<File> newlyCreatedFiles = new ArrayList<>();

        final String clientlibPath = getFullComponentPath() + "clientlib";
        final String clientlibTypePath = PathUtils.validatePath(clientlibPath, false, true) + clientlibType;
        File clientlibDir = new File(clientlibTypePath);
        LOGGER.debug("Making directory path: " + clientlibDir.getAbsolutePath());
        clientlibDir.mkdirs();

        //make actual files
        try {
            //clientlib overview file (js.txt / css.txt)
            final File overviewFile = new File(PathUtils.validatePath(clientlibPath, false, true) + clientlibType + ".txt");
            if(!overviewFile.exists()) {
                try (BufferedWriter overviewFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(overviewFile)))) {
                    overviewFileWriter.write("#base=" + clientlibType);
                    overviewFileWriter.newLine();
                    overviewFileWriter.newLine();
                    overviewFileWriter.write(componentConfig.getShortComponentName() + "." + clientlibType);
                }
                newlyCreatedFiles.add(overviewFile);
            }

            //clientlib folder .content.xml file
            File contentFile = new File(PathUtils.validatePath(clientlibPath, false, true) + ".content.xml");
            if(!contentFile.exists()) {
                LOGGER.debug("Clientlib content XML file does not exist yet... Continuing with creation");
                try (InputStream clientlibContentStream = PathUtils.getResourceAsStream(PathUtils.getTemplatePath("clientlib.content.xml"))) {
                    if (clientlibContentStream == null) {
                        throw new ComponentCreationException("An error occurred while reading the clientlib content.xml template");
                    }

                    LOGGER.debug("Reading template and replacing placeholders with values...");
                    try (InputStreamReader clientlibContentStreamReader = new InputStreamReader(clientlibContentStream, StandardCharsets.UTF_8)) {
                        try (BufferedReader clientlibContentReader = new BufferedReader(clientlibContentStreamReader)) {
                            List<String> contentLines = new ArrayList<>();
                            String line;
                            while ((line = clientlibContentReader.readLine()) != null) {
                                line = line.replace("{%CLIENTLIB_CATEGORY%}", componentConfig.getClientlibCategory());
                                contentLines.add(line);
                            }
                            LOGGER.debug("Finished read and replace");

                            writeLinesToFile(contentFile, contentLines);
                        }
                    }
                }
                newlyCreatedFiles.add(contentFile);
            }

            //empty clientlib impl file
            File clientlibFile = new File(PathUtils.validatePath(clientlibTypePath, false, true) + componentConfig.getShortComponentName() + "." + clientlibType);
            if(!clientlibFile.exists()) {
                if(!clientlibFile.createNewFile()) {
                    throw new ComponentCreationException("An occurred while creating the clientlib implementation file: " + clientlibFile.getName());
                }
                newlyCreatedFiles.add(clientlibFile);
            }
        } catch(IOException ioe) {
            throw new ComponentCreationException("An unexpected error occurred while creating " + clientlibType + " clientlib files", ioe);
        }
        return newlyCreatedFiles;
    }

    private void writeLinesToFile(final File target, List<String> linesToWrite) throws IOException {
        LOGGER.debug("Writing " + linesToWrite.size() + " lines to file " + target.getAbsolutePath());
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
        LOGGER.debug("Finished writing to file " + target.getAbsolutePath());
    }
}
