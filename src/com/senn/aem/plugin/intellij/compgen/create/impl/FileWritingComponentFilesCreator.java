package com.senn.aem.plugin.intellij.compgen.create.impl;

import com.intellij.openapi.module.Module;
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

    private final Project project;
    private final ComponentConfig componentConfig;

    public FileWritingComponentFilesCreator(final Project project, final ComponentConfig componentOptions) {
        this.componentConfig = componentOptions;
        this.project = project;
    }

    @Override
    public void createHtmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createHtmlFiles for " + componentConfig.getFullComponentName());

        try(InputStream htmlStream = PathUtils.getResourceAsStream("fileTemplates/internal/senn_aem.html")) {
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

                    final String fullUiAppsRootPath = project.getBasePath() +
                            PathUtils.validatePath(componentConfig.getUiAppsRoot(), true, true) +
                            PathUtils.validatePath(componentConfig.getFullComponentName(), false, true);
                    final File componentFileDir = new File(fullUiAppsRootPath);

                    if (!componentFileDir.mkdirs() && componentFileDir.canWrite()) {
                        throw new ComponentCreationException("An error occurred while creating the component folder path: " + fullUiAppsRootPath);
                    }
                    final File htmlFile = new File(fullUiAppsRootPath + componentConfig.getShortComponentName() + ".html");

                    try(BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile)))) {
                        htmlContentLines.forEach(htmlLine -> {
                            try {
                                htmlWriter.write(htmlLine);
                                htmlWriter.newLine();
                            } catch(IOException ioe) {
                                ioe.printStackTrace();
                            }
                        });
                    }
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
        //TODO: impl
        LOGGER.info("Finished createJavaScriptFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createCSSFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createCSSFiles for " + componentConfig.getFullComponentName());
        //TODO: impl
        LOGGER.info("Finished createCSSFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createDialogXmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createDialogXmlFiles for " + componentConfig.getFullComponentName());
        //TODO: impl
        LOGGER.info("Finished createDialogXmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createEditConfigXmlFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createEditConfigXmlFiles for " + componentConfig.getFullComponentName());
        //TODO: impl
        LOGGER.info("Finished createEditConfigXmlFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void createSlingModelCodeFiles() throws ComponentCreationException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started createSlingModelCodeFiles for " + componentConfig.getFullComponentName());
        //TODO: impl
        LOGGER.info("Finished createSlingModelCodeFiles for " + componentConfig.getFullComponentName() + " in " + (System.currentTimeMillis() - startTime) + "ms");
    }

}
