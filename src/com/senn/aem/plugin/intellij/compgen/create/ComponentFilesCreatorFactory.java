package com.senn.aem.plugin.intellij.compgen.create;

import com.senn.aem.plugin.intellij.compgen.create.impl.FileWritingComponentFilesCreator;

/**
 * Simple factory that provides a {@link ComponentFilesCreator} instance.
 * @author bart.senn@gmail.com
 */
public abstract class ComponentFilesCreatorFactory {

    public static ComponentFilesCreator getInstance() {
        return new FileWritingComponentFilesCreator();
    }

}
