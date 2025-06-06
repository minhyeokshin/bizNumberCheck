package com.bizcheck.system;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestDTO {
    private String businessNumber;
    private MultipartFile file;
}
