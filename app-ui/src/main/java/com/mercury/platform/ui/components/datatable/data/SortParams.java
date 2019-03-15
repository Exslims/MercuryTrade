package com.mercury.platform.ui.components.datatable.data;

import lombok.Data;

@Data
public class SortParams {
    private String columnName;
    private boolean reverse;
}
