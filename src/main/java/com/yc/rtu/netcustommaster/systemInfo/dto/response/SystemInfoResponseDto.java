package com.yc.rtu.netcustommaster.systemInfo.dto.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SystemInfoResponseDto {
    private String cpuUsage;
    private String memoryUsage;
    private String internetSpeed;
    private List<String> connectedDevices;
}
