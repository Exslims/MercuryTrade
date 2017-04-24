package com.mercury.platform.shared.entity.adr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdrCellDescriptor {
    private String iconPath = "";
    private Dimension cellSize = new Dimension(96,96);
    private float duration = 9.0f;
    //other
}
