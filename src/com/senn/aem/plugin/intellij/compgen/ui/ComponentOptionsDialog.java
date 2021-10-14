package com.senn.aem.plugin.intellij.compgen.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import javax.swing.*;
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

    public ComponentOptionsDialog(@Nullable Project project) {
        super(project, true);

        setTitle("Component Creation Options");

        init();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return txtComponentName;
    }

    public String getSelectedValues() {
        return txtComponentName.getText();
    }
}
