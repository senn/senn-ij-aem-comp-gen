package com.senn.aem.plugin.intellij.compgen.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.create.ComponentFilesCreatorFactory;
import com.senn.aem.plugin.intellij.compgen.ui.ComponentOptionsDialog;
import com.senn.aem.plugin.intellij.compgen.ui.IJSessionConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Action that is triggered when a user clicks on the create button
 * @author bart.senn@gmail.com
 */
public class OnClickCreateAction extends DumbAwareAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final Project project = event.getData(CommonDataKeys.PROJECT);
        if (project == null) return;

        ComponentOptionsDialog dialog = new ComponentOptionsDialog(project);
        dialog.setCrossClosesWindow(true);
        dialog.setOKActionEnabled(true);
        if(dialog.showAndGet()) {
            final ComponentFilesCreator creator = ComponentFilesCreatorFactory.getInstance();

            //update session constants
            IJSessionConstants.SELECT_HTML = dialog.makeHtml();
            IJSessionConstants.JAVA_ROOT = dialog.getJavaRoot();
            IJSessionConstants.PACKAGE = dialog.getPackageName();
            IJSessionConstants.SELECT_HTML = dialog.makeHtml();
            IJSessionConstants.SELECT_DIALOG_XML = dialog.makeDialogXml();
            IJSessionConstants.SELECT_EDIT_CONFIG_XML = dialog.makeEditConfigXml();
            IJSessionConstants.SELECT_JS = dialog.makeJavaScriptFiles();
            IJSessionConstants.SELECT_CSS = dialog.makeCSSFiles();
            IJSessionConstants.SELECT_SLING_MODEL = dialog.makeSlingModelCode();

            if(dialog.makeDialogXml()) creator.createDialogXmlFiles(dialog.getComponentName(), dialog.getUiAppsRoot());
            if(dialog.makeEditConfigXml()) creator.createEditConfigXmlFiles(dialog.getComponentName(), dialog.getUiAppsRoot());
            if(dialog.makeHtml()) creator.createHtmlFiles(dialog.getComponentName(), dialog.getUiAppsRoot());
            if(dialog.makeCSSFiles()) creator.createCSSFiles(dialog.getComponentName(), dialog.getUiAppsRoot());
            if(dialog.makeJavaScriptFiles()) creator.createJavaScriptFiles(dialog.getComponentName(), dialog.getUiAppsRoot());
            if(dialog.makeSlingModelCode()) creator.createSlingModelCodeFiles(dialog.getComponentName(), dialog.getJavaRoot(), dialog.getPackageName());
        }
    }
}
