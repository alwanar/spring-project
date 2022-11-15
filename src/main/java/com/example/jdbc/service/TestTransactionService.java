package com.example.jdbc.service;

import com.example.jdbc.dto.ProgramInsert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TestTransactionService {
    private JdbcTemplate jdbcTemplate;

    private TestTransactionService2 service2;

    public TestTransactionService(JdbcTemplate jdbcTemplate, TestTransactionService2 service2) {
        this.jdbcTemplate = jdbcTemplate;
        this.service2 = service2;
    }

    @Transactional
    public Map<String, Object> saveData(ProgramInsert programInsert) {
        System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
        Map<String, Object> response = new LinkedHashMap<>();
        service2.doInsert(programInsert);
        throw new RuntimeException("GAgal");
//        try {
//
//
//
//        } catch (Exception e){
//            System.out.println(e.getMessage());
////            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
//        return response;
    }


}
