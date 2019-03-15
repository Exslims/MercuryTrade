package com.mercury.platform.ui.components.datatable.data;

import lombok.Data;

@Data
public class DataRequest {
    private LazyLoadParams lazyLoadParams;
    private SortParams sortParams;
    private String filters;
}
