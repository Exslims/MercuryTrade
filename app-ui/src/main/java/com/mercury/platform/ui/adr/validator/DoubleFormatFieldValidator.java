package com.mercury.platform.ui.adr.validator;

import java.util.Arrays;
import java.util.List;

public class DoubleFormatFieldValidator extends FieldValidator<String, String> {
    @Override
    public boolean validate(String value) {
        if (validPatterns().contains(value)) {
            this.value = value;
            return true;
        }
        return false;
    }

    private List<String> validPatterns() {
        return Arrays.asList(
                "0",
                "0.0",
                "0.00",
                "0.000",
                "0.0000"
        );
    }
}
