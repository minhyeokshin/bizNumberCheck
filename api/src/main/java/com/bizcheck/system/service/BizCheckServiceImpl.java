package com.bizcheck.system.service;

import com.bizcheck.system.RequestDTO;
import com.bizcheck.system.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BizCheckServiceImpl implements BizCheckService {


    private final NtsBizCheckService ntsBizCheckService;
    private final OcrService ocrService;
    private final AiBizCheckService aiBizCheckService;

    @Override
    public ResponseDTO check(RequestDTO requestDTO) throws IOException {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setBusinessNumber(requestDTO.getBusinessNumber());

        // 1. 국세청 사업자 검증
        if (!ntsBizCheckService.check(requestDTO)) {
            responseDTO.setValid(false);
            responseDTO.setMessage("국세청 계속사업자 검증 실패");
            return responseDTO;
        }

        // 2. OCR 사업자 번호 추출
        responseDTO.setOcrNumber(ocrService.performOcr(requestDTO));


        // 3. GPT 사업자 번호 일치 검증
        String check = "유저 입력 사업자 번호 : " + requestDTO.getBusinessNumber() + "OCR 사업자 번호 : " + responseDTO.getOcrNumber();
        String result = aiBizCheckService.check("numberValid.json",check);

        if (result.equals("true")) {
            responseDTO.setValid(true);
        }else {
            responseDTO.setValid(false);
            responseDTO.setMessage("AI 사업자 번호 매칭 실패");
        }





        return responseDTO;
    }
}
