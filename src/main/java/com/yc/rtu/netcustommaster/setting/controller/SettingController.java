package com.yc.rtu.netcustommaster.setting.controller;

import com.yc.rtu.netcustommaster.auth.service.AuthService;
import com.yc.rtu.netcustommaster.setting.response.SettingResponseDto;
import com.yc.rtu.netcustommaster.setting.response.WifiInfoResponseDto;
import com.yc.rtu.netcustommaster.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        String hwMode = "g";
        String channel = "6";
        if (band.equals("2")) {
            hwMode = "g";
            channel = "6";
        } else if (band.equals("5")) {
            hwMode = "a";
            channel = "36";
        }
        String message=settingService.changeWifiBand(path,hwMode,channel).getMessage();
        return message;
    }

    @GetMapping("/reset")
    public ResponseEntity<String> reset(){
        return ResponseEntity.ok(settingService.Reset());
    }

    @GetMapping("/band")
    public ResponseEntity<String> band(){
        String band=settingService.getBand();
        return ResponseEntity.ok(band);
    }

    @GetMapping("/wifipassword")
    public ResponseEntity<List<String>> getWifiConfig() {
        List<String> info=new ArrayList<>();
        info.add(settingService.getSsid());
        info.add(settingService.getWifipassword());
        return ResponseEntity.ok(info);
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
