package com.senn.aem.plugin.intellij.compgen.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.ComponentConfig;
import com.senn.aem.plugin.intellij.compgen.ComponentCreationException;
import com.senn.aem.plugin.intellij.compgen.PersistentSettings;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreatorFactory;
import com.senn.aem.plugin.intellij.compgen.ui.ComponentOptionsDialog;
import com.senn.aem.plugin.intellij.compgen.IJSessionConstants;
import com.senn.aem.plugin.intellij.compgen.ui.UIUtils;
import com.senn.aem.plugin.intellij.compgen.utils.FocusPriorityFile;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action that is triggered when a user clicks on the create button
 * @author bart.senn@gmail.com
 */
public class OnClickCreateAction extends DumbAwareAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnClickCreateAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        LOGGER.info("actionPerformed called");
        final Project project = event.getData(CommonDataKeys.PROJECT);
        if (project == null) return;

        ComponentOptionsDialog dialog = new ComponentOptionsDialog(project);
        dialog.setCrossClosesWindow(true);
        dialog.setOKActionEnabled(true);
        if(dialog.showAndGet()) {
            LOGGER.debug("Clicked OK in dialog");
            final ComponentConfig compConfig = new ComponentConfig(
                    dialog.getComponentName(),
                    dialog.getComponentGroup(),
                    dialog.getUiAppsRoot(),
                    dialog.getJavaRoot(),
                    dialog.getPackageName(),
                    dialog.makeDialogXml(),
                    dialog.makeSlingModelCode(),
                    dialog.makeEditConfigXml(),
                    dialog.makeJavaScriptFiles(),
                    dialog.makeCSSFiles(),
                    dialog.makeHtml()
            );

            updateSessionConstants(dialog, project);

            try {
                long startTime = System.currentTimeMillis();
                LOGGER.debug("Starting creation of component files...");
                final List<File> createdFiles = createComponentFiles(compConfig, project);
                LOGGER.debug("Finished creation of component files! Took " + (System.currentTimeMillis() - startTime)  + "ms");

                //success notification
                UIUtils.notifyInfo("Component files created for '" + compConfig.getFullComponentName() + "'", project);

                //show in editor
                if(dialog.openAfterCreation()) {
                    LOGGER.debug("Opening " + createdFiles.size() + " files in editorMgr...");
                    createdFiles.sort(FocusPriorityFile.COMPARATOR);
                    UIUtils.openFilesInEditorAndProjectView(createdFiles, project, event);
                    LOGGER.debug("All created files are open in editor");
                } else {
                    LOGGER.debug("Not opening files in editor...");
                }
            } catch(ComponentCreationException cce) {
                //error notification
                UIUtils.notifyError(cce.getMessage(), project);
                cce.printStackTrace();
            } finally {
                //update UI
                LOGGER.debug("Refreshing UI to reflect changes and show new files");
                UIUtils.refreshUI(project);
            }

        }
    }

    private void updateSessionConstants(final ComponentOptionsDialog dialog, final Project project) {
        IJSessionConstants.SELECT_HTML = dialog.makeHtml();
        IJSessionConstants.JAVA_ROOT = dialog.getJavaRoot();
        IJSessionConstants.COMPONENT_GROUP = dialog.getComponentGroup();
        IJSessionConstants.PACKAGE = dialog.getPackageName();
        IJSessionConstants.SELECT_HTML = dialog.makeHtml();
        IJSessionConstants.SELECT_DIALOG_XML = dialog.makeDialogXml();
        IJSessionConstants.SELECT_EDIT_CONFIG_XML = dialog.makeEditConfigXml();
        IJSessionConstants.SELECT_JS = dialog.makeJavaScriptFiles();
        IJSessionConstants.SELECT_CSS = dialog.makeCSSFiles();
        IJSessionConstants.SELECT_SLING_MODEL = dialog.makeSlingModelCode();
        IJSessionConstants.UI_APPS_ROOT = dialog.getUiAppsRoot();
        IJSessionConstants.OPEN_AFTER_CREATION = dialog.openAfterCreation();
        IJSessionConstants.PERSIST_SETTINGS = dialog.persistSettings();
        LOGGER.info("Set session constants to selected values");

        if(dialog.persistSettings()) {
            PersistentSettings.persistSettings(project);
            LOGGER.info("Also persisted session constants to selected values");
        }

    }

    private List<File> createComponentFiles(final ComponentConfig config, final Project project) throws ComponentCreationException {
        final ComponentFilesCreator creator = ComponentFilesCreatorFactory.getInstance(project, config);

        final List<File> createdFiles = new ArrayList<>(creator.createComponentXmlFiles());
        //optional
        if (config.makeDialogXml()) createdFiles.addAll(creator.createDialogXmlFiles());
        if (config.makeEditConfigXml()) createdFiles.addAll(creator.createEditConfigXmlFiles());
        if (config.makeHtml()) createdFiles.addAll(creator.createHtmlFiles());
        if (config.makeCSS()) createdFiles.addAll(creator.createCSSFiles());
        if (config.makeJS()) createdFiles.addAll(creator.createJavaScriptFiles());
        if (config.makeSlingModelCode()) createdFiles.addAll(creator.createSlingModelCodeFiles());

        return createdFiles;
    }
}
