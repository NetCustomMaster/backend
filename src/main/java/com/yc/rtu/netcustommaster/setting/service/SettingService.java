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
    @Autowired
    private AuthService authService;

    public void Reset(){
        Properties config=authService.loadConfig();
        config.setProperty("admin.password","");
        config.setProperty("admin.username","");
        config.setProperty("first_time","true");
        authService.saveConfig(config);
    }
    //관리자 비밀번호 변경
    public boolean changeUserAuth(String username,String password){
        Properties config = authService.loadConfig();
        config.setProperty("admin.username", username);
        config.setProperty("admin.password", password);
        authService.saveConfig(config);
        return true;
    }
    public String changeWifiChannel(String channel,String path){
        String command = String.format(
                "cd /etc/hostapd && sudo sed -i 's/channel=[0-9]*$/channel=%s/' %s && "+
                        "sudo systemctl restart hostapd",
                channel,path
        );
        System.out.println(command);
        String result = executeCommand(command);
        return result.isEmpty() ? "Wifi 채널 변경 성공" : result;
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
    public String changeWifiPassword(String ssid,String password,String path){
        String command = String.format(
                "cd /etc/hostapd && sudo sed -i 's/ssid=.*/ssid=%s/' %s && sudo sed -i 's/wpa_passphrase=.*/wpa_passphrase=%s/' %s && sudo systemctl restart hostapd",
                ssid,path,password, path
        );
        System.out.println(command);
        String result = executeCommand(command);
        return result.isEmpty() ? "Wifi 설정 변경 성공" : result;
    }
}
