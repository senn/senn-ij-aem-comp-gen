package com.senn.aem.plugin.intellij.compgen.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
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

    public ComponentOptionsDialog(@Nullable Project project) {
        super(project, true);

        setTitle("Component Creation Options");

        init();
    }

    @Override
    protected void init() {
        super.init();

        //init values with session constants
        txtUiAppsRoot.setText(IJSessionConstants.UI_APPS_JCR_ROOT);
        txtCodeJavaRoot.setText(IJSessionConstants.JAVA_ROOT);
        txtPackage.setText(IJSessionConstants.PACKAGE);
        chkHtml.setSelected(IJSessionConstants.SELECT_HTML);
        chkDialogXml.setSelected(IJSessionConstants.SELECT_DIALOG_XML);
        chkEditConfigXML.setSelected(IJSessionConstants.SELECT_EDIT_CONFIG_XML);
        chkJavaScript.setSelected(IJSessionConstants.SELECT_JS);
        chkCSS.setSelected(IJSessionConstants.SELECT_CSS);
        chkCode.setSelected(IJSessionConstants.SELECT_SLING_MODEL);

        txtUiAppsRoot.setInputVerifier(new NotEmptyVerifier());
        txtCodeJavaRoot.setInputVerifier(new NotEmptyVerifier());
        txtComponentName.setInputVerifier(new NotEmptyVerifier());
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

    private static class NotEmptyVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent jComponent) {
            return StringUtils.isNotBlank(((JTextField)jComponent).getText());
        }
    }

}
