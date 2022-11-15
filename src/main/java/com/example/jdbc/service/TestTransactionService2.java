package com.example.jdbc.service;

import com.example.jdbc.dto.ProgramInsert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TestTransactionService2 {
    private JdbcTemplate jdbcTemplate;

    public TestTransactionService2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional( rollbackOn = Exception.class)
    public int doInsert(ProgramInsert programInsert) {
        String sql = "  insert into log_insert (FIDProgram, NamaProgram) values (?, ?)";
        int update = jdbcTemplate.update(sql, programInsert.getFIDProgram(), programInsert.getNamaProgram());
        return update;
    }
}
