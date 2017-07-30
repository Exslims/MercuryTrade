package com.mercury.platform.ui.adr.validator;


import java.util.List;

public class ProfileNameValidator extends FieldValidator<String, String> {
    private List<String> existingNames;

    public ProfileNameValidator(List<String> existingNames) {
        this.existingNames = existingNames;
    }

    @Override
    public boolean validate(String value) {
        return !existingNames.contains(value);
    }
}
