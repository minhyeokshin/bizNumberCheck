package com.bizcheck.system.service;

import com.bizcheck.system.RequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface OcrService {
    String performOcr(RequestDTO requestDTO) throws IOException;
}
