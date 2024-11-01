package com.yc.rtu.netcustommaster.systemInfo.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SystemInfoResponseDto {
    private String cpuUsage;
    private String memoryUsage;
    private List<String> connectedDevices;

    @Override
    public String toString() {
        String devices = (connectedDevices != null) ? String.join(", ", connectedDevices) : "없음";
        return String.format("cpuUsage: %s, memoryUsage: %s, connectedDevices: [%s]", cpuUsage, memoryUsage, devices);
    }
}
