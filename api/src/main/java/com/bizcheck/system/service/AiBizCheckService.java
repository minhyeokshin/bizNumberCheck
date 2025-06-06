package com.bizcheck.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface AiBizCheckService {

    String check(String openAiJson,String msg) throws IOException;
}
