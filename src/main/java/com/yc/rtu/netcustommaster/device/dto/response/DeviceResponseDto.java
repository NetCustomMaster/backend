package com.yc.rtu.netcustommaster.device.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class DeviceResponseDto {
    private List<TrafficInfo> trafficInfos;

    @Data
    @ToString
    public static class TrafficInfo {
        private String ipAddress;
        private String last2sTraffic;
    }

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
