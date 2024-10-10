package com.yc.rtu.netcustommaster.controller;
import com.yc.rtu.netcustommaster.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/setting")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/auth")
    public String handleAuth(@RequestBody Map<String, String> auth) {
        String action = auth.get("action");
        String username = auth.get("username");
        String password = auth.get("password");
        // action으로 로그인, 비밀번호 변경 구분하여 처리합니다
        // 로그인 처리
        if ("login".equals(action)) {
            if (adminService.authenticate(username, password)) {
                return "로그인 성공";
            } else {
                return "로그인 실패";
            }
        }
        // 비밀번호 변경 처리
        else if ("changePassword".equals(action)) {
            String newpassword = auth.get("newpassword");
            if (adminService.authenticate(username, password) && adminService.changepassword(newpassword)) {
                return "비밀번호 변경 성공";
            } else {
                return "비밀번호 변경 실패";
            }
        }

        // 잘못된 action 값일 때
        return "잘못된 요청";
    }
}