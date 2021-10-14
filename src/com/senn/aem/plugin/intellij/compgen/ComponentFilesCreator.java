package com.senn.aem.plugin.intellij.compgen;

public class ComponentFilesCreator {

    private static ComponentFilesCreator instance;

    private ComponentFilesCreator() {
        //TODO better constructor
    }

    public static ComponentFilesCreator getInstance() {
        if(instance == null) {
            instance = new ComponentFilesCreator();
        }
        return instance;
    }

    public void create() {
        System.out.println(System.currentTimeMillis() + " - Creating files for " + this);
    }
}
