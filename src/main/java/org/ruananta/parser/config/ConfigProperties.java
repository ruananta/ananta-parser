package org.ruananta.parser.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {

    @NotNull
    private String linkRegex;

    public String getLinkRegex() {
        return linkRegex;
    }

    public void setLinkRegex(String linkRegex) {
        this.linkRegex = linkRegex;
    }
}
