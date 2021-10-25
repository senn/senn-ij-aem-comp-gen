package com.senn.aem.plugin.intellij.compgen;

import org.apache.commons.lang.StringUtils;

public class ComponentConfig {

    private final String componentName;
    private final String componentGroup;
    private final String packageName;
    private final String uiAppsRoot;
    private final String javaCodeRoot;
    private final boolean makeDialogXml;
    private final boolean makeSlingModelCode;
    private final boolean makeEditConfigXml;
    private final boolean makeJS;
    private final boolean makeCSS;
    private final boolean makeHtml;

    public ComponentConfig(String componentName, String componentGroup, String uiAppsRoot, String javaCodeRoot, String packageName, boolean makeDialogXml, boolean makeSlingModelCode,
                           boolean makeEditConfigXml, boolean makeJS, boolean makeCSS, boolean makeHtml) {
        this.componentName = componentName;
        this.componentGroup = componentGroup;
        this.javaCodeRoot = javaCodeRoot;
        this.uiAppsRoot = uiAppsRoot;
        this.makeDialogXml = makeDialogXml;
        this.makeSlingModelCode = makeSlingModelCode;
        this.makeEditConfigXml = makeEditConfigXml;
        this.makeJS = makeJS;
        this.makeCSS = makeCSS;
        this.makeHtml = makeHtml;

        if(packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        this.packageName = packageName;
    }

    public String getFullComponentName() {
        return componentName;
    }

    public String getComponentGroup() { return componentGroup; }

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
        return getCapitalizedComponentShortName();
    }

    public String getComponentTitle() {
        return getCapitalizedComponentShortName();
    }
    private String getCapitalizedComponentShortName() {
        return StringUtils.capitalize(getShortComponentName());
    }

    public String getFullyQualifiedSlingModelName() {
        String pkg = getPackageName();
        if(StringUtils.isNotBlank(pkg)) {
            pkg += ".";
        }
        return pkg + getSlingModelName();
    }

    @Override
    public String toString() {
        return "ComponentOptions{" +
                "componentName='" + componentName + '\'' +
                ", componentGroup='" + componentGroup + '\'' +
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
