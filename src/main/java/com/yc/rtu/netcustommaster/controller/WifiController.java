package com.yc.rtu.netcustommaster.controller;

import com.yc.rtu.netcustommaster.service.WifiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/setting")
public class WifiController {

    @Autowired
    private WifiService wifiService;

    // 와이파이 비밀번호 변경 API
    @PostMapping("/password")
    //이전 패스워드, 새 패스워드를 requestbody로 받고
    public String changePassword(@RequestBody Map<String,String> wifipasswords) {
        String oldPassword = wifipasswords.get("oldPassword");
        String newPassword = wifipasswords.get("newPassword");
        // 비밀번호 변경 시도
        if (wifiService.changePassword(oldPassword, newPassword)) {
            return "비밀번호 변경 완료";
        } else {
            return "비밀번호 변경 실패";
        }
    }
}
