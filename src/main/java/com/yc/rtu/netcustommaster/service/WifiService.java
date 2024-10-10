package com.yc.rtu.netcustommaster.service;

import org.springframework.stereotype.Service;

@Service
public class WifiService {

    private String Password = "1234567890";
    //와이파이 비밀번호 변경 메서드
    public boolean changePassword(String oldPassword, String newPassword) {
        if (Password.equals(oldPassword) && newPassword.length() >= 8) {
            Password = newPassword;
            return true;
        }
        return false;
    }
}
