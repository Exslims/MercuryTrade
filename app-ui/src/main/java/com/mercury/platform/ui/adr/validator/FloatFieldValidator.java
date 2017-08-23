package com.mercury.platform.ui.adr.validator;


import java.util.regex.Pattern;

public class FloatFieldValidator extends RangeFieldValidator<String, Float> {
    public FloatFieldValidator(Float min, Float max) {
        super(min, max);
    }

    @Override
    public boolean validate(String value) {
        String decimalPattern = "(?=.)([+-]?([0-9]*)(\\.([0-9]+))?)";
        boolean numeric = Pattern.matches(decimalPattern, value);
        if (numeric) {
            this.value = Float.parseFloat(value);
            if (this.value >= min && this.value <= max) {
                return true;
            }
        }
        return false;
    }
}
