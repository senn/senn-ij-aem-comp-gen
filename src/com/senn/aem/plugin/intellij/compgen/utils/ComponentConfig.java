package com.senn.aem.plugin.intellij.compgen.utils;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

public class ComponentConfig {

    private final String componentName;
    private final String packageName;
    private final String uiAppsRoot;
    private final String javaCodeRoot;
    private final boolean makeDialogXml;
    private final boolean makeSlingModelCode;
    private final boolean makeEditConfigXml;
    private final boolean makeJS;
    private final boolean makeCSS;
    private final boolean makeHtml;

    public ComponentConfig(String componentName, String uiAppsRoot, String javaCodeRoot, String packageName, boolean makeDialogXml, boolean makeSlingModelCode,
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

    public String getFullComponentName() {
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

    //calculated methods

    public String getClientlibCategory() {
        return StringUtils.join(getFullComponentName().split("/"), ".");
    }

    public String getShortComponentName() {
        String[] split = getFullComponentName().split("/");
        return split[split.length - 1]; //last part
    }

    public String getSlingModelName() {
        return StringUtils.capitalize(getShortComponentName());
    }

    public String getFullyQualifiedSlingModelName() {
        return getPackageName() + "." + getSlingModelName();
    }

    @Override
    public String toString() {
        return "ComponentOptions{" +
                "componentName='" + componentName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", uiAppsRoot='" + uiAppsRoot + '\'' +
                ", javaCodeRoot='" + javaCodeRoot + '\'' +
                ", makeDialogXml=" + makeDialogXml +
                ", makeSlingModelCode=" + makeSlingModelCode +
                ", makeEditConfigXml=" + makeEditConfigXml +
                ", makeJS=" + makeJS +
                ", makeCSS=" + makeCSS +
                ", makeHtml=" + makeHtml +
                '}';
    }
}
