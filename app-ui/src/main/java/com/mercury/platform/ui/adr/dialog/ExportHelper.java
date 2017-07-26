package com.mercury.platform.ui.adr.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;

import javax.swing.*;
import java.util.Arrays;

public class ExportHelper {
    public static void exportComponent(AdrComponentDescriptor descriptor){
//        new AdrExportDialog<>(null, Arrays.asList(descriptor)).setVisible(true);
    }
    public static void exportProfile(AdrProfileDescriptor descriptor){
        SwingUtilities.invokeLater(() ->
                new AdrExportDialog(null, descriptor).setVisible(true));
    }
}
