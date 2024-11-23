package com.yc.rtu.netcustommaster.setting.controller;

import com.yc.rtu.netcustommaster.auth.service.AuthService;
import com.yc.rtu.netcustommaster.setting.response.SettingResponseDto;
import com.yc.rtu.netcustommaster.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.yc.rtu.netcustommaster.util.CommandExecutor.executeCommand;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
        return settingService.changeWifiBand(hwMode,channel);
    }

    @GetMapping("/reset")
    public String reset(){
        settingService.Reset();
        return "사용자 정보 리셋 완료";
    }

    @GetMapping("/band")
    public String band(){
        String band="";
        band=settingService.getBand();
        return band;
    }

    @GetMapping("/wifipassword")
    public Object getWifiConfig() {
        List<String> info=new ArrayList<>();
        info.add(settingService.getSsid());
        info.add(settingService.getWifipassword());
        return info;
    }

    //관리자 아이디 비밀번호 변경
    @PatchMapping("/changeauth")
    public String handleChangePassword(@RequestBody Map<String, String> request) {
        String newusername=request.get("newusername");
        String newpassword = request.get("newpassword");
        return settingService.changeUserAuth(newusername,newpassword);
    }
    //관리자 비밀번호 변경
    @PostMapping("/changepassword")
    public String changeAuthPassword(@RequestBody Map<String, String> request) {
        String password=request.get("password");
        String newpassword=request.get("newpassword");
        String newpasswordcheck=request.get("newpasswordcheck");
        SettingResponseDto response=new SettingResponseDto();
        if(authService.authenticate(password)){
            if(newpassword.equals(newpasswordcheck)){
                authService.setAdminPassword(newpassword);
                response.setMessage("변경 완료");
            }else {
                response.setMessage("비밀번호 확인이 맞지 않습니다.");
            }
        }else{
            response.setMessage("비밀번호가 틀렸습니다.");
        }
        return response.getMessage();
    }
    //와이파이 정보 변경
    @PatchMapping("/wifipassword")
    public String changePassword(@RequestBody Map<String,String> request) {
        String ssid=request.get("ssid");
        String newPassword = request.get("newpassword");
        return settingService.changeWifiPassword(ssid,newPassword,path);
    }

    @PatchMapping("/channel")
    public boolean changeChannel(@RequestBody Map<String, String> request) {
        String channel = request.get("channel");
        try{
            return settingService.changeWifiChannel(channel,path);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
