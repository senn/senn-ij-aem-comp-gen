package com.senn.aem.plugin.intellij.compgen.create.impl;

import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.ui.UIUtils;
import com.senn.aem.plugin.intellij.compgen.utils.ComponentOptions;
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

/**
 * Ideally we want to work with actual templates in IntelliJ but for now an <i>on-the-fly</i> file generation approach will do.
 * @author bart.senn@gmail.com
 */
public class FileWritingComponentFilesCreator implements ComponentFilesCreator {

    private final Project project;
    private final ComponentOptions componentOptions;

    public FileWritingComponentFilesCreator(final Project project, final ComponentOptions componentOptions) {
        this.componentOptions = componentOptions;
        this.project = project;
    }

    @Override
    public void createHtmlFiles() {
        try(InputStream htmlStream = PathUtils.getResourceAsStream("fileTemplates/internal/senn_aem.html")) {
            if (htmlStream == null) {
                UIUtils.notifyError("An error occurred while reading the HTML template", project);
                return;
            }

            try(InputStreamReader htmlStreamReader = new InputStreamReader(htmlStream, StandardCharsets.UTF_8)) {
                try(BufferedReader htmlReader = new BufferedReader(htmlStreamReader)) {
                    List<String> htmlContentLines = new ArrayList<>();
                    String line;
                    while((line = htmlReader.readLine()) != null) {
                        line = line.replace("{%USE_CLIENTLIB_CSS%}", "CSS FROM CODE")
                                    .replace("{%USE_CLIENTLIB_JS%}", "JS FROM CODE");
                        htmlContentLines.add(line);
                    }

                    final String fullUiAppsRootPath = project.getBasePath() +
                            PathUtils.validatePath(componentOptions.getUiAppsRoot(), true, true) +
                            PathUtils.validatePath(componentOptions.getComponentName(), false, true);
                    final File componentFileDir = new File(fullUiAppsRootPath);

                    if (!componentFileDir.mkdirs() && componentFileDir.canWrite()) {
                        UIUtils.notifyError("An error occurred while creating the component folder path", project);
                        return;
                    }
                    final File htmlFile = new File(fullUiAppsRootPath + componentOptions.getComponentName() + ".html");

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
            ioe.printStackTrace();
        }
    }

    @Override
    public void createJavaScriptFiles() {

    }

    @Override
    public void createCSSFiles() {

    }

    @Override
    public void createDialogXmlFiles() {

    }

    @Override
    public void createEditConfigXmlFiles() {

    }

    @Override
    public void createSlingModelCodeFiles() {

    }

    private void log(String msg) {
        System.out.println(msg);
    }

}
