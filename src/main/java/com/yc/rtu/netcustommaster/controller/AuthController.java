package com.yc.rtu.netcustommaster.controller;
import com.yc.rtu.netcustommaster.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    //시작 시 로그인 체크
    @GetMapping("/check-login")
    public String checkLogin() {
        //config.properties 파일을 통하여 첫 사용인지 체크함
        //첫 사용이면 회원가입 페이지로 넘어가게해야함
        if(authService.isFirstTime()){
            return "처음 사용하는 사용자입니다. 아이디 비밀번호 만드세요";
        }//첫 사용이 아니면 로그인 진행
        //자동 로그인 시에는 properties와 비교하여 auto-login?
        else{
            return "로그인해주세요";
        }
    }

    //처음 사용하는 사용자 회원가입
    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> request) {
               String adminUsername = request.get("username");
               String adminPassword = request.get("password");
               String wifiPassword = request.get("wifipassword");
               authService.setAdminAndWifiCredentials(adminUsername, adminPassword, wifiPassword);
               return "회원가입 완료";
    }
    //로그인 기능
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request) {
            String username = request.get("username");
            String password = request.get("password");
            // 로그인 처리
            if (authService.authenticate(username, password)) {
                return "로그인 성공";
            } else {
                return "로그인 실패";
            }
    }

}