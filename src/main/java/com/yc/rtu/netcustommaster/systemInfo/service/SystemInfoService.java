package com.yc.rtu.netcustommaster.systemInfo.service;

import com.yc.rtu.netcustommaster.systemInfo.dto.response.SpeedTestCliResponseDto;
import com.yc.rtu.netcustommaster.systemInfo.dto.response.SystemInfoResponseDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.yc.rtu.netcustommaster.util.CommandExecutor.executeCommand;

@Service
public class SystemInfoService {

    public SystemInfoResponseDto getSystemInfo() {
        SystemInfoResponseDto systemInfo = new SystemInfoResponseDto();
        systemInfo.setCpuUsage(getCpuUsage());
        systemInfo.setMemoryUsage(getMemoryUsage());
        return systemInfo;
    }

    public SpeedTestCliResponseDto getSpeedTestCli() {
        SpeedTestCliResponseDto speedTestCli = new SpeedTestCliResponseDto();
        String result = getInternetSpeed();
        SpeedTestCliResponseDto dto = new SpeedTestCliResponseDto();
        String[] lines = result.split("\n");

        for (String line : lines) {
            if (line.startsWith("Ping:")) {
                dto.setPing(line.split(": ")[1]);
            } else if (line.startsWith("Download:")) {
                dto.setDownload(line.split(": ")[1]);
            } else if (line.startsWith("Upload:")) {
                dto.setUpload(line.split(": ")[1]);
            }
        }
        return dto;
    }

    private String getCpuUsage() {
        String command = "top -bn2 -d 0.5 | grep \"Cpu(s)\" | tail -n 1 | awk '{ usage = 100 - $8; printf \"%.1f%%\\n\", usage }'";
        return executeCommand(command);
    }

    private String getMemoryUsage() {
        String command = "free -m | awk 'NR==2{printf \"%.2f\", $3*100/$2 }'";
        return executeCommand(command);
    }

    private String getInternetSpeed() {
        String command = "speedtest-cli --simple | grep 'Ping\\|Download\\|Upload'";
        return executeCommand(command);
    }
}