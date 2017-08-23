package com.mercury.platform.ui.adr.validator;


import lombok.Getter;

public abstract class FieldValidator<F, T> {
    @Getter
    protected T value;

    public abstract boolean validate(F value);
}
