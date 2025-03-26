package com.dataspace.exception;

import java.util.List;
import java.util.Map;

public class CustomValidationException extends RuntimeException {

    private final List<Map<String, Object>> errorList;

    public CustomValidationException(String message, List<Map<String, Object>> errorList) {
        super(message);
        this.errorList = errorList;
    }

    public List<Map<String, Object>> getErrorList() {
        return errorList;
    }
}
