package ru.antonov.bdid2.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataConfig {

    @Autowired
    private Environment env;

    /**
     * Gets data source.
     *
     * @return the data source
     */
    public DataSource getDataSource() {

        log.info("Setting up datasource for {} environment.", env.getActiveProfiles());

        DataSource dataSource = DataSourceBuilder.create()
            .url("jdbc:postgresql://172.26.155.26:5432/db04?currentSchema=citizenship")
            .username("dwh_admin")
            .password("qwerty$4")
            .build();
        String maxPoolSize = env.getProperty("spring.datasource.hikari.maximum-pool-size");
        assert maxPoolSize != null;
        ((HikariDataSource) dataSource)
            .setMaximumPoolSize(Integer.parseInt(maxPoolSize));

        return dataSource;
    }

    @Bean
    public DataSource dataSource() {
        return getDataSource();
    }


}
