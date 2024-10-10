package com.yc.rtu.netcustommaster.controller;

import com.yc.rtu.netcustommaster.dto.AdminDTO;
import com.yc.rtu.netcustommaster.service.AdminService;
import com.yc.rtu.netcustommaster.service.WifiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/v1/setting")
public class AdminController{
    @Autowired
    private AdminService adminService;
    @Autowired
    private WifiService wifiService;

    // 관리자 로그인 API
    @PostMapping("/login")
    public String login(@RequestBody AdminDTO adminDTO) {
        String username = adminDTO.getUsername();
        String password = adminDTO.getPasswd();

        if (adminService.authenticate(username, password)) {
            return "Login successful";
        } else {
            return "Invalid username or password";
        }
    }
    // 와이파이 비밀번호 변경 API
    @PostMapping("/password")
    //이전 패스워드, 새 패스워드를 requestbody로 받고
    public String changePassword(@RequestBody Map<String,String> passwords) {
        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");
        // 비밀번호 변경 시도
        if (wifiService.changePassword(oldPassword, newPassword)) {
            return "Password changed successfully";
        } else {
            return "Password change failed";
        }
    }
}