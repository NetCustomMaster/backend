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
    private final String CONFIG_PATH = "backend\\src\\main\\java\\com\\yc\\rtu\\netcustommaster\\properties\\config.properties"; // 설정 파일 경로
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
    public void setAdminAndWifiCredentials(String username, String password, String wifiPassword) {
        Properties config = loadConfig();
        config.setProperty("admin.username", username);
        //비밀번호는 암호화해 저장
        config.setProperty("admin.password", passwordEncoder.encode(password));
        config.setProperty("wifi.password", wifiPassword);
        config.setProperty("first_time", "false"); // 처음 사용 여부 플래그 업데이트
        saveConfig(config);
    }

    // 관리자 아이디 비밀번호 검증
    public boolean authenticate(String username, String password) {
        Properties config = loadConfig();
        String storedUsername = config.getProperty("admin.username");
        String storedPassword = config.getProperty("admin.password");
        return storedUsername.equals(username) && passwordEncoder.matches(password, storedPassword);
    }
}