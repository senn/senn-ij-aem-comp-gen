package com.senn.aem.plugin.intellij.compgen.utils;

public class ComponentOptions {

    private String componentName;
    private String packageName;
    private String uiAppsRoot;
    private String javaCodeRoot;
    private boolean makeDialogXml;
    private boolean makeSlingModelCode;
    private boolean makeEditConfigXml;
    private boolean makeJS;
    private boolean makeCSS;
    private boolean makeHtml;

    public ComponentOptions(String componentName, String uiAppsRoot, String javaCodeRoot, String packageName, boolean makeDialogXml, boolean makeSlingModelCode,
                            boolean makeEditConfigXml, boolean makeJS, boolean makeCSS, boolean makeHtml) {
        this.componentName = componentName;
        this.javaCodeRoot = javaCodeRoot;
        this.packageName = packageName;
        this.uiAppsRoot = uiAppsRoot;
        this.makeDialogXml = makeDialogXml;
        this.makeSlingModelCode = makeSlingModelCode;
        this.makeEditConfigXml = makeEditConfigXml;
        this.makeJS = makeJS;
        this.makeCSS = makeCSS;
        this.makeHtml = makeHtml;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getUiAppsRoot() {
        return uiAppsRoot;
    }

    public String getJavaCodeRoot() {
        return javaCodeRoot;
    }

    public boolean makeDialogXml() {
        return makeDialogXml;
    }

    public boolean makeSlingModelCode() {
        return makeSlingModelCode;
    }

    public boolean makeEditConfigXml() {
        return makeEditConfigXml;
    }

    public boolean makeJS() {
        return makeJS;
    }

    public boolean makeCSS() {
        return makeCSS;
    }

    public boolean makeHtml() {
        return makeHtml;
    }
}
