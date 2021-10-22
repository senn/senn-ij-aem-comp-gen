package com.senn.aem.plugin.intellij.compgen;

/**
 * Checked exception for component creation
 * @author bart.senn@gmail.com
 */
public class ComponentCreationException extends Exception {

    public ComponentCreationException(String message) {
        super(message);
    }

    public ComponentCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
