package com.yc.rtu.netcustommaster.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

@Service
public class AuthService {

    //비밀번호 보안 강화를 위한 암호화 도구 사용
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String CONFIG_PATH = "/home/hojin/backend/src/main/resources/config.properties"; // 설정 파일 경로
    // 설정 파일 읽기
    public Properties loadConfig() {
        Properties config = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_PATH)) {
            config.load(input);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return config;
    }

    // 설정 파일에 정보 저장하기
    public void saveConfig(Properties config) {
        try (FileOutputStream output = new FileOutputStream(CONFIG_PATH)) {
            config.store(output, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 처음 사용 여부 확인
    public boolean isFirstTime() {
        Properties config = loadConfig();
        return Boolean.parseBoolean(config.getProperty("first_time", "true"));
    }

    // 관리자 및 Wi-Fi 정보 설정
    public void setAdminCredentials(String username, String password,String passwordcheck) {
        Properties config = loadConfig();
        config.setProperty("admin.username", username);
        //비밀번호는 암호화해 저장
        config.setProperty("admin.password", password);
        config.setProperty("first_time", "false"); // 처음 사용 여부 플래그 업데이트
        saveConfig(config);
    }
    // 관리자 비밀번호 변경
    public void setAdminPassword(String newpassword){
        Properties config=loadConfig();
        config.setProperty("admin.password", newpassword);
        saveConfig(config);
    }
    // 관리자 아이디 비밀번호 검증
    public boolean authenticate(String username, String password) {
        Properties config = loadConfig();
        String storedUsername = config.getProperty("admin.username");
        String storedPassword = config.getProperty("admin.password");
        return storedUsername.equals(username) && storedPassword.equals(password);
    }
    public boolean authenticate(String password) {
        Properties config = loadConfig();
        String storedPassword = config.getProperty("admin.password");
        return storedPassword.equals(password);
    }
}