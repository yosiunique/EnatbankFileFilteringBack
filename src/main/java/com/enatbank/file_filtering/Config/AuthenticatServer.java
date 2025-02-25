package com.enatbank.file_filtering.Config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "smb")
public class AuthenticatServer {
    private String  server;
   private String userName;
   private String password;
}
