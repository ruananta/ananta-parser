package org.ruananta.parser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.ruananta.parser.config")
public class EngineConfig {

    @Bean
    public ConfigProperties configProperties() {
        return new ConfigProperties();
    }
}
