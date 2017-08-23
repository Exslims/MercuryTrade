package com.mercury.platform.ui.adr.validator;

import java.util.regex.Pattern;

public class DoubleFieldValidator extends RangeFieldValidator<String, Double> {
    public DoubleFieldValidator(Double min, Double max) {
        super(min, max);
    }

    @Override
    public boolean validate(String value) {
        String decimalPattern = "(?=.)([+-]?([0-9]*)(\\.([0-9]+))?)";
        boolean numeric = Pattern.matches(decimalPattern, value);
        if (numeric) {
            this.value = Double.parseDouble(value);
            if (this.value >= min && this.value <= max) {
                return true;
            }
        }
        return false;
    }
}
