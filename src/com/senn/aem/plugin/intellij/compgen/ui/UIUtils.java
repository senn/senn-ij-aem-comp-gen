package com.senn.aem.plugin.intellij.compgen.ui;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.ex.VirtualFileManagerEx;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UIUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UIUtils.class);

    private UIUtils() {}

    private static final String SENN_AEM_NOTIF_GROUP = "Senn.AEM";

    public static void notifyError(final String message, final Project project) {
        LOGGER.debug("Using IntelliJ NotificationGroupManager to notify with type=ERROR: " + message);
        NotificationGroupManager.getInstance()
                .getNotificationGroup(SENN_AEM_NOTIF_GROUP)
                .createNotification(message, NotificationType.ERROR)
                .notify(project);
    }

    public static void notifyInfo(final String message, final Project project) {
        LOGGER.debug("Using IntelliJ NotificationGroupManager to notify with type=INFORMATION: " + message);
        NotificationGroupManager.getInstance()
                .getNotificationGroup(SENN_AEM_NOTIF_GROUP)
                .createNotification(message, NotificationType.INFORMATION)
                .notify(project);
    }

    public static void refreshUI(final Project project) {
        VirtualFileManagerEx.getInstance().refreshWithoutFileWatcher(false);
        ProjectView.getInstance(project).refresh();
    }

    public static void openFilesInEditorAndProjectView(final List<File> files, final Project project, final AnActionEvent parentEvent) {
        if(files == null || files.isEmpty()) return;

        files.forEach(file -> {
            final VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
            if(virtualFile != null) {
                openFileInEditor(virtualFile, project);
                navigateToFile(virtualFile, project);
            }
        });

    }

    public static void openFileInEditor(final VirtualFile virtualFile, final Project project) {
        if(virtualFile == null || !virtualFile.isValid()) return;

        FileEditorManager.getInstance(project).openTextEditor(
                new OpenFileDescriptor(
                        project,
                        virtualFile,
                        0),
                true);
    }

    public static void navigateToFile(final VirtualFile virtualFile, final Project project) {
        final PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        if (psiFile != null) {
            ProjectViewImpl view = ((ProjectViewImpl) ProjectView.getInstance(project));
            view.selectPsiElement(psiFile, true);
        }
    }

}
