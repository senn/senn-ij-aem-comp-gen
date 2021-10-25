package com.senn.aem.plugin.intellij.compgen.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import java.awt.*;
import javax.swing.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

public class ComponentOptionsDialog extends DialogWrapper {
    private JCheckBox chkDialogXml;
    private JPanel mainPanel;
    private JCheckBox chkCode;
    private JCheckBox chkJavaScript;
    private JCheckBox chkCSS;
    private JTextField txtComponentName;
    private JCheckBox chkEditConfigXML;
    private JCheckBox chkHtml;
    private JTextField txtPackage;
    private JTextField txtCodeJavaRoot;
    private JTextField txtUiAppsRoot;
    private JTextField txtComponentGroup;
    private JCheckBox chkOpenFiles;

    public ComponentOptionsDialog(@Nullable Project project) {
        super(project, true);

        setTitle("Component Creation Options");

        init();
    }

    @Override
    protected void init() {
        super.init();

        mainPanel.setPreferredSize(new Dimension(500, 200));

        //init values with session constants
        txtUiAppsRoot.setText(IJSessionConstants.UI_APPS_ROOT);
        txtCodeJavaRoot.setText(IJSessionConstants.JAVA_ROOT);
        txtPackage.setText(IJSessionConstants.PACKAGE);
        txtComponentGroup.setText(IJSessionConstants.COMPONENT_GROUP);
        chkHtml.setSelected(IJSessionConstants.SELECT_HTML);
        chkDialogXml.setSelected(IJSessionConstants.SELECT_DIALOG_XML);
        chkEditConfigXML.setSelected(IJSessionConstants.SELECT_EDIT_CONFIG_XML);
        chkJavaScript.setSelected(IJSessionConstants.SELECT_JS);
        chkCSS.setSelected(IJSessionConstants.SELECT_CSS);
        chkCode.setSelected(IJSessionConstants.SELECT_SLING_MODEL);
        chkOpenFiles.setSelected(IJSessionConstants.OPEN_AFTER_CREATION);

        txtUiAppsRoot.setInputVerifier(new NotEmptyVerifier());
        txtCodeJavaRoot.setInputVerifier(new NotEmptyVerifier());
        txtComponentName.setInputVerifier(new NotEmptyVerifier());
        txtComponentGroup.setInputVerifier(new NotEmptyVerifier());
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return txtComponentName;
    }

    public String getComponentName() {
        return txtComponentName.getText().toLowerCase();
    }
    public String getUiAppsRoot() {
        return txtUiAppsRoot.getText();
    }
    public String getJavaRoot() {
        return txtCodeJavaRoot.getText();
    }
    public String getPackageName() {
        return txtPackage.getText();
    }
    public String getComponentGroup() { return txtComponentGroup.getText(); }

    public boolean makeDialogXml() {
        return chkDialogXml.isSelected();
    }

    public boolean makeSlingModelCode()  {
        return chkCode.isSelected();
    }

    public boolean makeJavaScriptFiles() {
        return chkJavaScript.isSelected();
    }

    public boolean makeCSSFiles()  {
        return chkCSS.isSelected();
    }

    public boolean makeEditConfigXml() {
        return chkEditConfigXML.isSelected();
    }

    public boolean makeHtml()  {
        return chkHtml.isSelected();
    }

    public boolean openAfterCreation() { return chkOpenFiles.isSelected(); }

    private static class NotEmptyVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent jComponent) {
            return StringUtils.isNotBlank(((JTextField)jComponent).getText());
        }
    }

}
