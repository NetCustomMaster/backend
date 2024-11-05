package com.yc.rtu.netcustommaster.device.service;

import com.yc.rtu.netcustommaster.util.CommandExecutor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yc.rtu.netcustommaster.util.CommandExecutor.executeCommand;

@Service
public class DeviceService {

    public String getTrafficData() {
        String command = "sudo iftop -i wlan0 -t -s 1"; // 1초 동안의 트래픽 모니터링
        String output = executeCommand(command);
        return extractIPAndTraffic(output);
    }

    private String extractIPAndTraffic(String output) {
        StringBuilder result = new StringBuilder();
        // 정규 표현식: IP 주소와 last 2s 값을 추출
        Pattern pattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+=>\\s+(\\S+)");
        Matcher matcher = pattern.matcher(output);

        while (matcher.find()) {
            String ipAddress = matcher.group(1);
            String last2sTraffic = matcher.group(2);
            result.append("IP: ").append(ipAddress).append(", Last 2s: ").append(last2sTraffic).append("\n");
        }
        return result.toString().trim(); // Trim to remove trailing newlines
    }
}
