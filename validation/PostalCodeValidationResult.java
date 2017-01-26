package com.lodoss.examples.validation;

import android.content.ContextWrapper;

import com.lodoss.examples.R;

/**
 * Container for information about validation(success / error + message)
 */
public class PostalCodeValidationResult {
    private static String VALIDATION_RESULT;

    private boolean isValid;
    private String message;

    public PostalCodeValidationResult(boolean isValid, String message, ContextWrapper cw) {
        this.isValid = isValid;
        this.message = message;
        VALIDATION_RESULT = cw.getString(R.string.pattern_is_valid_message_postal_code);
    }

    public PostalCodeValidationResult() {
    }

    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getMessage() {
        if(message == null){
            return "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PostalCodeValidationResult withValidationResult(boolean isValid){
        this.isValid = isValid;
        return this;
    }

    public PostalCodeValidationResult withMessage(String message){
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return String.format(VALIDATION_RESULT, String.valueOf(isValid), getMessage());
    }
}
