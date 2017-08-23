package com.mercury.platform.ui.adr.validator;

import org.apache.commons.lang3.StringUtils;

public class IntegerFieldValidator extends RangeFieldValidator<String, Integer> {
    public IntegerFieldValidator(int min, int max) {
        super(min, max);
    }

    @Override
    public boolean validate(String value) {
        boolean numeric = StringUtils.isNumeric(value);
        if (numeric) {
            this.value = Integer.parseInt(value);
            if (this.value >= min && this.value <= max) {
                return true;
            }
        }
        return false;
    }
}
