package com.roshkatest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initializeDatabase() {
        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("data.sql"));
            populator.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
            populator.setContinueOnError(false);
            
            DatabasePopulatorUtils.execute(populator, dataSource);
            
        } catch (Exception e) {
            System.err.println("Error inicializando base de datos: " + e.getMessage());
        }
    }
}
