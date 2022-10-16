package com.example.jdbc.service;

import com.example.jdbc.dto.ProgramInsert;
import com.example.jdbc.entity.LogInsert;
import com.example.jdbc.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProgramInsertService {


    private TestRepository testRepository;

    public ProgramInsertService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public Object programInsert(ProgramInsert programInsert) {
        Map<String, String> responseBody = new LinkedHashMap<>();

        if (!validation(programInsert).isEmpty()) {
            List<String> validation = validation(programInsert);
            String s = String.join(",", validation).toString();
            responseBody.put("ResponseCode", "01");
            responseBody.put("ResponseDescription", "Failed");
            responseBody.put("ResponseException", "field request " + s + " tidak ada");
            return responseBody;
        } else {
            Map<String, String> stringStringMap = programInsertVd(programInsert);

            System.out.println("apa tuch : " + stringStringMap.get("ResponseCode"));

            if (stringStringMap.get("ResponseCode").equals("01")) {
                return stringStringMap;
            } else {

                //validasi database pakai if;


                if (testRepository.existsByFIDProgram(programInsert.getFIDProgram()) >= 1) {
                    System.out.println("exist");
                    responseBody.put("ResponseCode", "01");
                    responseBody.put("ResponseDescription", "Failed");
                    responseBody.put("ResponseException", "FIDProgram sudah pernah diajukan");
                    return responseBody;
                } else {
                    System.out.println("masuk else");
                    LogInsert logInsert = programInsert.convertToEntity(programInsert);
                    int save = testRepository
                            .saveLog(logInsert.getFIDProgram(), logInsert.getNamaProgram(),
                                    logInsert.getTanggalMulaiProgram(), logInsert.getTanggalAwalRealisasi());

                    System.out.println("save " + save);

                    if (save == 1) {
                        responseBody.put("ResponseCode", "00");
                        responseBody.put("ResponseDescription", "Success");
                        responseBody.put("ResponseException", "");
                        return responseBody;
                    } else {
                        responseBody.put("ResponseCode", "01");
                        responseBody.put("ResponseDescription", "Failed");
                        responseBody.put("ResponseException", "");
                        return responseBody;
                    }
                }
            }
        }
    }

    public List<String> validation(ProgramInsert programInsert) {
        List<String> requestError = new ArrayList<>();

        if (programInsert.getFIDProgram() == null) requestError.add("FIDProgram");
        if (programInsert.getNamaProgram() == null) requestError.add("NamaProgram");
        if (programInsert.getTanggalMulaiProgram() == null) requestError.add("TanggalMulaiProgram");
        if (programInsert.getTanggalAwalRealisasi() == null) requestError.add("TanggalAwalRealisasi");

        return requestError;
    }


    private Map<String, String> programInsertVd(ProgramInsert programInsert) {
        Map<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("ResponseCode", "00");
        responseBody.put("ResponseDescription", "Success");
        responseBody.put("ResponseException", "");

        if ("".equals(programInsert.getFIDProgram())) {
            responseBody.put("ResponseCode", "01");
            responseBody.put("ResponseDescription", "Failed");
            responseBody.put("ResponseException", "FIDProgram tidak boleh kosong");
            return responseBody;
        }

        if ("".equals(programInsert.getNamaProgram())) {
            responseBody.put("ResponseCode", "01");
            responseBody.put("ResponseDescription", "Failed");
            responseBody.put("ResponseException", "NamaProgram tidak boleh kosong");
            return responseBody;
        }

        if ("".equals(programInsert.getTanggalMulaiProgram())) {
            responseBody.put("ResponseCode", "01");
            responseBody.put("ResponseDescription", "Failed");
            responseBody.put("ResponseException", "TanggalMulaiProgram tidak boleh kosong");
            return responseBody;
        } else {
            if (!this.isValidFormat(programInsert.getTanggalMulaiProgram())) {
                responseBody.put("ResponseCode", "01");
                responseBody.put("ResponseDescription", "Failed");
                responseBody.put("ResponseException", "TanggalMulaiProgram tidak sesuai format dd/MM/yyyy");
                return responseBody;
            }

        }

        if ("".equals(programInsert.getTanggalAwalRealisasi())) {
            responseBody.put("ResponseCode", "01");
            responseBody.put("ResponseDescription", "Failed");
            responseBody.put("ResponseException", "TanggalAwalRealisasi tidak boleh kosong");
            return responseBody;
        } else {
            if (!this.isValidFormat(programInsert.getTanggalAwalRealisasi())) {
                responseBody.put("ResponseCode", "01");
                responseBody.put("ResponseDescription", "Failed");
                responseBody.put("ResponseException", "TanggalAwalRealisasi tidak sesuai format dd/MM/yyyy");
                return responseBody;
            }
        }

        return responseBody;
    }

    private boolean isValidFormat(String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    private Boolean validasiTanggal(String value) {
        String[] t = value.split("/");

        System.out.println("Masuk : " + (Integer.valueOf(t[0]) <= 31 && Integer.valueOf(t[1]) <= 12));
        return (Integer.valueOf(t[0]) <= 31 && Integer.valueOf(t[1]) <= 12);
    }
}
