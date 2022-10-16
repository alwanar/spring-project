package com.example.jdbc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "log_insert")
public class LogInsert implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String FIDProgram;

    private String NamaProgram;

    private Date TanggalMulaiProgram;

    private Date TanggalAwalRealisasi;

    public Integer getId() {
        return id;
    }

    @Column(name = "id_log_insert")
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "FIDProgram")
    public String getFIDProgram() {
        return FIDProgram;
    }

    @Column(name = "FIDProgram")
    public void setFIDProgram(String FIDProgram) {
        this.FIDProgram = FIDProgram;
    }

    @Column(name = "NamaProgram")
    public String getNamaProgram() {
        return NamaProgram;
    }

    @Column(name = "NamaProgram")
    public void setNamaProgram(String namaProgram) {
        NamaProgram = namaProgram;
    }

    @Column(name = "TanggalMulaiProgram")
    public Date getTanggalMulaiProgram() {
        return TanggalMulaiProgram;
    }

    @Column(name = "TanggalMulaiProgram")
    public void setTanggalMulaiProgram(Date tanggalMulaiProgram) {
        TanggalMulaiProgram = tanggalMulaiProgram;
    }

    @Column(name = "TanggalAwalRealisasi")
    public Date getTanggalAwalRealisasi() {
        return TanggalAwalRealisasi;
    }

    @Column(name = "TanggalAwalRealisasi")
    public void setTanggalAwalRealisasi(Date tanggalAwalRealisasi) {
        TanggalAwalRealisasi = tanggalAwalRealisasi;
    }
}
