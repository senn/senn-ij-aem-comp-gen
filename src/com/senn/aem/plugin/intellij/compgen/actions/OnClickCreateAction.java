package com.senn.aem.plugin.intellij.compgen.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.ui.ComponentOptionsDialog;
import java.io.File;
import java.io.IOException;
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
            try {
                File.createTempFile(dialog.getSelectedValues(), ".tmp", new File("c:/temp"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
