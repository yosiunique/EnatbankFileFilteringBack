package com.enatbank.file_filtering.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "storage")
@Data
@Configuration
public class Location {
    private String smbServer ;
    private  String shareName ;
    private String folderPath ;
    private   String username ;
    private   String password ;
    private String domain ;                  // Leave empty if not using a domain


}
