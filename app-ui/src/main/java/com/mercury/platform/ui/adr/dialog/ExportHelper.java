package com.mercury.platform.ui.adr.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ExportHelper {
    public static void exportComponent(AdrComponentDescriptor descriptor){
        SwingUtilities.invokeLater(() ->
                new AdrExportDialog(null,
                        Arrays.stream(new AdrComponentDescriptor[]{descriptor}).collect(Collectors.toList()))
                        .setVisible(true));
    }
    public static void exportProfile(AdrProfileDescriptor descriptor){
        SwingUtilities.invokeLater(() ->
                new AdrExportDialog(null, descriptor.getContents()).setVisible(true));
    }
}
