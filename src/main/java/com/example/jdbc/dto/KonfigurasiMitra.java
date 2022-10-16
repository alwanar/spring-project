package com.example.jdbc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import java.util.Date;

public class KonfigurasiMitra {
    private String flagDasar;
    private String nomorPksSp3;
    private Date tglAwalPksSp3;
    private Date tglAkhirPksSp3;

    private Integer covMacet;

    private Integer FIDSkemaPerhitunganBunga;

    private Integer id_DD_Bank;

    private Integer idDcProduk;

    private String keteranganProduk;

    private Integer idDcPeruntukanKredit;


    private Integer flag_syariah;

    private Integer PlafonMaksimal;

    private Integer PlafonMinimal;

    private Integer JangkaWaktuMaksimal;

    private Integer JangkaWaktuMinimal;



    public String getFlagDasar() {
        return flagDasar;
    }

    public void setFlagDasar(String flagDasar) {
        this.flagDasar = flagDasar;
    }

    public String getNomorPksSp3() {
        return nomorPksSp3;
    }

    public void setNomorPksSp3(String nomorPksSp3) {
        this.nomorPksSp3 = nomorPksSp3;
    }

    public Date getTglAwalPksSp3() {
        return tglAwalPksSp3;
    }

    public void setTglAwalPksSp3(Date tglAwalPksSp3) {
        this.tglAwalPksSp3 = tglAwalPksSp3;
    }

    public Date getTglAkhirPksSp3() {
        return tglAkhirPksSp3;
    }

    public void setTglAkhirPksSp3(Date tglAkhirPksSp3) {
        this.tglAkhirPksSp3 = tglAkhirPksSp3;
    }

    public Integer getCovMacet() {
        return covMacet;
    }

    public void setCovMacet(Integer covMacet) {
        this.covMacet = covMacet;
    }

    public Integer getFIDSkemaPerhitunganBunga() {
        return FIDSkemaPerhitunganBunga;
    }

    public void setFIDSkemaPerhitunganBunga(Integer FIDSkemaPerhitunganBunga) {
        this.FIDSkemaPerhitunganBunga = FIDSkemaPerhitunganBunga;
    }

    public Integer getId_DD_Bank() {
        return id_DD_Bank;
    }

    @JsonProperty("idDDBank")
    public void setId_DD_Bank(Integer id_DD_Bank) {
        this.id_DD_Bank = id_DD_Bank;
    }

    public Integer getIdDcProduk() {
        return idDcProduk;
    }

    public void setIdDcProduk(Integer idDcProduk) {
        this.idDcProduk = idDcProduk;
    }

    public String getKeteranganProduk() {
        return keteranganProduk;
    }

    public void setKeteranganProduk(String keteranganProduk) {
        this.keteranganProduk = keteranganProduk;
    }

    public Integer getIdDcPeruntukanKredit() {
        return idDcPeruntukanKredit;
    }

    public void setIdDcPeruntukanKredit(Integer idDcPeruntukanKredit) {
        this.idDcPeruntukanKredit = idDcPeruntukanKredit;
    }

    public Integer getFlag_syariah() {
        return flag_syariah;
    }

    @JsonProperty("flagSyariah")
    public void setFlag_syariah(Integer flag_syariah) {
        this.flag_syariah = flag_syariah;
    }

    public Integer getPlafonMaksimal() {
        return PlafonMaksimal;
    }

    public void setPlafonMaksimal(Integer plafonMaksimal) {
        PlafonMaksimal = plafonMaksimal;
    }

    public Integer getPlafonMinimal() {
        return PlafonMinimal;
    }

    public void setPlafonMinimal(Integer plafonMinimal) {
        PlafonMinimal = plafonMinimal;
    }

    public Integer getJangkaWaktuMaksimal() {
        return JangkaWaktuMaksimal;
    }

    public void setJangkaWaktuMaksimal(Integer jangkaWaktuMaksimal) {
        JangkaWaktuMaksimal = jangkaWaktuMaksimal;
    }

    public Integer getJangkaWaktuMinimal() {
        return JangkaWaktuMinimal;
    }

    public void setJangkaWaktuMinimal(Integer jangkaWaktuMinimal) {
        JangkaWaktuMinimal = jangkaWaktuMinimal;
    }
}
