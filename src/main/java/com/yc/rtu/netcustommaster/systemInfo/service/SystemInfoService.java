package com.yc.rtu.netcustommaster.systemInfo.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemInfoService {

//    public Map<String, Object> getCpuUsage() {
//        Map<String, Object> response = new HashMap<>();
//        // Execute shell command to get CPU usage
//        String command = "top -bn1 | grep 'Cpu(s)' | " +
//                "sed 's/.*, *\\([0-9.]*\\)%* id.*/\\1/' | " +
//                "awk '{print 100 - $1\"%\"}'";
//        String cpuUsage = executeCommand(command);
//        response.put("cpuUsage", cpuUsage);
//        return response;
//    }

    public Map<String, Object> getCpuUsage() {
        Map<String, Object> response = new HashMap<>();
        String command = "wmic cpu get loadpercentage";
        String cpuUsage = executeCommand(command);
        response.put("cpuUsage", cpuUsage);
        return response;
    }


//    public Map<String, Object> getMemoryUsage() {
//        Map<String, Object> response = new HashMap<>();
//        // Execute shell command to get memory usage
//        String command = "free -m | awk 'NR==2{printf \"%.2f\", $3*100/$2 }'";
//        String memoryUsage = executeCommand(command);
//        response.put("memoryUsage", memoryUsage);
//        return response;
//    }

    public Map<String, Object> getMemoryUsage() {
        Map<String, Object> response = new HashMap<>();
        String command = "wmic OS get FreePhysicalMemory,TotalVisibleMemorySize /format:value";
        String memoryUsage = executeCommand(command);

        // 변환하여 사용량 비율 계산
        String[] lines = memoryUsage.split("\n");
        double totalMemory = 0;
        double freeMemory = 0;

        for (String line : lines) {
            if (line.startsWith("TotalVisibleMemorySize")) {
                totalMemory = Double.parseDouble(line.split("=")[1].trim());
            } else if (line.startsWith("FreePhysicalMemory")) {
                freeMemory = Double.parseDouble(line.split("=")[1].trim());
            }
        }
        double usedMemory = ((totalMemory - freeMemory) / totalMemory) * 100;
        response.put("memoryUsage", String.format("%.2f", usedMemory) + "%");
        return response;
    }


//    public Map<String, Object> getInternetSpeed() {
//        Map<String, Object> response = new HashMap<>();
//        // Run the speedtest CLI and parse the results
//        String command = "speedtest-cli --simple | grep 'Download\\|Upload'";
//        String speedOutput = executeCommand(command);
//        response.put("speedTest", speedOutput);
//        return response;
//    }

    public Map<String, Object> getInternetSpeed() {
        Map<String, Object> response = new HashMap<>();
        String command = "python -m speedtest-cli --simple";
        String speedOutput = executeCommand(command);
        response.put("speedTest", speedOutput);
        return response;
    }

//    public Map<String, Object> getConnectedDevices() {
//        Map<String, Object> response = new HashMap<>();
//        // Use arp-scan to find connected devices
//        String command = "arp-scan --interface=wlan0 --localnet";
//        String devices = executeCommand(command);
//        response.put("connectedDevices", devices);
//        return response;
//    }

    public Map<String, Object> getConnectedDevices() {
        Map<String, Object> response = new HashMap<>();
        String command = "arp -a";
        String devices = executeCommand(command);
        String[] deviceLines = devices.split("\\r?\\n"); // 줄바꿈 문자로 분리
        List<String> deviceList = new ArrayList<>();
        for (String line : deviceLines) {
            // 빈 줄 또는 불필요한 줄 제외
            if (!line.trim().isEmpty()) {
                deviceList.add(line.trim());
            }
        }
        response.put("connectedDevices", deviceList); // JSON 배열로 반환
        return response;
    }


    private String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

//    private String executeCommand(String command) {
//        StringBuilder output = new StringBuilder();
//        try {
//            Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return output.toString();
//    }
}
