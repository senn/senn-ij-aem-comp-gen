package com.senn.aem.plugin.intellij.compgen.create;

import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.create.impl.FileWritingComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.utils.ComponentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple factory that provides a {@link ComponentFilesCreator} instance.
 * @author bart.senn@gmail.com
 */
public abstract class ComponentFilesCreatorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentFilesCreatorFactory.class);

    public static ComponentFilesCreator getInstance(final Project project, final ComponentConfig config) {
        final ComponentFilesCreator creator = new FileWritingComponentFilesCreator(project, config);
        LOGGER.debug("Returning specific impl instance to handle creation: " + creator.getClass().getCanonicalName());
        return creator;
    }

}
