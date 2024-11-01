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
        systemInfo.setConnectedDevices(getConnectedDevices());
        return systemInfo;
    }

    public SpeedTestCliResponseDto getSpeedTestCli() {
        SpeedTestCliResponseDto speedTestCli = new SpeedTestCliResponseDto();
        speedTestCli.setInternetSpeed(getInternetSpeed());
        return speedTestCli;
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
        String command = "speedtest-cli --server 50686 --simple | grep 'Ping\\|Download\\|Upload'";
        return executeCommand(command);
    }

    private List<String> getConnectedDevices() {
        String command = "arp -a"; // ARP 명령어
        String output = executeCommand(command);
        return Arrays.stream(output.split("\n")) // 줄 바꿈 기준으로 나누기
                .map(String::trim) // 각 줄의 공백 제거
                .filter(line -> !line.isEmpty()) // 빈 줄 필터링
                .collect(Collectors.toList()); // 리스트로 수집
    }
}