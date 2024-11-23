package com.yc.rtu.netcustommaster.setting.service;

import com.yc.rtu.netcustommaster.auth.service.AuthService;
import com.yc.rtu.netcustommaster.setting.response.SettingResponseDto;
import com.yc.rtu.netcustommaster.setting.response.WifiInfoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.FileInputStream;
import java.io.IOException;

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
    public String changeUserAuth(String username,String password){
        SettingResponseDto settingResponse=new SettingResponseDto();
        Properties config = authService.loadConfig();
        config.setProperty("admin.username", username);
        config.setProperty("admin.password", password);
        try{
            authService.saveConfig(config);
            settingResponse.setMessage("비밀번호 변경 성공");
        } catch (Exception e) {
            settingResponse.setMessage("비밀번호 변경 실패");
        }
        return settingResponse.getMessage();
    }
    //와이파이 채널변경
    public boolean changeWifiChannel(String channel,String path){
        String command = String.format(
                "cd /etc/hostapd && sudo sed -i 's/channel=[0-9]*$/channel=%s/' %s && "+
                        "sudo systemctl restart hostapd",
                channel,path
        );
        System.out.println(command);
        String result = executeCommand(command);
        return true;
    }
    //현재 대역폭 확인
    public String getBand(){
        Properties properties=new Properties();
        String hwmode="";
        String band="";
        try(FileInputStream fis = new FileInputStream("/etc/hostapd/hostapd.conf")){
            properties.load(fis);
            hwmode=properties.getProperty("hw_mode");
            if(hwmode.equals("g")){
                band="2";
            }else if(hwmode.equals("a")){
                band="5";
            }
        }catch(IOException e){
            return e.getMessage();
        }
        return band;
    }
    //현재 ssid 확인
    public String getSsid(){
        Properties properties=new Properties();
        String ssid="";
        try(FileInputStream fis = new FileInputStream("/etc/hostapd/hostapd.conf")){
            properties.load(fis);
            ssid=properties.getProperty("ssid");
        }catch(IOException e){
            return e.getMessage();
        }
        return ssid;
    }
    //현재 와이파이 채널 확인
    public String getChannel(){
        Properties properties=new Properties();
        String channel="";
        try(FileInputStream fis = new FileInputStream("/etc/hostapd/hostapd.conf")){
            properties.load(fis);
            channel=properties.getProperty("channel");
        }catch(IOException e){
            return e.getMessage();
        }
        return channel;
    }
    //현재 와이파이 비밀번호 확인
    public String getWifipassword(){
        Properties properties=new Properties();
        String wifipassword="";
        try(FileInputStream fis = new FileInputStream("/etc/hostapd/hostapd.conf")){
            properties.load(fis);
            wifipassword=properties.getProperty("wpa_passphrase");
        }catch(IOException e){
            return e.getMessage();
        }
        return wifipassword;
    }
    //와이파이 정보 반환
    public WifiInfoResponseDto getWifiInfo() {
        WifiInfoResponseDto wifiInfo=new WifiInfoResponseDto();
        wifiInfo.setBand(getBand());
        wifiInfo.setSsid(getSsid());
        wifiInfo.setChannel(getChannel());
        wifiInfo.setWifipassword(getWifipassword());
        return wifiInfo;
    }
    //와이파이 대역폭변경
    public String changeWifiBand(String path,String hwMode,String channel){
        SettingResponseDto response=new SettingResponseDto();
        String command = String.format(
                "cd /etc/hostapd && sudo sed -i 's/hw_mode=.*/hw_mode=%s/' %s && " +
                        "sudo sed -i 's/channel=[0-9]*$/channel=%s/' %s && "+
                        "sudo systemctl restart hostapd",
                hwMode,path,channel,path
        );
        try{
            executeCommand(command);
            response.setMessage("Wifi 대역폭 변경 성공");
        }catch(Exception e){
            response.setMessage("Wifi 대역폭 변경 실패");
        }
        return response.getMessage();
    }
    //와이파이 비밀번호 변경
    public String changeWifiPassword(String ssid,String password,String path){
        SettingResponseDto response=new SettingResponseDto();
        String command = String.format(
                "cd /etc/hostapd && sudo sed -i 's/ssid=.*/ssid=%s/' %s && sudo sed -i 's/wpa_passphrase=.*/wpa_passphrase=%s/' %s && sudo systemctl restart hostapd",
                ssid,path,password, path
        );
        try{
            executeCommand(command);
            response.setMessage("와이파이 비밀번호 변경 성공");
        } catch (Exception e) {
            response.setMessage("와이파이 비밀번호 변경 실패");
        }
        return response.getMessage();
    }
}
