package com.senn.aem.plugin.intellij.compgen.create;

import com.intellij.openapi.project.Project;
import com.senn.aem.plugin.intellij.compgen.create.impl.FileWritingComponentFilesCreator;
import com.senn.aem.plugin.intellij.compgen.utils.ComponentOptions;

/**
 * Simple factory that provides a {@link ComponentFilesCreator} instance.
 * @author bart.senn@gmail.com
 */
public abstract class ComponentFilesCreatorFactory {

    public static ComponentFilesCreator getInstance(final Project project, final ComponentOptions options) {
        return new FileWritingComponentFilesCreator(project, options);
    }

}
