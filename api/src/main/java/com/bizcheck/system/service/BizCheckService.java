package com.bizcheck.system.service;

import com.bizcheck.system.RequestDTO;
import com.bizcheck.system.ResponseDTO;

import java.io.IOException;

public interface BizCheckService {
    ResponseDTO check(RequestDTO requestDTO) throws IOException;
}
