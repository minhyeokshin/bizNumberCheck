package com.bizcheck.system;

import lombok.Data;

@Data
public class ResponseDTO {
    private String businessNumber;
    private String ocrNumber;
    private boolean isValid;
    private String message;
}
