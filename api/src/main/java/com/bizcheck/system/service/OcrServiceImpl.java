package com.bizcheck.system.service;

import com.bizcheck.system.RequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OcrServiceImpl implements OcrService {

    @Value("${ocr-url}")
    String apiUrl;

    @Value("${ocr-key}")
    String secretKey;
    @Override
    public String performOcr(RequestDTO requestDTO) throws IOException {

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("version", "V2");
        requestMap.put("requestId", UUID.randomUUID().toString());
        requestMap.put("timestamp", Instant.now().toEpochMilli());

        Map<String, String> imageMap = new HashMap<>();
        imageMap.put("format", "jpg");
        imageMap.put("name", "demo");

        requestMap.put("images", new Object[]{imageMap});

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(requestMap);
        MultipartFile imageFile = requestDTO.getFile();

        byte[] imageBytes = imageFile.getBytes();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("message", jsonMessage)
                .addFormDataPart("file", imageFile.getName(),
                        RequestBody.create(imageBytes, MediaType.parse("application/octet-stream")))
                .build();

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("X-OCR-SECRET", secretKey)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonNode root = objectMapper.readTree(responseBody);
                StringBuilder resultText = new StringBuilder();
                JsonNode titleNode = root.path("images").get(0).path("title");
                if (!titleNode.isMissingNode()) {
                    String text = titleNode.path("inferText").asText();
                    text = text.replaceAll("[^0-9]", "");
                    resultText.append(text);
                }
                return resultText.toString().trim();
            } else {
                return "오류: " + response.code() + " - " + response.body().string();
            }
        }
    }
}
