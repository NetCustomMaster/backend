package com.yc.rtu.netcustommaster.setting.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class WifiInfoResponseDto {
    private String ssid;
    private String wifipassword;
    private String channel;
    private String band;
    private String message;
}
