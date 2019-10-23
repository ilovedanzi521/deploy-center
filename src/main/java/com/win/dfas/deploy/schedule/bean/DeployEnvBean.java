package com.win.dfas.deploy.schedule.bean;


import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
public class DeployEnvBean {
    private String homeDir;

    private String deployDir;

    private String modulesDir;

    private String packagesDir;

    private String scriptsDir;

    private String logsDir;

    private String ftpHost;

    private Integer ftpPort;

    private String ftpUser;

    private String ftpPswd;

    private String ftpRepoDir;

    private String ftpTempDir;

    private String releaseDescFile;
}

