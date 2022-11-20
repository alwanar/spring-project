package com.example.jdbc.transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;



import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;



@Service
public class TestTransactionalService {
    @Autowired
    @Qualifier("testDbJdbcTemplate")
    private JdbcTemplate jdbcTemplateTestDb;
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplatePusat;

    public static class Tutorial {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = { Exception.class, RuntimeException.class })
    public void testTransaction() {
        jdbcTemplateTestDb.update("update tutorial set name = 'test' where id = 6");

        jdbcTemplatePusat.update("insert into log_insert (FIDProgram, NamaProgram) values (465, 'testTrans')");
        throw new RuntimeException("sss");

    }

    public Optional<Tutorial> testData(){
        try {
            Tutorial tutorial1 = jdbcTemplateTestDb.queryForObject("select name from tutorial where id = 30", (rs, rowNum) -> {
                HashMap<String, String> hashMap = new HashMap<>();
                Tutorial tutorial = new Tutorial();
                tutorial.setName(rs.getString(1));
                return tutorial;
            });

            return Optional.of(tutorial1);
        } catch (DataAccessException ex) {
            return Optional.empty();
        }

    }


}
