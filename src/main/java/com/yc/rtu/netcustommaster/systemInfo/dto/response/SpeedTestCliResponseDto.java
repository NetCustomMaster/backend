package com.yc.rtu.netcustommaster.systemInfo.dto.response;

import lombok.Data;

@Data
public class SpeedTestCliResponseDto {
    private String ping;
    private String download;
    private String upload;
}
