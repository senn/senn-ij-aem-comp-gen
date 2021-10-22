package com.senn.aem.plugin.intellij.compgen.ui;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public final class UIUtils {

    private UIUtils() {}

    private static final String SENN_AEM_NOTIF_GROUP = "Senn.AEM";

    public static void notifyError(final String message, final Project project) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(SENN_AEM_NOTIF_GROUP)
                .createNotification(message, NotificationType.ERROR)
                .notify(project);
    }

    public static void notifyInfo(final String message, final Project project) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(SENN_AEM_NOTIF_GROUP)
                .createNotification(message, NotificationType.INFORMATION)
                .notify(project);
    }

}
