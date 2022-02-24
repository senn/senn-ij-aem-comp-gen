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

    /**
     * Creates an error notification
     * @see #notify(String, Project, NotificationType)
     * @param message the notification message
     * @param project the current project
     */
    public static void notifyError(final String message, final Project project) {
        notify(message, project, NotificationType.ERROR);
    }

    /**
     * Creates an info notification
     * @see #notify(String, Project, NotificationType)
     * @param message the notification message
     * @param project the current project
     */
    public static void notifyInfo(final String message, final Project project) {
        notify(message, project, NotificationType.INFORMATION);
    }

    /**
     * Creates a notification for the provided type
     * @param message the notification message
     * @param project the current project
     * @param notificationType the notification type
     */
    public static void notify(final String message, final Project project, final NotificationType notificationType) {
        LOGGER.debug("Using IntelliJ NotificationGroupManager to notify with type=" + notificationType.name() + ": " + message);
        NotificationGroupManager.getInstance()
                .getNotificationGroup(SENN_AEM_NOTIF_GROUP)
                .createNotification(message, notificationType)
                .notify(project);
    }

    /**
     * Refreshes the workspace UI
     * @param project the current project
     */
    public static void refreshUI(final Project project) {
        VirtualFileManagerEx.getInstance().refreshWithoutFileWatcher(false);
        ProjectView.getInstance(project).refresh();
    }

    /**
     * Opens a list of files in the editor and tree view
     * @param files the list of files to open
     * @param project the current project
     */
    public static void openFilesInEditorAndProjectView(final List<File> files, final Project project) {
        if(files == null || files.isEmpty()) return;

        files.forEach(file -> {
            final VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
            if(virtualFile != null) {
                openFileInEditor(virtualFile, project);
                navigateToFile(virtualFile, project);
            }
        });

    }

    /**
     * Opens a "virtual file" in the editor
     * @param virtualFile the virtual file to open
     * @param project the current project
     */
    public static void openFileInEditor(final VirtualFile virtualFile, final Project project) {
        if(virtualFile == null || !virtualFile.isValid()) return;

        FileEditorManager.getInstance(project).openTextEditor(
                new OpenFileDescriptor(
                        project,
                        virtualFile,
                        0),
                true);
    }

    /**
     * Navigates to a "virtual file" in the tree view
     * @param virtualFile the virtual file to navigate to
     * @param project the current project
     */
    public static void navigateToFile(final VirtualFile virtualFile, final Project project) {
        final PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        if (psiFile != null) {
            ProjectViewImpl view = ((ProjectViewImpl) ProjectView.getInstance(project));
            view.selectPsiElement(psiFile, true);
        }
    }

}
