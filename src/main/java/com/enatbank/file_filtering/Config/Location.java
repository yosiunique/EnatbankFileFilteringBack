package com.enatbank.file_filtering.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "storage")
@Data
@Configuration
public class Location {
    private String location;

}
