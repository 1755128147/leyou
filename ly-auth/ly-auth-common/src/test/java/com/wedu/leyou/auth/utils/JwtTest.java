package com.wedu.leyou.auth.utils;

import com.wedu.leyou.auth.pojo.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "E:/project/test/rsa/rsa.pub";

    private static final String priKeyPath = "E:/project/test/rsa/rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU5MDEyNjcwM30.iy7N_78wG0ig1NzHce6TbBMW4i50IyaVwTyJKm1CASxNKMIaRIloaDg73-AiCRMLHnmRIiZyDjPZ8S8OOXrFNTp63YzHiJcQt7dMrulG4eFgu_YmgaPygfugEqDUu7qmJjcANBEzZ5SWp0EvsmCxNvqD8n4SNlKyFpnEQ2n-Du0";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}