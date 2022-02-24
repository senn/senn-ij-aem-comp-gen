package com.senn.aem.plugin.intellij.compgen;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistentSettings implements StartupActivity, ProjectManagerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistentSettings.class);

    @Override
    public void runActivity(@NotNull Project project) {
        //loadSettings(project);
    }

    @Override
    public void projectOpened(@NotNull Project project) {
        loadSettings(project);
    }

    //this method has been commented because it is scheduled for deletion in 2022.3
//    @Override
//    public boolean canCloseProject(@NotNull Project project) {
//        persistSettings(project);
//        return ProjectManagerListener.super.canCloseProject(project);
//    }

    @Override
    public void projectClosing(@NotNull Project project) {
        persistSettings(project);
    }

    public static void persistSettings(@NotNull Project project) {
        LOGGER.info("Persisting settings to workspace file...");
        final PropertiesComponent props = PropertiesComponent.getInstance(project);
        props.setValue(PluginConstants.KEY_JAVA_ROOT, IJSessionConstants.JAVA_ROOT);
        props.setValue(PluginConstants.KEY_COMPONENT_GROUP, IJSessionConstants.COMPONENT_GROUP);
        props.setValue(PluginConstants.KEY_UI_APPS_ROOT, IJSessionConstants.UI_APPS_ROOT);
        props.setValue(PluginConstants.KEY_PACKAGE, IJSessionConstants.PACKAGE);
        //for booleans nothing is persisted if the value is the same as the default value for some reason
        //see PropertiesComponentImpl.setValue(String, boolean, boolean)
        //so we explicitly provide a default that is the negation of our value
        props.setValue(PluginConstants.KEY_SELECT_HTML, IJSessionConstants.SELECT_HTML, !IJSessionConstants.SELECT_HTML);
        props.setValue(PluginConstants.KEY_SELECT_DIALOG_XML, IJSessionConstants.SELECT_DIALOG_XML, !IJSessionConstants.SELECT_DIALOG_XML);
        props.setValue(PluginConstants.KEY_SELECT_EDIT_CONFIG_XML, IJSessionConstants.SELECT_EDIT_CONFIG_XML, !IJSessionConstants.SELECT_EDIT_CONFIG_XML);
        props.setValue(PluginConstants.KEY_SELECT_JS, IJSessionConstants.SELECT_JS, !IJSessionConstants.SELECT_JS);
        props.setValue(PluginConstants.KEY_SELECT_CSS, IJSessionConstants.SELECT_CSS, !IJSessionConstants.SELECT_CSS);
        props.setValue(PluginConstants.KEY_SELECT_SLING_MODEL, IJSessionConstants.SELECT_SLING_MODEL, !IJSessionConstants.SELECT_SLING_MODEL);
        props.setValue(PluginConstants.KEY_SELECT_OPEN_AFTER_CREATION, IJSessionConstants.OPEN_AFTER_CREATION, !IJSessionConstants.OPEN_AFTER_CREATION);
        LOGGER.info("Settings successfully persisted!");
    }

    public static void loadSettings(@NotNull Project project) {
        LOGGER.info("Loading persistent settings from workspace file...");
        final PropertiesComponent props = PropertiesComponent.getInstance(project);
        int loaded = 0;
        if(props.isValueSet(PluginConstants.KEY_JAVA_ROOT)) {
            IJSessionConstants.JAVA_ROOT = props.getValue(PluginConstants.KEY_JAVA_ROOT);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_COMPONENT_GROUP)) {
            IJSessionConstants.COMPONENT_GROUP = props.getValue(PluginConstants.KEY_COMPONENT_GROUP);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_UI_APPS_ROOT)) {
            IJSessionConstants.UI_APPS_ROOT = props.getValue(PluginConstants.KEY_UI_APPS_ROOT);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_PACKAGE)) {
            IJSessionConstants.PACKAGE = props.getValue(PluginConstants.KEY_PACKAGE);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_SELECT_HTML)) {
            IJSessionConstants.SELECT_HTML = props.getBoolean(PluginConstants.KEY_SELECT_HTML);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_SELECT_DIALOG_XML)) {
            IJSessionConstants.SELECT_DIALOG_XML = props.getBoolean(PluginConstants.KEY_SELECT_DIALOG_XML);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_SELECT_EDIT_CONFIG_XML)) {
            IJSessionConstants.SELECT_EDIT_CONFIG_XML = props.getBoolean(PluginConstants.KEY_SELECT_EDIT_CONFIG_XML);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_SELECT_JS)) {
            IJSessionConstants.SELECT_JS = props.getBoolean(PluginConstants.KEY_SELECT_JS);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_SELECT_CSS)) {
            IJSessionConstants.SELECT_CSS = props.getBoolean(PluginConstants.KEY_SELECT_CSS);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_SELECT_SLING_MODEL)) {
            IJSessionConstants.SELECT_SLING_MODEL = props.getBoolean(PluginConstants.KEY_SELECT_SLING_MODEL);
            loaded++;
        }
        if(props.isValueSet(PluginConstants.KEY_SELECT_OPEN_AFTER_CREATION)) {
            IJSessionConstants.OPEN_AFTER_CREATION = props.getBoolean(PluginConstants.KEY_SELECT_OPEN_AFTER_CREATION);
            loaded++;
        }
        if(loaded > 0) {
            LOGGER.info("Settings successfully loaded!");
        } else {
            LOGGER.info("No settings found!");
        }
    }
}
