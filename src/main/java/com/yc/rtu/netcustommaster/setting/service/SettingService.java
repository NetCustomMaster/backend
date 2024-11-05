package com.yc.rtu.netcustommaster.setting.service;

import com.yc.rtu.netcustommaster.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Properties;

import static com.yc.rtu.netcustommaster.util.CommandExecutor.executeCommand;

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
    public String changeWifiBand(String path,String hwMode,String channel){
        String command = String.format(
                "cd /etc/hostapd && sudo sed -i 's/hw_mode=.*/hw_mode=%s/' %s && " +
                        "sudo sed -i 's/channel=[0-9]*$/channel=%s/' %s && "+
                        "sudo systemctl restart hostapd",
                hwMode,path,channel,path
        );
        System.out.println(command);
        String result = executeCommand(command);
        return result.isEmpty() ? "Wifi 대역폭 변경 성공" : result;
    }
    //와이파이 비밀번호 변경
    public String changeWifiPassword(String password,String path){
        String command = String.format(
                "cd /etc/hostapd && sed -i 's/wpa_passphrase=.*/wpa_passphrase=%s/' %s && sudo systemctl restart hostapd",
                password, path
        );
        System.out.println(command);
        String result = executeCommand(command);
        return result.isEmpty() ? "Wifi 비밀번호 변경 성공" : result;
    }
}
