package com.example.jdbc.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChainedTransactionsConfiguration {

    @Bean("chainedTransaction")
    public PlatformTransactionManager chPlatformTransactionManager(
            @Qualifier("jdbcTestDbTransaction") PlatformTransactionManager jdbcTestTransaction,
            @Qualifier("jdbcPusatDbTransaction") PlatformTransactionManager jdbcPusatTransaction
    ){
        ChainedTransactionManager transactionManager = new ChainedTransactionManager(
                jdbcTestTransaction, jdbcPusatTransaction
        );
        return transactionManager;
    }
}
