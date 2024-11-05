package com.yc.rtu.netcustommaster.setting.controller;

import com.yc.rtu.netcustommaster.auth.service.AuthService;
import com.yc.rtu.netcustommaster.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.yc.rtu.netcustommaster.util.CommandExecutor.executeCommand;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {
    @Autowired
    private AuthService authService;
    @Autowired
    private SettingService settingService;
    final String path = "/etc/hostapd/hostapd.conf";
    //Wifi 대역폭 설정
    @PatchMapping("/band")
    public String updateWifiBand(@RequestBody Map<String, String> request) {
        String band = request.get("band");
        //hostapd.conf 파일에서 hwMode 가 g이면 2.4ghz a이면 5ghz인데
        //사용자의 band 입력값에 따라 이를 설정
        String hwMode = "g";
        String channel = "6";
        if ("2".equals(band)) {
            hwMode = "g";
            channel = "6";
        } else if ("5".equals(band)) {
            hwMode = "a";
            channel = "36";
        }
        // hostapd.conf 파일 수정 명령어를 실행하도록 하여 구현
        System.out.println("PATCH 요청 수신됨: " + request);
        settingService.changeWifiBand(path,hwMode,channel);
        return "Wi-Fi 주파수 대역이 성공적으로 업데이트되었습니다.";

    }

    //관리자 비밀번호 변경
    @PatchMapping("/password")
    public String handleChangePassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String newpassword = request.get("newpassword");
        if (authService.authenticate(username, password) && settingService.changePassword(newpassword)) {
            return "비밀번호 변경 성공";
        } else {
            return "비밀번호 변경 실패";
        }
    }

    @PatchMapping("/wifipassword")
    public String changePassword(@RequestBody Map<String,String> request) {
        String newPassword = request.get("newpassword");
        // 비밀번호 변경 시도
        try{
            settingService.changeWifiPassword(newPassword,path);
        }catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
        return "와이파이 비밀번호 변경 성공";
    }
}
