package com.yc.rtu.netcustommaster.setting.controller;

import com.yc.rtu.netcustommaster.auth.service.AuthService;
import com.yc.rtu.netcustommaster.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.yc.rtu.netcustommaster.util.CommandExecutor.executeCommand;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {
    @Autowired
    private AuthService authService;
    @Autowired
    private SettingService settingService;
    final String path = "hostapd.conf";
    //Wifi 대역폭 설정
    @PatchMapping("/band")
    public String updateWifiBand(@RequestBody Map<String, String> request) {
        String band = request.get("band");
        //hostapd.conf 파일에서 hwMode 가 g이면 2.4ghz a이면 5ghz인데
        //사용자의 band 입력값에 따라 이를 설정
        String hwMode = "g";
        String channel = "6";
        if ("2".equals(band)) {
            hwMode = "g";
            channel = "6";
        } else if ("5".equals(band)) {
            hwMode = "a";
            channel = "36";
        }
        // hostapd.conf 파일 수정 명령어를 실행하도록 하여 구현
        System.out.println("PATCH 요청 수신됨: " + request);
        return settingService.changeWifiBand(path,hwMode,channel);
    }
    @GetMapping("/reset")
    public String reset(){
        settingService.Reset();
        return "사용자 정보 리셋 완료";
    }
    @GetMapping("/wifipassword")
    public String getWifiConfig() {
        Properties properties = new Properties();

        String ssid = "";
        String password="";
        try (FileInputStream fis = new FileInputStream("etc/hostapd/hostapd.conf")) {
            properties.load(fis);
            // SSID와 비밀번호 가져오기
            ssid = properties.getProperty("ssid");
            password= properties.getProperty("wpa_passphrase");
        } catch (IOException e) {
            return "오류 발생: " + e.getMessage();
        }
        return ssid + password;
    }
    //관리자 아이디 비밀번호 변경
    @PatchMapping("/changeauth")
    public String handleChangePassword(@RequestBody Map<String, String> request) {
        String newusername=request.get("newusername");
        String newpassword = request.get("newpassword");
        if (settingService.changeUserAuth(newusername,newpassword)) {
            return "관리자 정보 변경 성공";
        } else {
            return "관리자 정보 변경 실패";
        }
    }
    //관리자 비밀번호 변경
    @PostMapping("/changepassword")
    public String changeAuthPassword(@RequestBody Map<String, String> request) {
        String password=request.get("password");
        String newpassword=request.get("newpassword");
        String newpasswordcheck=request.get("newpasswordcheck");
        if(authService.authenticate(password)){
            if(newpassword.equals(newpasswordcheck)){
                authService.setAdminPassword(newpassword);
                return "비밀번호 변경 완료";
            }else {
                return "비밀번호 확인 틀림";
            }
        }else{
            return "비밀번호 틀림";
        }
    }
    @PatchMapping("/wifipassword")
    public String changePassword(@RequestBody Map<String,String> request) {
        String ssid=request.get("ssid");
        String newPassword = request.get("newpassword");
        try{
            return settingService.changeWifiPassword(ssid,newPassword,path);
        }catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }

    @PatchMapping("/channel")
    public String changeChannel(@RequestBody Map<String, String> request) {
        String channel = request.get("channel");
        try{
            return settingService.changeWifiChannel(channel,path);
        }catch(Exception e){
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }
}
