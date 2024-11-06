package com.yc.rtu.netcustommaster.auth.controller;
import com.yc.rtu.netcustommaster.auth.service.AuthService;
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
            return "회원가입페이지";
        }//첫 사용이 아니면 로그인 진행
        else{
            return "로그인페이지";
        }
    }

    @GetMapping("/register")
    public String register(){
        return "회원가입페이지";
    }

    //처음 사용하는 사용자 회원가입
    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> request) {
               String username = request.get("username");
               String password = request.get("password");
               String passwordcheck=request.get("passwordcheck");
               if(passwordcheck.equals(password)){
                   authService.setAdminCredentials(username,password,passwordcheck);
                   return "회원가입 완료";
               }else{
                   return "비밀번호 확인 틀림";
               }
    }

    @GetMapping("/login")
    public String login(){
        return "로그인페이지";
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