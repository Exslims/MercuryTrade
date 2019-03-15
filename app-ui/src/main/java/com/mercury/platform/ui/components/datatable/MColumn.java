package com.mercury.platform.ui.components.datatable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MColumn {
    private String caption;
    private String selector;
    private boolean sort;
    private boolean filter;
    private Class<?> rendererClass;
}
