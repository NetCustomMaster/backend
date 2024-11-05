package com.yc.rtu.netcustommaster.device.service;

import com.yc.rtu.netcustommaster.device.dto.response.DeviceResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yc.rtu.netcustommaster.util.CommandExecutor.executeCommand;

@Slf4j
@Service
public class DeviceService {

    public DeviceResponseDto getTrafficData() {
        String command = "sudo iftop -i eth0 -t -s 1"; // 1초 동안의 트래픽 모니터링
        String output = executeCommand(command);
        return extractIPAndTraffic(output);
    }

    private DeviceResponseDto extractIPAndTraffic(String output) {
        DeviceResponseDto responseDto = new DeviceResponseDto();
        List<DeviceResponseDto.TrafficInfo> trafficInfos = new ArrayList<>();

        // 송신 및 수신 트래픽을 모두 추출하기 위한 정규 표현식
        Pattern pattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(<=|=>)\\s+(\\S+)");
        Matcher matcher = pattern.matcher(output);

        while (matcher.find()) {
            DeviceResponseDto.TrafficInfo trafficInfo = new DeviceResponseDto.TrafficInfo();
            trafficInfo.setIpAddress(matcher.group(1));
            trafficInfo.setLast2sTraffic(matcher.group(3)); // 트래픽 값 설정

            // 송신 또는 수신에 따라 방향 설정
            if ("=>".equals(matcher.group(2))) {
                trafficInfo.setDirection("sent");
            } else {
                trafficInfo.setDirection("received");
            }

            trafficInfos.add(trafficInfo);
        }

        responseDto.setTrafficInfos(trafficInfos);
        log.info("responseDto : {}", responseDto);
        return responseDto; // DTO 반환
    }
}
