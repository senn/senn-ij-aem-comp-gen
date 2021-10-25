package com.senn.aem.plugin.intellij.compgen.ui;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
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

}
