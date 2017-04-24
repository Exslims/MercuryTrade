package com.mercury.platform.shared.entity.adr;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.util.List;

@Data
@AllArgsConstructor
public class AdrGroupDescriptor {
    private Point location;
    private List<AdrCellDescriptor> cells;
    private float opacity;
    private float scale;
}
