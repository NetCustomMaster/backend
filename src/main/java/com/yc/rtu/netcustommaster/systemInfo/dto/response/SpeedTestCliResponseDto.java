package com.yc.rtu.netcustommaster.systemInfo.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpeedTestCliResponseDto {
    private String ping;
    private String download;
    private String upload;
}
