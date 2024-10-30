package com.yc.rtu.netcustommaster.systemInfo.service;

import com.yc.rtu.netcustommaster.systemInfo.dto.response.SystemInfoResponseDto;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Service
public class SystemInfoService {

    public SystemInfoResponseDto getSystemInfo() {
        SystemInfoResponseDto systemInfo = new SystemInfoResponseDto();
        systemInfo.setCpuUsage(getCpuUsage());
        systemInfo.setMemoryUsage(getMemoryUsage());
        systemInfo.setInternetSpeed(getInternetSpeed());
        systemInfo.setConnectedDevices(getConnectedDevices());
        return systemInfo;
    }

    private String getCpuUsage() {
        String command = "top -bn1 | grep 'Cpu(s)' | " +
                "sed 's/.*, *\\([0-9.]*\\)%* id.*/\\1/' | " +
                "awk '{print 100 - $1\"%\"}'";
        return executeCommand(command);
    }

    private String getMemoryUsage() {
        String command = "free -m | awk 'NR==2{printf \"%.2f\", $3*100/$2 }'";
        return executeCommand(command);
    }

    private String getInternetSpeed() {
        String command = "speedtest-cli --simple | grep 'Download\\|Upload'";
        return executeCommand(command);
    }

    private List<String> getConnectedDevices() {
        String command = "arp -a"; // ARP 명령어
        String output = executeCommand(command);
        return Arrays.asList(output.split("\n")); // 결과를 리스트로 변환
    }

    private String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString().trim(); // Trim to remove trailing newlines
    }
}
