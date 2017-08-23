package com.mercury.platform.ui.adr.dialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.shared.config.json.deserializer.AdrComponentJsonAdapter;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreePanel;
import com.mercury.platform.ui.adr.components.panel.tree.dialog.AdrDialogTreeNodeRenderer;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class AdrImportDialog extends AdrDialog {
    private JTextArea jsonArea;
    private AdrTreePanel adrTree;
    private JPanel viewPanel;
    private JPanel treePanel;
    private JLabel errorLabel;

    public AdrImportDialog(Component relative) {
        super(relative, null);
        this.setTitle("Import manager");

        MercuryStoreUI.adrManagerPack.subscribe(state -> {
            this.pack();
            this.repaint();
        });
    }

    @Override
    protected JPanel getDataPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        root.setBackground(AppThemeColor.ADR_BG);
        JLabel header = this.componentsFactory.getTextLabel("Data (Ctrl + V for paste):", FontStyle.BOLD, 18);
        header.setForeground(AppThemeColor.TEXT_NICKNAME);
        root.add(header, BorderLayout.PAGE_START);

        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.ADR_BG);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        this.jsonArea = this.componentsFactory.getSimpleTextArea("");
        this.jsonArea.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.jsonArea.addCaretListener(e -> {
            this.viewPanel.remove(this.errorLabel);
            this.viewPanel.add(this.treePanel, BorderLayout.CENTER);
            this.adrTree.setVisible(false);
            List<AdrComponentDescriptor> jsonAsObject = this.getJsonAsObject(this.jsonArea.getText());
            this.adrTree.updateTree(jsonAsObject);
            this.adrTree.setVisible(true);
            this.pack();
            this.repaint();
        });
        this.jsonArea.setMinimumSize(new Dimension(450, 550));
        this.jsonArea.setEditable(true);
        this.jsonArea.setPreferredSize(new Dimension(450, 550));
        this.jsonArea.setBackground(AppThemeColor.ADR_TEXT_ARE_BG);
        container.add(this.jsonArea, AppThemeColor.ADR_BG);
        root.add(this.componentsFactory.wrapToSlide(verticalContainer, AppThemeColor.ADR_BG, 0, 5, 4, 0), BorderLayout.CENTER);
        return this.componentsFactory.wrapToSlide(root);
    }

    @Override
    protected JPanel getViewPanel() {
        this.viewPanel = this.componentsFactory.getJPanel(new BorderLayout());
        this.viewPanel.setPreferredSize(new Dimension(240, 100));
        this.viewPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        this.viewPanel.setBackground(AppThemeColor.FRAME_RGB);
        this.errorLabel = this.componentsFactory.getIconLabel("app/error_icon.png", 26);
        this.errorLabel.setText("Some errors occurred..");
        this.errorLabel.setFont(this.componentsFactory.getFont(FontStyle.REGULAR, 16));
        this.errorLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        JPanel headerPanel = this.componentsFactory.getJPanel(new BorderLayout());
        headerPanel.setBackground(AppThemeColor.ADR_BG);

        JLabel header = this.componentsFactory.getTextLabel("View:", FontStyle.BOLD, 18);
        header.setForeground(AppThemeColor.TEXT_NICKNAME);
        headerPanel.add(header, BorderLayout.CENTER);
        this.viewPanel.add(headerPanel, BorderLayout.PAGE_START);

        this.adrTree = new AdrTreePanel(new ArrayList<>(), new AdrDialogTreeNodeRenderer());
        this.adrTree.setVisible(false);
        this.treePanel = this.componentsFactory.wrapToSlide(this.adrTree, AppThemeColor.FRAME_RGB);
        this.viewPanel.add(this.treePanel, BorderLayout.CENTER);

        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(2, 1, 0, 6));
        JButton importToCurrent = this.componentsFactory.getBorderedButton("Import to current profile");
        importToCurrent.addActionListener(action -> {
            List<AdrComponentDescriptor> descriptors = this.adrTree.getDescriptors();
            if (descriptors != null) {
                AdrComponentDefinition definition = this.prepareDefinition(descriptors);
                MercuryStoreUI.adrComponentStateSubject.onNext(definition);
                this.setVisible(false);
                this.dispose();
            }
        });
        JButton createAndImport = this.componentsFactory.getBorderedButton("Create new and import");
        createAndImport.addActionListener(action -> {
            List<AdrComponentDescriptor> descriptors = this.adrTree.getDescriptors();
            if (descriptors != null) {
                List<String> profilesNames = Configuration.get()
                        .adrConfiguration()
                        .getEntities()
                        .stream()
                        .map(AdrProfileDescriptor::getProfileName)
                        .collect(Collectors.toList());
                profilesNames.add("Create new");
                new AdrNewProfileDialog(
                        profileName -> {
                            MercuryStoreUI.adrNewProfileSubject.onNext(profileName);
                            AdrComponentDefinition definition = this.prepareDefinition(descriptors);
                            MercuryStoreUI.adrComponentStateSubject.onNext(definition);
                            this.setVisible(false);
                            this.dispose();
                        },
                        null, profilesNames).setVisible(true);
            }
        });
        buttonsPanel.add(importToCurrent);
        buttonsPanel.add(createAndImport);
        this.viewPanel.add(this.componentsFactory.wrapToSlide(buttonsPanel), BorderLayout.PAGE_END);
        return this.componentsFactory.wrapToSlide(viewPanel);
    }

    private AdrComponentDefinition prepareDefinition(List<AdrComponentDescriptor> descriptors) {
        descriptors.forEach(it -> {
            it.setComponentId(UUID.randomUUID().toString());
            if (it instanceof AdrTrackerGroupDescriptor) {
                ((AdrTrackerGroupDescriptor) it).getCells().forEach(cell -> cell.setComponentId(UUID.randomUUID().toString()));
            }
        });
        AdrComponentDefinition definition = new AdrComponentDefinition();
        definition.setDescriptors(descriptors);
        definition.setOperations(AdrComponentOperations.NEW_FROM_IMPORT);
        return definition;
    }

    private List<AdrComponentDescriptor> getJsonAsObject(String jsonStr) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();
            JsonParser jsonParser = new JsonParser();
            return gson.fromJson(
                    jsonParser.parse(jsonStr),
                    new TypeToken<List<AdrComponentDescriptor>>() {
                    }.getType());
        } catch (IllegalStateException | JsonSyntaxException e) {
            this.viewPanel.remove(this.treePanel);
            this.viewPanel.add(this.errorLabel, BorderLayout.CENTER);
            this.pack();
            this.repaint();
            MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError("Error while importing string: " + jsonStr, e));
            return null;
        }
    }
}
