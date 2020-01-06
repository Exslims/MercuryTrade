package com.mercury.platform.ui.components.datatable.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LazyLoadParams {
    private int skip;
    private int top;
}
