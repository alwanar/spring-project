package com.example.jdbc.test;

import com.example.jdbc.dto.ProgramInsert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class DBDao {

    private JdbcTemplate jdbcTemplate;

    public DBDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional( rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public int doInsert(ProgramInsert programInsert) {
        String sql = "  insert into log_insert (FIDProgram, NamaProgram) values (?, ?)";
        int update = jdbcTemplate.update(sql, programInsert.getFIDProgram(), programInsert.getNamaProgram());
        if (update == 1){
            throw new RuntimeException("GAgal");
        }
        return update;
    }
}
