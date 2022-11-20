package com.example.jdbc.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TransactionManagerConfiguration {

    @Bean
    public PlatformTransactionManager jdbcTestDbTransaction(@Qualifier("devDataSource") DataSource datasource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(datasource);
        return dataSourceTransactionManager;
    }

    @Bean
    public PlatformTransactionManager jdbcPusatDbTransaction(@Qualifier("opPusatDataSource") DataSource datasource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(datasource);
        return dataSourceTransactionManager;
    }
}
