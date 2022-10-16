package com.example.jdbc.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class Datasource {
//    @Bean
//    public Datasource datasource(){
//        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
//        driverManagerDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        driverManagerDataSource.setUrl("jdbc:sqlserver://localhost;database=op_pusat");
//        driverManagerDataSource.setUsername("Demo");
//        driverManagerDataSource.setPassword("Uat46");
//        return driverManagerDataSource;
//    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl("jdbc:sqlserver://localhost:1433;database=op_pusat;Encrypt=true;trustServerCertificate=true");
        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ds.setUsername("Demo");
        ds.setPassword("Uat46");
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
