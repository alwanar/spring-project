package com.example.jdbc.dto;

import com.example.jdbc.entity.LogInsert;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgramInsert {
    private String FIDProgram;
    private String NamaProgram;

    private String TanggalMulaiProgram;
    private String TanggalAwalRealisasi;

    public String getFIDProgram() {
        return FIDProgram;
    }

    @JsonProperty("FIDProgram")
    public void setFIDProgram(String FIDProgram) {
        this.FIDProgram = FIDProgram;
    }


    public String getNamaProgram() {
        return NamaProgram;
    }

    @JsonProperty("NamaProgram")
    public void setNamaProgram(String namaProgram) {
        NamaProgram = namaProgram;
    }

    public String getTanggalMulaiProgram() {
        return TanggalMulaiProgram;
    }

    @JsonProperty("TanggalMulaiProgram")
    public void setTanggalMulaiProgram(String tanggalMulaiProgram) {
        TanggalMulaiProgram = tanggalMulaiProgram;
    }

    public String getTanggalAwalRealisasi() {
        return TanggalAwalRealisasi;
    }

    @JsonProperty("TanggalAwalRealisasi")
    public void setTanggalAwalRealisasi(String tanggalAwalRealisasi) {
        TanggalAwalRealisasi = tanggalAwalRealisasi;
    }

    public LogInsert convertToEntity(ProgramInsert programInsert) {

        java.sql.Date tanggalMulai = new java.sql.Date(convertDate(programInsert.getTanggalMulaiProgram()));
        java.sql.Date tanggalAwal = new java.sql.Date(convertDate(programInsert.getTanggalAwalRealisasi()));

        LogInsert logInsert = new LogInsert();
        logInsert.setFIDProgram(programInsert.getFIDProgram());
        logInsert.setNamaProgram(programInsert.getNamaProgram());
        logInsert.setTanggalMulaiProgram(tanggalMulai);
        logInsert.setTanggalAwalRealisasi(tanggalAwal);

        return logInsert;
    }

    public long convertDate(String value) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d = f.parse(value);
            long milliseconds = d.getTime();
            return milliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
