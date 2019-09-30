package com.win.dfas.deploy.schedule.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "deploy-center")
public class DeployEnvConfig {
    @Value("home_dir")
    private String homeDir;

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }
    @Value("depoly_dir")
    private String deployDir;

    @Value("modules_dir")
    private String modulesDir;

    @Value("packages_dir")
    private String packagesDir;

    @Value("scripts_dir")
    private String scriptsDir;

    @Value("ftp-server.host")
    private String ftpHost;

    @Value("ftp-server.port")
    private String ftpPort;

    @Value("ftp-server.user")
    private String ftpUser;

    @Value("ftp-server.pswd")
    private String ftpPswd;

    @Value("ftp-server.repo-dir")
    private String ftpRepoDir;

    @Value("relased-description")
    private String releaseDescFile;
}
