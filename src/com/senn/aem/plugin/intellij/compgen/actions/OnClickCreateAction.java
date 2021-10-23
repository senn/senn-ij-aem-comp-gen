package com.senn.aem.plugin.intellij.compgen.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.ex.VirtualFileManagerEx;
import com.senn.aem.plugin.intellij.compgen.ComponentCreationException;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreatorFactory;
import com.senn.aem.plugin.intellij.compgen.ui.ComponentOptionsDialog;
import com.senn.aem.plugin.intellij.compgen.ui.IJSessionConstants;
import com.senn.aem.plugin.intellij.compgen.ui.UIUtils;
import com.senn.aem.plugin.intellij.compgen.utils.ComponentConfig;
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
            final ComponentConfig compConfig = new ComponentConfig(
                    dialog.getComponentName(),
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

            updateSessionConstants(compConfig);

            try {
                createComponentFiles(compConfig, project);

                //success notification
                UIUtils.notifyInfo("Component files created for '" + compConfig.getFullComponentName() + "'", project);
            } catch(ComponentCreationException cce) {
                //error notification
                UIUtils.notifyError(cce.getMessage(), project);
            }
            //update UI
            VirtualFileManagerEx.getInstance().refreshWithoutFileWatcher(false);
        }
    }

    private void updateSessionConstants(final ComponentConfig cfg) {
        IJSessionConstants.SELECT_HTML = cfg.makeHtml();
        IJSessionConstants.JAVA_ROOT = cfg.getJavaCodeRoot();
        IJSessionConstants.PACKAGE = cfg.getPackageName();
        IJSessionConstants.SELECT_HTML = cfg.makeHtml();
        IJSessionConstants.SELECT_DIALOG_XML = cfg.makeDialogXml();
        IJSessionConstants.SELECT_EDIT_CONFIG_XML = cfg.makeEditConfigXml();
        IJSessionConstants.SELECT_JS = cfg.makeJS();
        IJSessionConstants.SELECT_CSS = cfg.makeCSS();
        IJSessionConstants.SELECT_SLING_MODEL = cfg.makeSlingModelCode();
        IJSessionConstants.UI_APPS_JCR_ROOT = cfg.getUiAppsRoot();
        LOGGER.info("Setting session constants to selected values: " + cfg);
    }

    private void createComponentFiles(final ComponentConfig userOptions, final Project project) throws ComponentCreationException {
        final ComponentFilesCreator creator = ComponentFilesCreatorFactory.getInstance(project, userOptions);
        if (userOptions.makeDialogXml()) creator.createDialogXmlFiles();
        if (userOptions.makeEditConfigXml()) creator.createEditConfigXmlFiles();
        if (userOptions.makeHtml()) creator.createHtmlFiles();
        if (userOptions.makeCSS()) creator.createCSSFiles();
        if (userOptions.makeJS()) creator.createJavaScriptFiles();
        if (userOptions.makeSlingModelCode()) creator.createSlingModelCodeFiles();
    }
}
