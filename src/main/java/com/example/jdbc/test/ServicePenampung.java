package com.example.jdbc.test;

import com.example.jdbc.dto.ProgramInsert;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ServicePenampung {
    private Service1 service1;

    public ServicePenampung(Service1 service1) {
        this.service1 = service1;
    }

    public Map<String, Object> doIt(ProgramInsert programInsert) {
        Penjaminan  penjaminan = null;
        if (true){
            penjaminan = service1;
            return penjaminan.execute(programInsert);
        }
        return null;
    }
}
