package com.mercury.platform.ui.adr.validator;

import lombok.Getter;

//todo
public abstract class RangeFieldValidator<F, T extends Number> extends FieldValidator<F, T> {
    @Getter
    protected T max;
    @Getter
    protected T min;

    public RangeFieldValidator(T min, T max) {
        this.max = max;
        this.min = min;
    }
}
