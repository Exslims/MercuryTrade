package com.mercury.platform.ui.adr.routing;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdrPageDefinition<T> {
    private AdrPageState state;
    private T payload;
}
