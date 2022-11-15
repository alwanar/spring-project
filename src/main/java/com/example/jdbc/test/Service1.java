package com.example.jdbc.test;

import com.example.jdbc.dto.ProgramInsert;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class Service1 implements Penjaminan {
    private Service2 service2;

    public Service1(Service2 service2) {
        this.service2 = service2;
    }

    @Override
    public Map<String, Object> execute(ProgramInsert programInsert) {

        Map<String, Object> response = new LinkedHashMap<>();
        System.out.println("Logic Here!");

        Map<String, Object> i = service2.prosesCoveringValidation(programInsert);


        return i;
    }
}
