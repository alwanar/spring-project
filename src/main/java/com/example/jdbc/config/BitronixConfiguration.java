package com.example.jdbc.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.util.Properties;

import com.example.jdbc.SpringJdbcApplication;

import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
public class BitronixConfiguration {
    private static final Logger log = LoggerFactory.getLogger(BitronixConfiguration.class);

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager userTransactionManager() throws SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setTransactionTimeout(300);
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    @Bean
    public JtaTransactionManager transactionManager() throws SystemException {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransactionManager());
        return jtaTransactionManager;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public AtomikosDataSourceBean inventoryDataSource() {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("db1");
        dataSource.setXaDataSourceClassName("org.apache.derby.jdbc.EmbeddedXADataSource");
        Properties xaProperties = new Properties();
        xaProperties.put("databaseName", "db1");
        xaProperties.put("createDatabase", "create");
        dataSource.setXaProperties(xaProperties);
        dataSource.setPoolSize(10);
        return dataSource;
    }

    @Bean
    public DataSourceProperties inventoryEntityManager() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.baeldung.atomikos.spring.jpa.inventory");
        factory.setDataSource(inventoryDataSource());
        Properties jpaProperties = new Properties();
        //jpaProperties.put("hibernate.show_sql", "true");
        //jpaProperties.put("hibernate.format_sql", "true");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
        jpaProperties.put("hibernate.current_session_context_class", "jta");
        jpaProperties.put("javax.persistence.transactionType", "jta");
        jpaProperties.put("hibernate.transaction.manager_lookup_class", "com.atomikos.icatch.jta.hibernate3.TransactionManagerLookup");
        jpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");
        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
