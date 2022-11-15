package com.example.jdbc.test;

import com.example.jdbc.dto.ProgramInsert;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class Service2 {

    private DBDao dbDao;

    public Service2(DBDao dbDao) {
        this.dbDao = dbDao;
    }

    @Transactional
    public Map<String, Object> prosesCoveringValidation(ProgramInsert programInsert){
        Map<String, Object> response = new LinkedHashMap<>();
        int i = dbDao.doInsert(programInsert);

        if (i == 1){
            throw new RuntimeException("GAgal");
//          response.put("statusMessage", 0);
//          response.put("description", "success");
//          return response;
        }

        response.put("statusMessage", 1);
        response.put("description", "Gagal");

        return response;
    }
}
