package com.senn.aem.plugin.intellij.compgen.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreatorFactory;
import com.senn.aem.plugin.intellij.compgen.ui.ComponentOptionsDialog;
import com.senn.aem.plugin.intellij.compgen.ui.IJSessionConstants;
import com.senn.aem.plugin.intellij.compgen.ui.UIUtils;
import com.senn.aem.plugin.intellij.compgen.utils.ComponentOptions;
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
            final ComponentOptions userOptions = new ComponentOptions(
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
            final ComponentFilesCreator creator = ComponentFilesCreatorFactory.getInstance(project, userOptions);

            //update session constants
            IJSessionConstants.SELECT_HTML = userOptions.makeHtml();
            IJSessionConstants.JAVA_ROOT = userOptions.getJavaCodeRoot();
            IJSessionConstants.PACKAGE = userOptions.getPackageName();
            IJSessionConstants.SELECT_HTML = userOptions.makeHtml();
            IJSessionConstants.SELECT_DIALOG_XML = userOptions.makeDialogXml();
            IJSessionConstants.SELECT_EDIT_CONFIG_XML = userOptions.makeEditConfigXml();
            IJSessionConstants.SELECT_JS = userOptions.makeJS();
            IJSessionConstants.SELECT_CSS = userOptions.makeCSS();
            IJSessionConstants.SELECT_SLING_MODEL = userOptions.makeSlingModelCode();
            LOGGER.info("Setting session constants to selected values: " + userOptions);

            try {
                if (userOptions.makeDialogXml()) creator.createDialogXmlFiles();
                if (userOptions.makeEditConfigXml()) creator.createEditConfigXmlFiles();
                if (userOptions.makeHtml()) creator.createHtmlFiles();
                if (userOptions.makeCSS()) creator.createCSSFiles();
                if (userOptions.makeJS()) creator.createJavaScriptFiles();
                if (userOptions.makeSlingModelCode()) creator.createSlingModelCodeFiles();

                UIUtils.notifyInfo("Component files create for '" + userOptions.getComponentName() + "'", project);
            } catch(Exception e) {
                LOGGER.error("An unexpected error occurred while trying to create the AEM component files: " + e.getMessage(), e);
                UIUtils.notifyError("An unexpected error occurred while trying to create the AEM component files: " + e.getMessage(), project);
            }
        }
    }
}
