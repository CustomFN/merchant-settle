package com.z.merchantsettle.common;

import lombok.Data;

import java.util.List;

@Data
public class ValidationResult {

    private List<String> errorMsg;
    private boolean hasError;

    public String getErrorMsgStr() {
        StringBuilder builder = new StringBuilder();
        for (String msg : errorMsg) {
            builder.append(msg).append("\r\n");
        }
        return builder.toString();
    }
}
