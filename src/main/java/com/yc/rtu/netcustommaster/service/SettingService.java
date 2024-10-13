package com.yc.rtu.netcustommaster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SettingService {
    //비밀번호 보안 강화를 위한 암호화 도구 사용
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private AuthService authService;
    //관리자 비밀번호 변경
    public boolean changePassword(String password){
        Properties config = authService.loadConfig();
        config.setProperty("admin.password", passwordEncoder.encode(password));
        authService.saveConfig(config);
        return true;
    }
    //와이파이 비밀번호 변경
    public boolean changeWifiPassword(String password){
        Properties config = authService.loadConfig();
        config.setProperty("wifi.password", password);
        authService.saveConfig(config);
        return true;
    }
}
