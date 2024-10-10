package com.yc.rtu.netcustommaster.service;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
    // 메모리에 저장되는 관리자 정보
    private String adminUsername = "admin";
    private String adminPassword = "admin123";

    // 관리자 인증 메소드
    public boolean authenticate(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }
}
