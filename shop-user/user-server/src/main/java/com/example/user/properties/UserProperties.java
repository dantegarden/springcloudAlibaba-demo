package com.example.user.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-26 22:44
 */
@Component
@ConfigurationProperties(prefix = "user")
@RefreshScope
@Data
public class UserProperties {
    private String tokenKey;
    private Integer expire;
}
