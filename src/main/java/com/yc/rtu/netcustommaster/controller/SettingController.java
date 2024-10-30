package com.yc.rtu.netcustommaster.controller;

import com.yc.rtu.netcustommaster.service.AuthService;
import com.yc.rtu.netcustommaster.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {
    @Autowired
    private AuthService authService;
    @Autowired
    private SettingService settingService;

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
    
    // 와이파이 비밀번호 변경 API
    @PatchMapping("/wifipassword")
    //이전 패스워드, 새 패스워드를 받고
    public String changePassword(@RequestBody Map<String,String> request) {
        String newPassword = request.get("newpassword");
        // 비밀번호 변경 시도
        if (settingService.changeWifiPassword(newPassword)) {
            return "비밀번호 변경 완료";
        } else {
            return "비밀번호 변경 실패";
        }
    }
}
