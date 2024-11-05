package com.yc.rtu.netcustommaster.systemInfo.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class SystemInfoResponseDto {
    private String cpuUsage;
    private String memoryUsage;

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // 오류 발생 시 빈 JSON 반환
        }
    }
}
