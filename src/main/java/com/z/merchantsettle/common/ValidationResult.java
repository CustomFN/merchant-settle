package com.z.merchantsettle.common;

import lombok.Data;

import java.util.List;

@Data
public class ValidationResult {

    private List<String> errorMsg;
    private boolean hasError;

}
