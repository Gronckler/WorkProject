package com.example.architecture.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

//    @Bean("dataSourceOne")
//    @ConfigurationProperties(prefix = "spring.datasource.one")
//    DataSource dataSourceOne(){
//        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
//        dataSource.setMaxActive(100);
//        return dataSource;
//    }

//    @Bean("dataSourceTwo")
//    @ConfigurationProperties(prefix = "spring.datasource.two")
//    DataSource dataSourceTwo(){
//        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
//        dataSource.setMaxActive(100);
//        return dataSource;
//    }

    @Bean("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource dataSource(){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        System.out.println(dataSource.isEnable());
//        dataSource.setMaxActive(100);
        return dataSource;
    }
}
