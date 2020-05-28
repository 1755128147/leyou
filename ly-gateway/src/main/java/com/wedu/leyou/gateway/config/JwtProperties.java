package com.wedu.leyou.gateway.config;

import com.wedu.leyou.auth.utils.RsaUtils;
import com.wedu.leyou.common.enums.ExceptionEnum;
import com.wedu.leyou.common.exception.LyException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String pubKeyPath;// 公钥

    private PublicKey publicKey; // 公钥

    private String cookieName;

    @PostConstruct
    public void init(){
        try {
            // 获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.CREATE_TOKEN_ERROR);
        }
    }
}