package com.mercury.platform.ui.adr.validator;


import java.util.regex.Pattern;

public class HexFieldValidator extends FieldValidator<String, String> {
    @Override
    public boolean validate(String value) {
        String hexPattern = "#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})";
        value = value.toLowerCase();
        boolean hex = Pattern.matches(hexPattern, value);
        if (hex) {
            this.value = value;
            return true;
        }
        return false;
    }
}
