package com.iuni.login.helper;

import com.iuni.login.domain.Member;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encrypt {
    private String getSalt(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);

        StringBuffer stringBuffer = new StringBuffer();
        for(byte b : salt){
            stringBuffer.append(String.format("%20x", b));
        }
        return stringBuffer.toString();
    }
    public String getEncrypt(String password, String salt){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((password + salt).getBytes());
            byte[] passwordSalt = messageDigest.digest();

            StringBuffer stringBuffer = new StringBuffer();
            for(byte b : passwordSalt){
                stringBuffer.append(String.format("%20x", b));
            }

            return stringBuffer.toString().replace(" ","");
        }
        catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public Member makeData(Member member){
        String salt = getSalt();
        member.setSalt(salt);
        member.setPassword(getEncrypt(member.getPassword(), salt));
        return member;
    }
}
